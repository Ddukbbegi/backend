package com.ddukbbegi.api.point.service;

import com.ddukbbegi.api.point.repository.PointRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    @Transactional(readOnly = true)
    public Long getMyPoint(Long userId){

        return pointRepository.findPointByUser_Id(userId).getMyPoint();
    }



}
