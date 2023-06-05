package com.dangol.dangolsonnimbackend.customer.controller;

import com.amazonaws.util.IOUtils;
import com.dangol.dangolsonnimbackend.boss.dto.request.BossSignupRequestDTO;
import com.dangol.dangolsonnimbackend.boss.service.BossService;
import com.dangol.dangolsonnimbackend.config.jwt.TokenProvider;
import com.dangol.dangolsonnimbackend.customer.domain.Customer;
import com.dangol.dangolsonnimbackend.customer.domain.CustomerInfo;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerInfoRequestDTO;
import com.dangol.dangolsonnimbackend.customer.dto.CustomerUpdateRequestDTO;
import com.dangol.dangolsonnimbackend.customer.repository.CustomerRepository;
import com.dangol.dangolsonnimbackend.customer.service.CustomerService;
import com.dangol.dangolsonnimbackend.oauth.*;
import com.dangol.dangolsonnimbackend.store.dto.BusinessHourRequestDTO;
import com.dangol.dangolsonnimbackend.store.dto.StoreSignupRequestDTO;
import com.dangol.dangolsonnimbackend.store.enumeration.CategoryType;
import com.dangol.dangolsonnimbackend.store.service.StoreService;
import com.dangol.dangolsonnimbackend.subscribe.dto.BenefitDTO;
import com.mysql.cj.jdbc.CallableStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith(RestDocumentationExtension.class)
class CustomerControllerTest {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AuthTokenProvider tokenProvider;
    @Autowired
    private AppProperties appProperties;

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    private static final String BASE_URL = "/api/v1/customer";
    private static final String CUSTOMER_TEST_ID = "customer123";
    private static final String CUSTOMER_TEST_EMAIL = "rnjstmdals6@gmail.com";
    private static final String CUSTOMER_TEST_NAME = "홍길동";
    private static final String CUSTOMER_TEST_PHONE_NUMBER = "01012345678";
    private static final String CUSTOMER_TEST_BIRTH = "19990101";
    private static final String CUSTOMER_TEST_NICKNAME = "nickname";
    private static final String BOSS_TEST_NAME = "GilDong";
    private static final String BOSS_TEST_EMAIL = "test@example.com";
    private static final String BOSS_TEST_PASSWORD = "password";
    private static final String BOSS_TEST_PHONE_NUMBER = "01012345678";
    private static final Boolean BOSS_TEST_MARKETING_AGREEMENT = true;
    @Autowired
    private BossService bossService;
    @Autowired
    private StoreService storeService;
    private StoreSignupRequestDTO signupRequestDTO;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();

        signupRequestDTO = StoreSignupRequestDTO.builder()
                .name("단골손님" + new Random().nextInt())
                .phoneNumber("01012345678")
                .newAddress("서울특별시 서초구 단골로 130")
                .sido("서울특별시")
                .sigungu("서초구")
                .bname1("단골동")
                .bname2("")
                .detailedAddress("")
                .comments("단골손님 가게로 좋아요.")
                .registerNumber("1234567890")
                .registerName("단골손님")
                .categoryType(CategoryType.KOREAN)
                .tags(List.of("태그1", "태그2"))
                .businessHours(List.of(new BusinessHourRequestDTO("월~수", "10:00~12:00"),
                        new BusinessHourRequestDTO("토,일", "10:00~12:00")))
                .build();

