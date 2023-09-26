package com.ooadprojectserver.restaurantmanagement.model.user;

import com.ooadprojectserver.restaurantmanagement.model.Address;
import com.ooadprojectserver.restaurantmanagement.util.Constant;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Inheritance
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "manager")
public class Manager extends User {
    @Column(name = "certification_management", nullable = false)
    private String certificationManagement;

    @Column(name = "foreign_language", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String foreignLanguage;

    @Column(name = "experienced_year", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String experiencedYear;

    @Builder(builderMethodName = "managerBuilder")
    public Manager(String username, String firstName, String lastName, String hashPassword, String phoneNumber, Integer role, Address address,
                   Integer status, LocalDateTime createAt, String certificationManagement, String foreignLanguage, String experiencedYear){
        super(username, firstName, lastName, hashPassword, phoneNumber, role, address, status, createAt);
        this.certificationManagement = certificationManagement;
        this.foreignLanguage = foreignLanguage;
        this.experiencedYear = experiencedYear;
    }

}