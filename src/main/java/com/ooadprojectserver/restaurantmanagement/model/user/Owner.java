package com.ooadprojectserver.restaurantmanagement.model.user;

import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "owner")
public class Owner extends User {
    @Column(name = "branch", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String branch;

    @Column(name = "licen_business", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String licenseBusiness;

    public Owner (User user, String branch, String licenseBusiness){
        super(user);
        this.branch = branch;
        this.licenseBusiness = licenseBusiness;
    }
}