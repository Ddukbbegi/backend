package com.ddukbbegi.api.point.service;

import com.ddukbbegi.api.order.entity.Order;
import com.ddukbbegi.api.point.dto.PointResponseDto;
import com.ddukbbegi.api.point.entity.Point;
import com.ddukbbegi.api.point.entity.PointUsage;
import com.ddukbbegi.api.point.repository.PointRepository;
import com.ddukbbegi.api.point.repository.PointUsageRepository;
import com.ddukbbegi.api.user.entity.User;
import com.ddukbbegi.api.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final static double POINT_RATE = 0.03;
    private final PointUsageRepository pointUsageRepository;

    @Transactional(readOnly = true)
    public Long getMyPoint(Long userId){

        return pointRepository.findPointByUser_Id(userId).getMyPoint();
    }

    @Transactional
    public PointResponseDto savePointWallet(Long userId){
        User user = userRepository.findByIdOrElseThrow(userId);
        Point point = Point.builder()
                .user(user)
                .myPoint(0L)
                .build();
        Point savaedPoint = pointRepository.save(point);
        return PointResponseDto.builder()
                .myPoint(savaedPoint.getMyPoint())
                .userId(savaedPoint.getUser().getId())
                .build();
    }

    @Transactional
    public void addPoint(User user, Order order, Long price){
        PointUsage pointUsage = PointUsage
                .builder()
                .user(user)
                .order(order)
                .usageHistory((long) (price * POINT_RATE))
                .build();
        pointUsageRepository.save(pointUsage);
        Point myPoint = pointRepository.findPointByUser(user);
        Long sumUsagePoint = pointUsageRepository.sumUsageHistoryByUserId(user.getId());
        myPoint.refreshPoint(sumUsagePoint);
    }

    @Transactional
    public Long usagePoint(User user, Order order){
        Point myPoint = pointRepository.findPointByUser(user);
        Long usagePoint = myPoint.getMyPoint();
        myPoint.usagePoint();
        PointUsage pointUsage = PointUsage
                .builder()
                .user(user)
                .order(order)
                .usageHistory(usagePoint * -1)
                .build();
        pointUsageRepository.save(pointUsage);
        return usagePoint;
    }



}
