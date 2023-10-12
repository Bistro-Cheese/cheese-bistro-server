package com.ooadprojectserver.restaurantmanagement.model.user.type;

import com.ooadprojectserver.restaurantmanagement.model.user.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

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
    public Owner(String username, String firstName, String lastName,
                 Date dateOfBirth, String password, String phoneNumber, Integer role, Integer status, Address address,
                 Date createdDate, Date lastModifiedDate, boolean enabled, String branch, String licenseBusiness){
        super(username, firstName, lastName, dateOfBirth, password, phoneNumber, role, address, status, createdDate, lastModifiedDate, enabled);
        this.branch = branch;
        this.licenseBusiness = licenseBusiness;
    }

}