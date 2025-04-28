package com.ddukbbegi.api.point.component;

import com.ddukbbegi.api.point.dto.PointUsageEvent;
import com.ddukbbegi.api.point.dto.PointUsageResponsDto;
import com.ddukbbegi.api.point.entity.Point;
import com.ddukbbegi.api.point.entity.PointUsage;
import com.ddukbbegi.api.point.repository.PointRepository;
import com.ddukbbegi.api.point.repository.PointUsageRepository;
import com.ddukbbegi.api.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class PointEventListener {

    private final PointRepository pointRepository;
    private final PointUsageRepository pointUsageRepository;
    private final static double POINT_RATE = 0.03;

    @EventListener
    @Transactional
    public void addPointUsageHistory(PointUsageEvent event){
        Long point = Math.round(event.getPrice()*POINT_RATE);
        PointUsage pointUsage = PointUsage.builder()
                .user(event.getUser())
                .order(event.getOrder())
                .usage(point)
                .build();
        Point findMyPoint = pointRepository.findPointByUser(event.getUser());
        findMyPoint.addPoint(point);
        pointUsageRepository.save(pointUsage);
    }

    @Transactional
    @EventListener
    public void usagePoint(PointUsageEvent event){
        Point point = pointRepository.findPointByUser(event.getUser());
        PointUsage pointUsage = PointUsage.builder()
                                    .usage(point.getMyPoint() * -1)
                                    .user(event.getUser())
                                    .order(event.getOrder())
                                    .build();
        pointUsageRepository.save(pointUsage);
        point.usagePoint();
    }

    public Long getMyPoint(User user){
        return pointRepository.findPointByUser(user).getMyPoint();
    }
    public Page<PointUsageResponsDto> getMyPointUsages(Long userId, Pageable pageable){
        return pointUsageRepository.findAllByUser_Id(userId, pageable).map(PointUsageResponsDto::from);
    }

}
