package com.dangol.dangolsonnimbackend.subscribe.service.impl;

import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.customer.domain.PurchasedSubscribe;
import com.dangol.dangolsonnimbackend.customer.repository.CustomerRepository;
import com.dangol.dangolsonnimbackend.customer.repository.PurchasedSubscribeRepository;
import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.NotFoundException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.repository.StoreRepository;
import com.dangol.dangolsonnimbackend.subscribe.domain.Benefit;
import com.dangol.dangolsonnimbackend.subscribe.domain.CountSubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.MonthlySubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.Subscribe;
import com.dangol.dangolsonnimbackend.subscribe.dto.PurchasedSubscribeResponseDTO;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeRequestDTO;
import com.dangol.dangolsonnimbackend.subscribe.dto.SubscribeResponseDTO;
import com.dangol.dangolsonnimbackend.subscribe.enumeration.SubscribeType;
import com.dangol.dangolsonnimbackend.subscribe.repository.SubscribeRepository;
import com.dangol.dangolsonnimbackend.subscribe.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscribeServiceImpl implements SubscribeService {

    private final SubscribeRepository<Subscribe> subscribeRepository;
    // 가게 관련 서비스 머지 시 QueryDsl Repository로 수정할 예정
    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final PurchasedSubscribeRepository purchasedSubscribeRepository;

    @Autowired
    public SubscribeServiceImpl(SubscribeRepository<Subscribe> subscribeRepository, StoreRepository storeRepository, CustomerRepository customerRepository, PurchasedSubscribeRepository purchasedSubscribeRepository){
        this.subscribeRepository = subscribeRepository;
        this.storeRepository = storeRepository;
        this.customerRepository = customerRepository;
        this.purchasedSubscribeRepository = purchasedSubscribeRepository;
    }

    @Transactional
    public SubscribeResponseDTO create(SubscribeRequestDTO dto) {
        // 가게 관련 머지 시 수정 예정
        Store store = storeRepository.findById(dto.getStoreId()).orElseThrow(
                () -> new BadRequestException(ErrorCodeMessage.STORE_NOT_FOUND)
        );

        Subscribe savedSubscribe = subscribeRepository.save(classify(dto, store));
        store.getSubscribeList().add(savedSubscribe);

        // 구독 관련 혜택 저장
        dto.getBenefits().forEach(benefitDto -> {
            Benefit benefit = new Benefit(benefitDto.getDescription());
            savedSubscribe.getBenefits().add(benefit);
        });

        return savedSubscribe.toResponseDTO();
    }

    public Subscribe classify(SubscribeRequestDTO dto, Store store){
        switch (dto.getType()) {
            case MONTHLY:
                return new MonthlySubscribe(dto, store);
            case COUNT:
                return new CountSubscribe(dto, store);
            default:
                throw new BadRequestException(ErrorCodeMessage.INVALID_SUBSCRIBE_TYPE);
        }
    }

    @Transactional(readOnly = true)
    public SubscribeResponseDTO getSubscribe(Long subscribeId) {
        return subscribeRepository.findById(subscribeId).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.SUBSCRIBE_NOT_FOUND)
        ).toResponseDTO();
    }

    @Transactional
    public void deleteSubscribe(Long subscribeId) {
        Subscribe subscribe = subscribeRepository.findById(subscribeId).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.SUBSCRIBE_NOT_FOUND));
        subscribe.getStore().getSubscribeList().remove(subscribe);
        subscribeRepository.deleteById(subscribeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchasedSubscribeResponseDTO> getSubscribeList(String id) {
        Customer customer = Optional.ofNullable(customerRepository.findById(id)).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.CUSTOMER_NOT_FOUND)
        );

        return purchasedSubscribeRepository.findAllByCustomer(customer).stream()
                .map(PurchasedSubscribe::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public PurchasedSubscribeResponseDTO useSubscribe(Long subscribeId) {

        PurchasedSubscribe purchasedSubscribe = purchasedSubscribeRepository.findById(subscribeId).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.SUBSCRIBE_NOT_FOUND)
        );

        if (purchasedSubscribe.getSubscribeType() == SubscribeType.COUNT && purchasedSubscribe.getRemainingCount() < 0){
            throw new BadRequestException(ErrorCodeMessage.NOT_REMAINING_COUNT);
        }
        if (purchasedSubscribe.getExpiredAt().isBefore(LocalDateTime.now())){
            throw new BadRequestException(ErrorCodeMessage.EXPIRED_SUBSCRIBE);
        }
        if (purchasedSubscribe.getSubscribeType() == SubscribeType.COUNT) {
            purchasedSubscribe.useSubscribe();
        }

        return purchasedSubscribe.toResponseDTO();
    }
}
