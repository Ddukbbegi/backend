package com.ddukbbegi.api.review.repository;
import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ReviewLikeRepositoryTest {


    @Autowired
    private ReviewLikeRepository reviewLikeRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    void test(){

        reviewLikeRepository.countLikesByUserId(1L);
        reviewLikeRepository.countLikesByStoreId(1L);
    }
}