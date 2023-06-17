package com.dangol.dangolsonnimbackend.customer.service.impl;

import com.dangol.dangolsonnimbackend.boss.dto.request.IsValidAccessTokenRequestDTO;
import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.customer.domain.Like;
import com.dangol.dangolsonnimbackend.customer.domain.PurchasedSubscribe;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerInfoRequestDTO;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerResponseDTO;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerUpdateRequestDTO;
import com.dangol.dangolsonnimbackend.customer.dto.PurchaseSubscribeRequestDTO;
import com.dangol.dangolsonnimbackend.customer.repository.CustomerInfoRepository;
import com.dangol.dangolsonnimbackend.customer.repository.CustomerRepository;
import com.dangol.dangolsonnimbackend.customer.repository.LikeRepository;
import com.dangol.dangolsonnimbackend.customer.repository.PurchasedSubscribeRepository;
import com.dangol.dangolsonnimbackend.customer.service.CustomerService;
import com.dangol.dangolsonnimbackend.errors.BadRequestException;
import com.dangol.dangolsonnimbackend.errors.NotFoundException;
import com.dangol.dangolsonnimbackend.errors.enumeration.ErrorCodeMessage;
import com.dangol.dangolsonnimbackend.file.service.FileService;
import com.dangol.dangolsonnimbackend.oauth.AuthToken;
import com.dangol.dangolsonnimbackend.oauth.AuthTokenProvider;
import com.dangol.dangolsonnimbackend.store.domain.Store;
import com.dangol.dangolsonnimbackend.store.dto.StoreResponseDTO;
import com.dangol.dangolsonnimbackend.store.repository.StoreRepository;
import com.dangol.dangolsonnimbackend.subscribe.domain.CountSubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.MonthlySubscribe;
import com.dangol.dangolsonnimbackend.subscribe.domain.Subscribe;
import com.dangol.dangolsonnimbackend.subscribe.enumeration.SubscribeType;
import com.dangol.dangolsonnimbackend.subscribe.repository.CountSubscribeRepository;
import com.dangol.dangolsonnimbackend.subscribe.repository.MonthlySubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final LikeRepository likeRepository;
    private final CustomerInfoRepository customerInfoRepository;
    private final FileService fileService;
    private final AuthTokenProvider authTokenProvider;
    private final CountSubscribeRepository countSubscribeRepository;
    private final MonthlySubscribeRepository monthlySubscribeRepository;
    private final PurchasedSubscribeRepository purchasedSubscribeRepository;

    @Override
    public CustomerResponseDTO addInfo(String id, CustomerInfoRequestDTO dto) {
        Customer customer = Optional.ofNullable(customerRepository.findById(id)).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.CUSTOMER_NOT_FOUND)
        );
        customer.setRoleTypeIsUser();
        String s3FileUrl = uploadFileIfPresent(dto.getMultipartFile());
        customer.getCustomerInfo().setCustomerInfo(dto, s3FileUrl);
        return new CustomerResponseDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getInfo(String id){
        Customer customer = Optional.ofNullable(customerRepository.findById(id)).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.CUSTOMER_NOT_FOUND)
        );
        return new CustomerResponseDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public void existsByNickname(String nickname) {
        if(customerInfoRepository.existsByNickname(nickname)){
            throw new BadRequestException(ErrorCodeMessage.ALREADY_EXISTS_NICKNAME);
        }
    }

    @Override
    public void like(String id, Long storeId) {
        Customer customer = Optional.ofNullable(customerRepository.findById(id)).orElseThrow(
                ()-> new NotFoundException(ErrorCodeMessage.CUSTOMER_NOT_FOUND));
        Store store = storeRepository.findById(storeId).orElseThrow(
                ()-> new BadRequestException(ErrorCodeMessage.STORE_NOT_FOUND));

        Optional<Like> likeOptional = Optional.ofNullable(likeRepository.findByCustomerAndStore(customer, store));

        if (likeOptional.isPresent()) {
            store.decreaseLikeNum();
            customer.getLikeList().remove(likeOptional.get());
            likeRepository.delete(likeOptional.get());
        } else {
            store.increaseLikeNum();
            customer.getLikeList().add(likeRepository.save(new Like(customer, store)));
        }
    }

    @Override
    public Boolean isLike(String id, Long storeId) {
        Customer customer = Optional.ofNullable(customerRepository.findById(id)).orElseThrow(
                ()-> new NotFoundException(ErrorCodeMessage.CUSTOMER_NOT_FOUND));
        Store store = storeRepository.findById(storeId).orElseThrow(
                ()-> new BadRequestException(ErrorCodeMessage.STORE_NOT_FOUND));

        Optional<Like> likeOptional = Optional.ofNullable(likeRepository.findByCustomerAndStore(customer, store));
        return likeOptional.isPresent();
    }

    @Override
    public void withdraw(String id) {
        Customer customer = Optional.ofNullable(customerRepository.findById(id)).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.CUSTOMER_NOT_FOUND)
        );
        customerRepository.delete(customer);
    }

    @Override
    public CustomerResponseDTO update(String id, CustomerUpdateRequestDTO reqeustDTO) {
        Customer customer = Optional.ofNullable(customerRepository.findById(id)).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.CUSTOMER_NOT_FOUND)
        );

        String s3FileUrl = uploadFileIfPresent(reqeustDTO.getMultipartFile());
        customer.getCustomerInfo().update(reqeustDTO, s3FileUrl);
        return new CustomerResponseDTO(customer);
    }

    @Override
    public void accessTokenValidate(IsValidAccessTokenRequestDTO dto) {
        AuthToken authToken = authTokenProvider.convertAuthToken(dto.getAccessToken());

        if (!authToken.validate()){
            throw new BadRequestException(ErrorCodeMessage.INVALID_TOKEN);
        }
    }

    @Override
    public void purchaseSubscribe(String id, PurchaseSubscribeRequestDTO dto) {
        Customer customer = Optional.ofNullable(customerRepository.findById(id)).orElseThrow(
                () -> new NotFoundException(ErrorCodeMessage.CUSTOMER_NOT_FOUND)
        );

        PurchasedSubscribe purchasedSubscribe;

        if (dto.getSubscribeType().equals(SubscribeType.COUNT)){
            CountSubscribe countSubscribe = countSubscribeRepository.findById(dto.getSubscribeId()).orElseThrow(
                    () -> new NotFoundException(ErrorCodeMessage.SUBSCRIBE_NOT_FOUND)
            );
            purchasedSubscribe = new PurchasedSubscribe(countSubscribe, customer, dto);
        }
        else {
            MonthlySubscribe monthlySubscribe = monthlySubscribeRepository.findById(dto.getSubscribeId()).orElseThrow(
                    () -> new NotFoundException(ErrorCodeMessage.SUBSCRIBE_NOT_FOUND)
            );
            purchasedSubscribe = new PurchasedSubscribe(monthlySubscribe, customer, dto);
        }

        purchasedSubscribeRepository.save(purchasedSubscribe);
    }

    private String uploadFileIfPresent(MultipartFile file) {
        if (file != null) {
            return fileService.upload(file);
        }
        return "";
    }
}
