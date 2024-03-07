package com.ky.commons.model;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@SuperBuilder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Auditable<T> {

    @CreatedBy
    protected T createdBy;
    @CreatedDate
    protected LocalDateTime createdDate;
    @LastModifiedBy
    protected T updatedBy;
    @LastModifiedDate
    protected LocalDateTime updatedDate;
}
