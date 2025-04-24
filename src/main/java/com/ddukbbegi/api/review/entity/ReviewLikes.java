package com.ddukbbegi.api.review.entity;

import com.ddukbbegi.api.common.entity.BaseUserEntity;
import com.ddukbbegi.api.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@Table(
        name = "review_like",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"review_id", "user_id"})
        }
)
public class ReviewLikes extends BaseUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Reviews reviews;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static ReviewLikes from(Reviews reviews, User user) {
        return ReviewLikes.builder()
                .reviews(reviews)
                .user(user)
                .build();
    }



}
