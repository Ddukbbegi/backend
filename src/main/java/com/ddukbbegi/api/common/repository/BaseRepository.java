package com.ddukbbegi.api.common.repository;

import com.ddukbbegi.api.menu.entity.Menu;
import com.ddukbbegi.common.component.ResultCode;
import com.ddukbbegi.common.exception.BusinessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Collection;
import java.util.List;

/**
 * 여러 도메인의 Repository에서 공통적으로 사용하는 기능을 default 메서드로 구현한 인터페이스
 * @param <T>   Entity 타입
 * @param <ID>  Entity ID 타입
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

    default T findByIdOrElseThrow(ID id) {
        return findById(id).orElseThrow(() ->
                new BusinessException(ResultCode.NOT_FOUND, "해당 Entity를 찾을 수 없습니다. id = " + id)
        );
    }

    default List<T> findAllByIdOrElseThrow(Collection<ID> ids) {
        List<T> results = findAllById(ids);
        if (results.size() != ids.size()) {
            throw new BusinessException(ResultCode.NOT_FOUND, "요청한 일부 엔티티를 찾을 수 없습니다. 요청 ID 수: " + ids.size() + ", 조회된 수: " + results.size());
        }
        return results;
    }

}
