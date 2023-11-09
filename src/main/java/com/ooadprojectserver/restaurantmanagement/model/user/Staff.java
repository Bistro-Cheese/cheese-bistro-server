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
@Table(name = "staff")
public class Staff extends User {
    @Column(name = "frg_lg")
    private String foreignLanguage;

    @Column(name = "acdmic_lv")
    private String academicLevel;

    public Staff(User user, String foreignLanguage, String academicLevel){
        super(user);
        this.foreignLanguage = foreignLanguage;
        this.academicLevel = academicLevel;
    }
}