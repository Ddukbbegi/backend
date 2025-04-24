package com.ddukbbegi.api.review.entity;

import com.ddukbbegi.api.common.entity.BaseUserEntity;
import com.ddukbbegi.api.review.dto.AnonymousStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private AnonymousStatus anonymousStatus = AnonymousStatus.NON_ANONYMOUS;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @Column(nullable = true)
    private String reply;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id")
//    private Order order;

    public void updateReview(){

    }

    public void updateReply(){

    }

    public void softDelete(){
        this.deletedAt = LocalDateTime.now();
    }

}
