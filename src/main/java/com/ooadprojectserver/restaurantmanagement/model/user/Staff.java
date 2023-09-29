package com.ooadprojectserver.restaurantmanagement.model.user;

import com.ooadprojectserver.restaurantmanagement.model.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "staff")
public class Staff extends User {
    @Column(name = "foreign_language", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String foreignLanguage;

    @Column(name = "academic_level", nullable = false)
    private String academicLevel;

    @Builder(builderMethodName = "staffBuilder")
    public Staff(String username, String firstName, String lastName,
                 Date dateOfBirth, String hashPassword, String phoneNumber, Integer role, Integer status, Address address,
                 Date createdDate, Date lastModifiedDate, String foreignLanguage, String academicLevel){
        super(username, firstName, lastName, dateOfBirth, hashPassword, phoneNumber, role, address, status, createdDate, lastModifiedDate);
        this.academicLevel = academicLevel;
        this.foreignLanguage = foreignLanguage;
    }

}