        BossSignupRequestDTO bossSignupRequestDTO = new BossSignupRequestDTO();
        bossSignupRequestDTO.setName(BOSS_TEST_NAME);
        bossSignupRequestDTO.setEmail(BOSS_TEST_EMAIL);
        bossSignupRequestDTO.setPassword(BOSS_TEST_PASSWORD);
        bossSignupRequestDTO.setPhoneNumber(BOSS_TEST_PHONE_NUMBER);
        bossSignupRequestDTO.setMarketingAgreement(BOSS_TEST_MARKETING_AGREEMENT);
        bossService.signup(bossSignupRequestDTO);

    }

    @Test
    void givenTokenAndDto_whenAddInfo_thenCreateNewCustomerInfo() throws Exception {
        // given
        InputStream imageInputStream = getClass().getResourceAsStream("/example-image.png");
        byte[] imageBytes = IOUtils.toByteArray(imageInputStream);


        // Info 관련 RequestDTO
        CustomerInfoRequestDTO dto = new CustomerInfoRequestDTO();
        dto.setNickname(CUSTOMER_TEST_NICKNAME);
        dto.setPhoneNumber(CUSTOMER_TEST_PHONE_NUMBER);
        dto.setBirth(CUSTOMER_TEST_BIRTH);

        // 고객 생성
        Customer customer = new Customer(CUSTOMER_TEST_ID, CUSTOMER_TEST_NAME, CUSTOMER_TEST_EMAIL, ProviderType.LOCAL, RoleType.USER, new CustomerInfo());
        customerRepository.save(customer);

        // MockMultipartFile 객체 생성
        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile", "example-image.png", "image/png", imageBytes);

        Date now = new Date();
        AuthToken authToken = tokenProvider.createAuthToken(CUSTOMER_TEST_ID, new Date(now.getTime() + appProperties.getAuth().getTokenExpiry()));
        String accessToken = authToken.getToken();
        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                CUSTOMER_TEST_ID, null, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // when
        mockMvc.perform(MockMvcRequestBuilders.multipart(BASE_URL + "/info")
                        .file(multipartFile)
                        .param("nickname", dto.getNickname())
                        .param("phoneNumber", dto.getPhoneNumber())
                        .param("birth", dto.getBirth())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(CUSTOMER_TEST_NAME))
                .andExpect(jsonPath("$.email").value(CUSTOMER_TEST_EMAIL))
                .andExpect(jsonPath("$.providerType").value(ProviderType.LOCAL.toString()))
                .andExpect(jsonPath("$.roleType").value(RoleType.USER.toString()))
                .andExpect(jsonPath("$.nickname").value(CUSTOMER_TEST_NICKNAME))
                .andExpect(jsonPath("$.profileImageUrl").exists())
                .andExpect(jsonPath("$.phoneNumber").value(CUSTOMER_TEST_PHONE_NUMBER))
                .andExpect(jsonPath("$.birth").value(CUSTOMER_TEST_BIRTH))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.modifiedAt").exists())
                .andDo(document("customer/add-info",
                        requestHeaders(headerWithName("Authorization").description("Access 토큰 정보")),
                        requestParts(
                                partWithName("multipartFile").description("메뉴 이미지 파일")
                        ),
                        requestParameters(
                                parameterWithName("nickname").description("고객 닉네임"),
                                parameterWithName("phoneNumber").description("고객 휴대폰번호"),
                                parameterWithName("birth").description("고객 생년월일")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("고객 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("고객 이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("고객 이메일 주소"),
                                fieldWithPath("providerType").type(JsonFieldType.STRING).description("소셜 로그인 제공자"),
                                fieldWithPath("roleType").type(JsonFieldType.STRING).description("고객 역할"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("고객 닉네임"),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("고객 프로필 이미지 URL"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("고객 휴대폰번호"),
                                fieldWithPath("birth").type(JsonFieldType.STRING).description("고객 생년월일"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("고객 생성 일자"),
                                fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("고객 수정 일자")
                        )
                ));
    }

    @Test
    void givenCustomerId_whenGetInfo_thenReturnCustomerInfo() throws Exception {
        // given
        Customer customer = new Customer(CUSTOMER_TEST_ID, CUSTOMER_TEST_NAME, CUSTOMER_TEST_EMAIL, ProviderType.LOCAL, RoleType.USER, new CustomerInfo());
        customerRepository.save(customer);

        Date now = new Date();
        AuthToken authToken = tokenProvider.createAuthToken(CUSTOMER_TEST_ID, new Date(now.getTime() + appProperties.getAuth().getTokenExpiry()));
        String accessToken = authToken.getToken();
        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                CUSTOMER_TEST_ID, null, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomerInfoRequestDTO dto = new CustomerInfoRequestDTO();
        dto.setNickname(CUSTOMER_TEST_NICKNAME);
        dto.setPhoneNumber(CUSTOMER_TEST_PHONE_NUMBER);
        dto.setBirth(CUSTOMER_TEST_BIRTH);

        customerService.addInfo(CUSTOMER_TEST_ID, dto);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(CUSTOMER_TEST_NAME))
                .andExpect(jsonPath("$.email").value(CUSTOMER_TEST_EMAIL))
                .andExpect(jsonPath("$.providerType").value(ProviderType.LOCAL.toString()))
                .andExpect(jsonPath("$.roleType").value(RoleType.USER.toString()))
                .andExpect(jsonPath("$.nickname").value(CUSTOMER_TEST_NICKNAME))
                .andExpect(jsonPath("$.profileImageUrl").exists())
                .andExpect(jsonPath("$.phoneNumber").value(CUSTOMER_TEST_PHONE_NUMBER))
                .andExpect(jsonPath("$.birth").value(CUSTOMER_TEST_BIRTH))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.modifiedAt").exists())
                .andDo(document("customer/get-info",
                        requestHeaders(headerWithName("Authorization").description("Access 토큰 정보")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("고객 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("고객 이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("고객 이메일 주소"),
                                fieldWithPath("providerType").type(JsonFieldType.STRING).description("소셜 로그인 제공자"),
                                fieldWithPath("roleType").type(JsonFieldType.STRING).description("고객 역할"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("고객 닉네임"),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("고객 프로필 이미지 URL"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("고객 휴대폰번호"),
                                fieldWithPath("birth").type(JsonFieldType.STRING).description("고객 생년월일"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("고객 생성 일자"),
                                fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("고객 수정 일자")
                        )
                ));
    }

    @Test
    void givenNonDuplicateNickname_whenIsNicknameDuplicate_thenReturnsHttpStatusOk() throws Exception {
        // given
        String nickname = "not_duplicate_nickname";
        Customer customer = new Customer(CUSTOMER_TEST_ID, CUSTOMER_TEST_NAME, CUSTOMER_TEST_EMAIL, ProviderType.LOCAL, RoleType.USER, new CustomerInfo());
        customerRepository.save(customer);

        CustomerInfoRequestDTO dto = new CustomerInfoRequestDTO();
        dto.setNickname(CUSTOMER_TEST_NICKNAME);
        dto.setPhoneNumber(CUSTOMER_TEST_PHONE_NUMBER);
        dto.setBirth(CUSTOMER_TEST_BIRTH);

        customerService.addInfo(CUSTOMER_TEST_ID, dto);

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + "/validate/{nickname}", nickname))
                // then
                .andExpect(status().isOk())
                .andDo(document("customer/is-nickname-duplicate",
                        pathParameters(
                                parameterWithName("nickname").description("닉네임")
                        )
                ));
    }

    @Test
    void givenCustomerIdAndStoreId_whenLike_thenToggleLikeStatus() throws Exception {
        Customer customer = new Customer(CUSTOMER_TEST_ID, CUSTOMER_TEST_NAME, CUSTOMER_TEST_EMAIL, ProviderType.LOCAL, RoleType.USER, new CustomerInfo());
        customerRepository.save(customer);

        Date now = new Date();
        AuthToken authToken = tokenProvider.createAuthToken(CUSTOMER_TEST_ID, new Date(now.getTime() + appProperties.getAuth().getTokenExpiry()));
        String accessToken = authToken.getToken();
        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                CUSTOMER_TEST_ID, null, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long storeId = storeService.create(signupRequestDTO, BOSS_TEST_EMAIL).getId();

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + "/like/{storeId}", storeId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andDo(document("customer/like",
                        requestHeaders(headerWithName("Authorization").description("Access 토큰 정보")),
                        pathParameters(
                                parameterWithName("storeId").description("상점 ID")
                        )
                ));
    }

    @Test
    void givenCustomerIdAndStoreId_whenIsLike_thenReturnLikeStatus() throws Exception {
        Customer customer = new Customer(CUSTOMER_TEST_ID, CUSTOMER_TEST_NAME, CUSTOMER_TEST_EMAIL, ProviderType.LOCAL, RoleType.USER, new CustomerInfo());
        customerRepository.save(customer);

        Date now = new Date();
        AuthToken authToken = tokenProvider.createAuthToken(CUSTOMER_TEST_ID, new Date(now.getTime() + appProperties.getAuth().getTokenExpiry()));
        String accessToken = authToken.getToken();
        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                CUSTOMER_TEST_ID, null, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long storeId = storeService.create(signupRequestDTO, BOSS_TEST_EMAIL).getId();

        // when
        mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + "/isLike/{storeId}", storeId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isLike").value(Boolean.FALSE))
                .andDo(document("customer/is-like",
                        requestHeaders(headerWithName("Authorization").description("Access 토큰 정보")),
                        pathParameters(
                                parameterWithName("storeId").description("상점 ID")
                        ),
                        responseFields(
                                fieldWithPath("isLike").type(JsonFieldType.BOOLEAN).description("좋아요 상태")
                        )
                ));
    }

    @Test
    void givenAuthenticatedUser_whenWithdraw_thenReturnNoContent() throws Exception {
        // given
        Customer customer = new Customer(CUSTOMER_TEST_ID, CUSTOMER_TEST_NAME, CUSTOMER_TEST_EMAIL, ProviderType.LOCAL, RoleType.USER, new CustomerInfo());
        customerRepository.save(customer);

        Date now = new Date();
        AuthToken authToken = tokenProvider.createAuthToken(CUSTOMER_TEST_ID, new Date(now.getTime() + appProperties.getAuth().getTokenExpiry()));
        String accessToken = authToken.getToken();
        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                CUSTOMER_TEST_ID, null, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // when
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent())
                .andDo(document("customer/withdraw",
                        requestHeaders(headerWithName("Authorization").description("Access 토큰 정보"))
                ));
    }

    @Test
    void givenAuthenticatedUserAndValidRequest_whenUpdate_thenReturnCustomerResponse() throws Exception {
        // given
        InputStream imageInputStream = getClass().getResourceAsStream("/example-image.png");
        byte[] imageBytes = IOUtils.toByteArray(imageInputStream);

        // Info 관련 RequestDTO
        CustomerInfoRequestDTO dto = new CustomerInfoRequestDTO();
        dto.setNickname(CUSTOMER_TEST_NICKNAME);
        dto.setPhoneNumber(CUSTOMER_TEST_PHONE_NUMBER);
        dto.setBirth(CUSTOMER_TEST_BIRTH);

        // 고객 생성
        Customer customer = new Customer(CUSTOMER_TEST_ID, CUSTOMER_TEST_NAME, CUSTOMER_TEST_EMAIL, ProviderType.LOCAL, RoleType.USER, new CustomerInfo());
        customerRepository.save(customer);

        // MockMultipartFile 객체 생성
        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile", "example-image.png", "image/png", imageBytes);

        Date now = new Date();
        AuthToken authToken = tokenProvider.createAuthToken(CUSTOMER_TEST_ID, new Date(now.getTime() + appProperties.getAuth().getTokenExpiry()));
        String accessToken = authToken.getToken();
        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                CUSTOMER_TEST_ID, null, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // when
        mockMvc.perform(RestDocumentationRequestBuilders.fileUpload(BASE_URL + "/info/update")
                        .file(multipartFile)
                        .param("nickname", dto.getNickname())
                        .param("phoneNumber", dto.getPhoneNumber())
                        .param("birth", dto.getBirth())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(CUSTOMER_TEST_NAME))
                .andExpect(jsonPath("$.email").value(CUSTOMER_TEST_EMAIL))
                .andExpect(jsonPath("$.providerType").value(ProviderType.LOCAL.toString()))
                .andExpect(jsonPath("$.roleType").value(RoleType.USER.toString()))
                .andExpect(jsonPath("$.nickname").value(CUSTOMER_TEST_NICKNAME))
                .andExpect(jsonPath("$.profileImageUrl").exists())
                .andExpect(jsonPath("$.phoneNumber").value(CUSTOMER_TEST_PHONE_NUMBER))
                .andExpect(jsonPath("$.birth").value(CUSTOMER_TEST_BIRTH))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.modifiedAt").exists())
                .andDo(document("customer/update-info",
                        requestHeaders(headerWithName("Authorization").description("Access 토큰 정보")),
                        requestParts(
                                partWithName("multipartFile").description("메뉴 이미지 파일")
                        ),
                        requestParameters(
                                parameterWithName("nickname").description("고객 닉네임"),
                                parameterWithName("phoneNumber").description("고객 휴대폰번호"),
                                parameterWithName("birth").description("고객 생년월일")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("고객 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("고객 이름"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("고객 이메일 주소"),
                                fieldWithPath("providerType").type(JsonFieldType.STRING).description("소셜 로그인 제공자"),
                                fieldWithPath("roleType").type(JsonFieldType.STRING).description("고객 역할"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("고객 닉네임"),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("고객 프로필 이미지 URL"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("고객 휴대폰번호"),
                                fieldWithPath("birth").type(JsonFieldType.STRING).description("고객 생년월일"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("고객 생성 일자"),
                                fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("고객 수정 일자")
                        )
                ));
    }
}