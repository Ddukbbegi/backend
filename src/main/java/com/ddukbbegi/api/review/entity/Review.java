package com.ddukbbegi.api.review.entity;

import com.ddukbbegi.api.common.entity.BaseUserEntity;
import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.review.dto.AnonymousStatus;
import com.ddukbbegi.api.review.dto.ReviewRequestDto;
import com.ddukbbegi.api.review.dto.ReviewUpdateRequestDto;
import com.ddukbbegi.api.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "reviews")
public class Review extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private Float rate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnonymousStatus anonymousStatus;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @Column(nullable = true)
    private String reply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public void updateReview(ReviewUpdateRequestDto requestDto){

        this.contents = requestDto.contents();
        this.rate = requestDto.rate();
        this.anonymousStatus = requestDto.anonymousStatus();
    }

    public void updateReply(String reply){
        this.reply = reply;
    }

    public void softDelete(){
        this.deletedAt = LocalDateTime.now();
    }

    public static Review from(User user, Order order , ReviewRequestDto dto) {
        return Review.builder()
                .order(order)
                .user(user)
                .contents(dto.contents())
                .rate(dto.rate())
                .anonymousStatus(dto.anonymousStatus())
                .build();
    }
    @PrePersist
    public void prePersist() {
        if (anonymousStatus == null) {
            anonymousStatus = AnonymousStatus.NON_ANONYMOUS;
        }
    }




}
