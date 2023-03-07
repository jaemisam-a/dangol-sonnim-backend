package com.dangol.dangolsonnimbackend.store.service.impl;

import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.InternalServerException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.dto.StoreResponseDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreUpdateDTO;
import com.dangol.dangolsonnimbackend.store.repository.StoreRepository;
import com.dangol.dangolsonnimbackend.store.repository.dsl.StoreQueryRepository;
import com.dangol.dangolsonnimbackend.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreQueryRepository storeQueryRepository;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository, StoreQueryRepository storeQueryRepository) {
        this.storeRepository = storeRepository;
        this.storeQueryRepository = storeQueryRepository;
    }

    /**
     * 신규 가게 정보를 통해 새로운 가게 정보를 생성한다.
     *
     * @param dto 가게 정보가 담긴 DTO 객체
     */
    @Override
    @Transactional
    public StoreResponseDTO signup(StoreSignupRequestDTO dto) {
        if(storeQueryRepository.existsByRegisterNumber(dto.getRegisterNumber()))
            throw new BadRequestException(ErrorCodeMessage.ALREADY_EXISTS_STORE_REGISTER_NUMBER);

        Store store = new Store(dto);
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

        if(!store.isPresent())
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

        if(!store.isPresent())
            throw new BadRequestException(ErrorCodeMessage.STORE_NOT_FOUND);

        return store
                .filter(Objects::nonNull)
                .filter(o -> o.getRegisterNumber() != null)
                .get().update(dto)
                .map(StoreResponseDTO::new)
                .orElseThrow(() -> new InternalServerException(ErrorCodeMessage.RESPONSE_CREATE_ERROR));
    }
}
