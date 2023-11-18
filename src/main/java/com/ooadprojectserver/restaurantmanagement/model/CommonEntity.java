package com.ooadprojectserver.restaurantmanagement.model;

import com.ooadprojectserver.restaurantmanagement.util.DateTimeUtils;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonEntity  {
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP default NOW()")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP default NOW()")
    private Timestamp updatedAt;

    @Column(name = "created_by", columnDefinition = "binary(16) DEFAULT 0")
    private UUID createdBy;

    @Column(name = "updated_by", columnDefinition = "binary(16) DEFAULT 0")
    private UUID updatedBy;

    public void setCommonCreate(UUID currentLoginId) {
        this.createdAt = DateTimeUtils.resultTimestamp();
        this.createdBy = currentLoginId;
        this.updatedAt = DateTimeUtils.resultTimestamp();
        this.updatedBy = currentLoginId;
    }


    public void setCommonUpdate(UUID currentLoginId) {
        this.updatedAt = DateTimeUtils.resultTimestamp();
        this.updatedBy = currentLoginId;
    }

}