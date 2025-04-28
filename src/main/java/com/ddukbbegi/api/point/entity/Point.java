package com.ddukbbegi.api.point.entity;

import com.ddukbbegi.api.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "points")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Long myPoint;

    public void usagePoint(){
        this.myPoint = 0L;
    }
    public void addPoint(Long point){
        this.myPoint += point;
    }

}
