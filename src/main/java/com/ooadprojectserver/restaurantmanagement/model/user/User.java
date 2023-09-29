package com.ooadprojectserver.restaurantmanagement.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.model.Address;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String username;

    @Column(name = "first_name", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    @JsonFormat(pattern = DateTimeConstant.FORMAT_DATE, timezone = DateTimeConstant.TIMEZONE)
    @JdbcTypeCode(SqlTypes.DATE)
    private Date dateOfBirth;

    @Column(name = "hash_password", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String hashPassword;

    @Column(name = "phone_number", nullable = false, unique = true, length = 11)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String phoneNumber;

    @Column(name = "role_id", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer role;

    @Column(name = "status", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer status;

    @CreatedDate
    @Column(name = "created_date")
    @JsonFormat(pattern = DateTimeConstant.FORMAT_DATE_TIME, timezone = DateTimeConstant.TIMEZONE)
    @JdbcTypeCode(SqlTypes.TIMESTAMP)
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @JsonFormat(pattern = DateTimeConstant.FORMAT_DATE_TIME, timezone = DateTimeConstant.TIMEZONE)
    @JdbcTypeCode(SqlTypes.TIMESTAMP)
    private Date lastModifiedDate;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    public User(
            String username,
            String firstName,
            String lastName,
            Date dateOfBirth,
            String hashPassword,
            String phoneNumber,
            Integer role,
            Address address,
            Integer status,
            Date createdDate,
            Date lastModifiedDate
    ) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.hashPassword = hashPassword;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.address = address;
    }
}