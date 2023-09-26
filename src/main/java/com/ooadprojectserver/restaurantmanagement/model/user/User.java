package com.ooadprojectserver.restaurantmanagement.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ooadprojectserver.restaurantmanagement.dto.response.util.APIStatus;
import com.ooadprojectserver.restaurantmanagement.exception.ApplicationException;
import com.ooadprojectserver.restaurantmanagement.model.Address;
import com.ooadprojectserver.restaurantmanagement.util.Constant;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
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

    @Column(name = "create_at")
    @JdbcTypeCode(SqlTypes.TIMESTAMP)
    LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    public User(String username, String firstName, String lastName, String hashPassword, String phoneNumber, Integer role, Address address, Integer status, LocalDateTime createAt) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hashPassword = hashPassword;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
        this.createAt = createAt;
        this.address = address;
    }
}