package com.ddukbbegi.api.point.repository;

import com.ddukbbegi.api.common.repository.BaseRepository;
import com.ddukbbegi.api.point.entity.Point;
import com.ddukbbegi.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends BaseRepository<Point, Long> {


    Point findPointByUser(User user);

    Point findPointByUser_Id(Long userId);
}
