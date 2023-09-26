package com.ooadprojectserver.restaurantmanagement.model.user;

import com.ooadprojectserver.restaurantmanagement.model.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "owner")
public class Owner extends User {
    @Column(name = "branch", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String branch;

    @Column(name = "license_business", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String licenseBusiness;

    @Builder(builderMethodName = "ownerBuilder")
    public Owner(String username, String firstName, String lastName, String hashPassword, String phoneNumber, Integer role, Integer status, Address address,
                 LocalDateTime createAt, String branch, String licenseBusiness){
        super(username, firstName, lastName, hashPassword, phoneNumber, role, address, status, createAt);
        this.branch = branch;
        this.licenseBusiness = licenseBusiness;
    }

}