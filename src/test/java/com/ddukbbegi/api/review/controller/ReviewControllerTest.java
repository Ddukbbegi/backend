package com.ddukbbegi.api.review.controller;

import com.ddukbbegi.api.common.dto.PageResponseDto;
import com.ddukbbegi.api.review.dto.*;
import com.ddukbbegi.api.review.service.ReviewService;
import com.ddukbbegi.api.user.enums.UserRole;
import com.ddukbbegi.common.auth.CustomUserDetails;
import com.ddukbbegi.common.auth.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwtUtil jwtUtil;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private RedisTemplate<String, Object> redisTemplate;
    @MockitoBean
    private ReviewService reviewService;
    @InjectMocks
    private ReviewController reviewController;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setup() {
        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + UserRole.USER.name()));
        customUserDetails = new CustomUserDetails(1L, authorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities())
        );
        SecurityContextHolder.setContext(context);
    }

    @Test
    void saveReview() throws Exception {
        //given
        ReviewRequestDto requestDto =
                new ReviewRequestDto(
                        1L,
                        "맛있어요",
                        4.5f,
                        AnonymousStatus.ANONYMOUS);
        ReviewResponseDto responseDto =
                new ReviewResponseDto(
                        1L,
                        requestDto.orderId(),
                        requestDto.contents(),
                        requestDto.rate(),
                        "익명",
                        null,
                        0L);
        given(reviewService.saveReview(customUserDetails.getUserId(), requestDto))
                .willReturn(responseDto);
        //when then
        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(user(customUserDetails))) // 인증된 유저로 설정
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.reviewId").value(responseDto.reviewId()))
                .andExpect(jsonPath("$.data.contents").value("맛있어요"))
                .andExpect(jsonPath("$.data.rate").value(4.5))
                .andExpect(jsonPath("$.data.writer").value("익명"))
                .andExpect(jsonPath("$.data.likeCount").value(0));

    }

    @Test
    void findAllMyReviews() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 20);
        // Mock 데이터 생성
        ReviewResponseDto reviewResponseDto1 = new ReviewResponseDto(1L, 1L, "맛있어요", 4.5f, "익명", null, 0L);
        ReviewResponseDto reviewResponseDto2 = new ReviewResponseDto(2L, 2L, "별로에요", 2.0f, "익명", null, 0L);
        // Page<ReviewResponseDto> 객체 만들기 (첫 번째 페이지, 10개 항목)
        Page<ReviewResponseDto> responseDtoPage = new PageImpl<>(List.of(reviewResponseDto1, reviewResponseDto2), pageable, 2);

        // service 메서드가 위 데이터를 반환하도록 설정
        given(reviewService.findAllMyReviews(customUserDetails.getUserId(), pageable))
                .willReturn(responseDtoPage);
        //when //then
        mockMvc.perform(get("/api/users/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(customUserDetails))) // 인증된 유저로 설정
                .andExpect(status().isOk());
    }

    @Test
    void findAllStoreReviews() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 20);
        // Mock 데이터 생성
        ReviewResponseDto reviewResponseDto1 = new ReviewResponseDto(1L, 1L, "맛있어요", 4.5f, "익명", null, 0L);
        ReviewResponseDto reviewResponseDto2 = new ReviewResponseDto(2L, 2L, "별로에요", 2.0f, "익명", null, 0L);
        // Page<ReviewResponseDto> 객체 만들기 (첫 번째 페이지, 10개 항목)
        Page<ReviewResponseDto> responseDtoPage = new PageImpl<>(List.of(reviewResponseDto1, reviewResponseDto2), pageable, 2);

        // service 메서드가 위 데이터를 반환하도록 설정
        given(reviewService.findAllStoreReviews(1L, pageable))
                .willReturn(responseDtoPage);
        //when //then
        mockMvc.perform(get("/api/stores/{storeId}/reviews", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(customUserDetails))) // 인증된 유저로 설정
                .andExpect(status().isOk());
    }

    @Test
    void updateReview() throws Exception {
        //given
        Long reviewId = 1L;
        ReviewUpdateRequestDto requestDto =
                new ReviewUpdateRequestDto(
                        "맛없어요",
                        4.5f,
                        AnonymousStatus.ANONYMOUS);
        ReviewResponseDto responseDto =
                new ReviewResponseDto(
                        1L,
                        1L,
                        requestDto.contents(),
                        requestDto.rate(),
                        "익명",
                        null,
                        0L);
        given(reviewService.updateReview(customUserDetails.getUserId(), reviewId, requestDto))
                .willReturn(responseDto);
        //then
        mockMvc.perform(patch("/api/reviews/{reviewId}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(customUserDetails))
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(jsonPath("$.data.reviewId").value(responseDto.reviewId()))
                .andExpect(jsonPath("$.data.contents").value(requestDto.contents()))
                .andExpect(jsonPath("$.data.rate").value(requestDto.rate()))
                .andExpect(jsonPath("$.data.writer").value(responseDto.writer()))
                .andExpect(status().isOk());

    }

    @Test
    void deleteReview() throws Exception {
        Long reviewId = 1L;

        mockMvc.perform(delete("/api/reviews/{reviewId}", reviewId)
                        .with(user(customUserDetails)))
                .andExpect(status().isOk());
    }

    @Test
    void saveReviewReply() throws Exception{
        ReviewOwnerRequestDto requestDto =
                new ReviewOwnerRequestDto("감사합니다!");
        ReviewResponseDto responseDto =
                new ReviewResponseDto(
                        1L,
                        1L,
                        "맛없어요",
                        4.5f,
                        "익명",
                        requestDto.contents(),
                        0L);
        Long reviewId = 1L;
        given(reviewService.saveReviewReply(customUserDetails.getUserId(),reviewId, requestDto))
                .willReturn(responseDto);

        mockMvc.perform(post("/api/owners/reviews/{reviewId}/reply", reviewId)
                .with(user(customUserDetails))
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.reviewId").value(reviewId))
                .andExpect(jsonPath("$.data.reply").value(requestDto.contents()))
                .andExpect(status().isOk());
    }

    @Test
    void updateReviewReply() throws Exception{

        ReviewOwnerRequestDto requestDto =
                new ReviewOwnerRequestDto("감사합니다!");
        ReviewResponseDto responseDto =
                new ReviewResponseDto(
                        1L,
                        1L,
                        "맛없어요",
                        4.5f,
                        "익명",
                        requestDto.contents(),
                        0L);
        Long reviewId = 1L;
        given(reviewService.updateReviewReply(customUserDetails.getUserId(),reviewId, requestDto))
                .willReturn(responseDto);

        mockMvc.perform(patch("/api/owners/reviews/{reviewId}/reply-update", reviewId)
                        .with(user(customUserDetails))
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.reviewId").value(reviewId))
                .andExpect(jsonPath("$.data.reply").value(requestDto.contents()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReviewReply() throws Exception{
        Long reviewId= 1L;
        //given(reviewService.deleteReviewReply(customUserDetails.getUserId(), reviewId))
        mockMvc.perform(delete("/api/owners/reviews/{reviewId}/reply-delete", reviewId)
                        .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void saveLike() throws Exception{
        Long reviewId = 1L;

        mockMvc.perform(post("/api/reviews/{reviewId}/likes", reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(customUserDetails)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteLike() throws Exception{
        Long reviewId = 1L;

        mockMvc.perform(delete("/api/reviews/{reviewId}/likes", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(customUserDetails)))
                .andExpect(status().isOk());
    }

    @Test
    void getStoreRate() throws Exception {
        Long storeId = 1L;

        RatingPerStarResponseDto responseDto = new RatingPerStarResponseDto(
                storeId, 4.0f, List.of(0L, 1L,2L,3L,4L,5L)
        );
        given(reviewService.getStoreRating(storeId))
                .willReturn(responseDto);

// when // then
        mockMvc.perform(get("/api/stores/{storeId}/rates", storeId)
                        .with(user(customUserDetails)))
                .andExpect(status().isOk()) // 👈 응답 200 OK 체크
                .andExpect(jsonPath("$.data.storeId").value(storeId))
                .andExpect(jsonPath("$.data.rate").value(4.0))
                .andExpect(jsonPath("$.data.ratingCountPerStar[0]").value(0))
                .andExpect(jsonPath("$.data.ratingCountPerStar[1]").value(1))
                .andExpect(jsonPath("$.data.ratingCountPerStar[2]").value(2))
                .andExpect(jsonPath("$.data.ratingCountPerStar[3]").value(3))
                .andExpect(jsonPath("$.data.ratingCountPerStar[4]").value(4))
                .andExpect(jsonPath("$.data.ratingCountPerStar[5]").value(5));
    }
}