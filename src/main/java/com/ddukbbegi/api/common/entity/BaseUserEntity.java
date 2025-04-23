package com.ddukbbegi.api.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
abstract public class BaseUserEntity extends BaseTimeEntity {

    // 생성자 ID
    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;

    // 수정자 ID
    @LastModifiedBy
    private Long modifiedBy;

}
