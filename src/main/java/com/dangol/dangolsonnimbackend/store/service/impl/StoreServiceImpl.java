package com.dangol.dangolsonnimbackend.store.service.impl;

import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.InternalServerException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import com.dangol.dangolsonnimbackend.store.domain.Category;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.dto.StoreResponseDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreUpdateDTO;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import com.dangol.dangolsonnimbackend.store.repository.StoreRepository;
import com.dangol.dangolsonnimbackend.store.repository.dsl.CategoryQueryRepository;
import com.dangol.dangolsonnimbackend.store.repository.dsl.StoreQueryRepository;
import com.dangol.dangolsonnimbackend.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreQueryRepository storeQueryRepository;
    private final CategoryQueryRepository categoryQueryRepository;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository, StoreQueryRepository storeQueryRepository, CategoryQueryRepository categoryQueryRepository) {
        this.storeRepository = storeRepository;
        this.storeQueryRepository = storeQueryRepository;
        this.categoryQueryRepository = categoryQueryRepository;
    }

    /**
     * 신규 가게 정보를 통해 새로운 가게 정보를 생성한다.
     *
     * @param dto 가게 정보가 담긴 DTO 객체
     */
    @Override
    @Transactional
    public StoreResponseDTO create(StoreSignupRequestDTO dto) {
        if(storeQueryRepository.existsByRegisterNumber(dto.getRegisterNumber()))
            throw new BadRequestException(ErrorCodeMessage.ALREADY_EXISTS_STORE_REGISTER_NUMBER);

        // 카테고리 정보는 스키마에 사전 반영이 되어있어야 한다.
        Category category = categoryQueryRepository
                .findByCategoryType(dto.getCategoryType())
                .orElse(new Category(dto.getCategoryType()));

        Store store = new Store(dto, category);
        storeRepository.save(store);

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
    public StoreResponseDTO updateStoreByDto(StoreUpdateDTO dto) {
        Optional<Store> store =  storeQueryRepository.findByRegisterNumber(dto.getRegisterNumber());
        Optional<Category> category = dto.getCategoryType().map(categoryType ->
                categoryQueryRepository.findByCategoryType(categoryType).get());

        if(store.isEmpty())
            throw new BadRequestException(ErrorCodeMessage.STORE_NOT_FOUND);

        return store
                .filter(o -> o.getRegisterNumber() != null)
                .flatMap(o -> o.update(dto, category))
                .map(StoreResponseDTO::new)
                .orElseThrow(() -> new InternalServerException(ErrorCodeMessage.RESPONSE_CREATE_ERROR));
    }
}
