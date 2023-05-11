package com.dangol.dangolsonnimbackend.store.service.impl;

import com.dangol.dangolsonnimbackend.boss.domain.Boss;
import com.dangol.dangolsonnimbackend.boss.repository.dsl.BossQueryRepository;
import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.InternalServerException;
import com.dangol.dangolsonnimbackend.errors.NotFoundException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import com.dangol.dangolsonnimbackend.file.service.FileService;
import com.dangol.dangolsonnimbackend.store.domain.*;
import com.dangol.dangolsonnimbackend.store.dto.*;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import com.dangol.dangolsonnimbackend.store.repository.StoreImageRepository;
import com.dangol.dangolsonnimbackend.store.repository.StoreRepository;
import com.dangol.dangolsonnimbackend.store.repository.dsl.CategoryQueryRepository;
import com.dangol.dangolsonnimbackend.store.repository.dsl.StoreQueryRepository;
import com.dangol.dangolsonnimbackend.store.service.StoreService;
import com.dangol.dangolsonnimbackend.store.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
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
    private final FileService fileService;
    private final StoreImageRepository storeImageRepository;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository, StoreQueryRepository storeQueryRepository, CategoryQueryRepository categoryQueryRepository, TagService tagService, BossQueryRepository bossQueryRepository, FileService fileService, StoreImageRepository storeImageRepository) {
        this.storeRepository = storeRepository;
        this.storeQueryRepository = storeQueryRepository;
        this.categoryQueryRepository = categoryQueryRepository;
        this.tagService = tagService;
        this.bossQueryRepository = bossQueryRepository;
        this.fileService = fileService;
        this.storeImageRepository = storeImageRepository;
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
        store.setTags(tagService.getOrCreateTags(Optional.ofNullable(dto.getTags()).orElse(Collections.emptyList())));
        // BusinessHour 생성
        List<BusinessHour> businessHours = new ArrayList<>();
        for (BusinessHourRequestDTO businessHourRequestDTO : dto.getBusinessHours()) {
            BusinessHour businessHour = new BusinessHour(businessHourRequestDTO, store);
            businessHours.add(businessHour);
        }
        store.setBusinessHours(businessHours);

        return new StoreResponseDTO(store);
    }

    /**
     * 가게 ID 값을 통해 가게 정보를 조회한다.
     *
     * @param id 가게 ID
     * @return 변경된 정보
     */
    @Override
    public StoreDetailResponseDTO findById(Long id) {
        Store store = storeQueryRepository.findById(id).orElseThrow(
                () -> new BadRequestException(ErrorCodeMessage.STORE_NOT_FOUND)
        );

        return new StoreDetailResponseDTO(store);
    }

    /**
     * 새로 전달받은 StoreUpdateDTO 객체를 통해 가게 상세 정보를 수정한다.
     *
     * @param dto 수정하고자 하는 가게의 정보
     * @return 변경된 정보
     */
    @Override
    @Transactional
    public StoreResponseDTO updateStoreByDto(StoreUpdateDTO dto, Long storeId) {
        Optional<Store> store =  storeQueryRepository.findById(storeId);
        Optional<Category> category = dto.getCategoryType().map(categoryType ->
                categoryQueryRepository.findByCategoryType(categoryType).get());

        if(store.isEmpty())
            throw new BadRequestException(ErrorCodeMessage.STORE_NOT_FOUND);

        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            store.get().setTags(tagService.getOrCreateTags(dto.getTags()));
        }

        if (dto.getBusinessHours() != null && !dto.getBusinessHours().isEmpty()) {
            List<BusinessHour> businessHours = new ArrayList<>();
            for (BusinessHourRequestDTO businessHourRequestDTO : dto.getBusinessHours()) {
                BusinessHour businessHour = new BusinessHour(businessHourRequestDTO, store.get());
                businessHours.add(businessHour);
            }
            store.get().setBusinessHours(businessHours);
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

    @Override
    @Transactional
    public void imageUpload(StoreImageUploadRequestDTO dto) {
        Store store = storeQueryRepository.findById(dto.getStoreId()).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.STORE_NOT_FOUND)
        );

        List<StoreImage> storeImages = new ArrayList<>();
        for(MultipartFile multipartFile : dto.getMultipartFile()){
            String s3FileUrl = fileService.upload(multipartFile);
            StoreImage storeImage = storeImageRepository.save(new StoreImage(store, s3FileUrl));
            storeImages.add(storeImage);
        }

        // 기존 이미지가 있다면 전부 삭제
        for(StoreImage storeImage : store.getStoreImages()){
            fileService.fileDelete(storeImage.getImageUrl());
            storeImageRepository.delete(storeImage);
        }

        store.setStoreImages(storeImages);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StoreResponseDTO> findStoreList(String sigungu, CategoryType category, String kw, Pageable pageable){

        if(category == CategoryType.NONE){
            return storeRepository.findAllBySigunguContainingAndNameContaining(sigungu, kw, pageable)
                    .map(StoreResponseDTO::new);
        }

        return storeRepository.findAllBySigunguContainingAndCategory_CategoryTypeAndNameContaining(sigungu, category, kw, pageable)
                .map(StoreResponseDTO::new);
    }
}
