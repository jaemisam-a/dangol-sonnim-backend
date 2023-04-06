package com.dangol.dangolsonnimbackend.store.service.impl;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.repository.dsl.BossQueryRepository;
import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.InternalServerException;
import com.dangol.dangolsonnimbackend.errors.NotFoundException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import com.dangol.dangolsonnimbackend.store.domain.Category;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.dto.StoreResponseDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreUpdateDTO;
import com.dangol.dangolsonnimbackend.store.repository.StoreRepository;
import com.dangol.dangolsonnimbackend.store.repository.dsl.CategoryQueryRepository;
import com.dangol.dangolsonnimbackend.store.repository.dsl.StoreQueryRepository;
import com.dangol.dangolsonnimbackend.store.service.StoreService;
import com.dangol.dangolsonnimbackend.store.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreQueryRepository storeQueryRepository;
    private final CategoryQueryRepository categoryQueryRepository;
    private final TagService tagService;
    private final BossQueryRepository bossQueryRepository;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository, StoreQueryRepository storeQueryRepository, CategoryQueryRepository categoryQueryRepository, TagService tagService, BossQueryRepository bossQueryRepository) {
        this.storeRepository = storeRepository;
        this.storeQueryRepository = storeQueryRepository;
        this.categoryQueryRepository = categoryQueryRepository;
        this.tagService = tagService;
        this.bossQueryRepository = bossQueryRepository;
    }

    /**
     * 신규 가게 정보를 통해 새로운 가게 정보를 생성한다.
     *
     * @param dto 가게 정보가 담긴 DTO 객체
     */
    @Override
    @Transactional
    public StoreResponseDTO create(StoreSignupRequestDTO dto, String email) {
        if(storeQueryRepository.existsByRegisterNumber(dto.getRegisterNumber()))
            throw new BadRequestException(ErrorCodeMessage.ALREADY_EXISTS_STORE_REGISTER_NUMBER);
        Boss boss = Optional.ofNullable(bossQueryRepository.findByEmail(email)).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.BOSS_NOT_FOUND)
        );

        // 카테고리 정보는 스키마에 사전 반영이 되어있어야 한다.
        Category category = categoryQueryRepository
                .findByCategoryType(dto.getCategoryType())
                .orElse(new Category(dto.getCategoryType()));

        Store store = new Store(dto, category, boss);
        storeRepository.save(store);

        // 태그를 추가해주는 과정입니다.
        store.setTags(tagService.getOrCreateTags(dto.getTags()));

        return new StoreResponseDTO(store);
    }

    /**
     * 가게 ID 값을 통해 가게 정보를 조회한다.
     *
     * @param id 가게 ID
     * @return 변경된 정보
     */
    @Override
    public StoreResponseDTO findById(Long id) {
        Optional<Store> store = storeQueryRepository.findById(id);

        if(store.isEmpty())
            throw new BadRequestException(ErrorCodeMessage.STORE_NOT_FOUND);

        return store.map(StoreResponseDTO::new)
                .orElseThrow(() -> new InternalServerException(ErrorCodeMessage.RESPONSE_CREATE_ERROR));
    }

    /**
     * 새로 전달받은 StoreUpdateDTO 객체를 통해 가게 상세 정보를 수정한다.
     *
     * @param dto 수정하고자 하는 가게의 정보
     * @return 변경된 정보
     */
    @Override
    @Transactional
    public StoreResponseDTO updateStoreByDto(StoreUpdateDTO dto) {
        Optional<Store> store =  storeQueryRepository.findByRegisterNumber(dto.getRegisterNumber());
        Optional<Category> category = dto.getCategoryType().map(categoryType ->
                categoryQueryRepository.findByCategoryType(categoryType).get());

        if(store.isEmpty())
            throw new BadRequestException(ErrorCodeMessage.STORE_NOT_FOUND);

        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            store.get().setTags(tagService.getOrCreateTags(dto.getTags()));
        }

        return store
                .filter(o -> o.getRegisterNumber() != null)
                .flatMap(o -> o.update(dto, category))
                .map(StoreResponseDTO::new)
                .orElseThrow(() -> new InternalServerException(ErrorCodeMessage.RESPONSE_CREATE_ERROR));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreResponseDTO> findMyStore(String email) {
        return storeQueryRepository.findMyStore(email).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.STORE_NOT_FOUND)
        ).stream().map(StoreResponseDTO::new).collect(Collectors.toList());
    }
}
