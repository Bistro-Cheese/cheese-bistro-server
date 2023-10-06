package com.ooadprojectserver.restaurantmanagement.model.user;

import com.ooadprojectserver.restaurantmanagement.model.Address;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

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
    public Manager(
            String username,
            String firstName,
            String lastName,
            Date dateOfBirth,
            String password,
            String phoneNumber,
            Integer role,
            Address address,
            Integer status,
            Date createdDate,
            Date lastModifiedDate,
            boolean enabled,
            String certificationManagement,
            String foreignLanguage,
            String experiencedYear
    ) {
        super( username, firstName, lastName, dateOfBirth, password, phoneNumber, role, address, status, createdDate, lastModifiedDate, enabled);
        this.certificationManagement = certificationManagement;
        this.foreignLanguage = foreignLanguage;
        this.experiencedYear = experiencedYear;
    }

}