package com.ooadprojectserver.restaurantmanagement.model.user.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.model.user.RoleConstant;
import com.ooadprojectserver.restaurantmanagement.model.user.AccountStatus;
import com.ooadprojectserver.restaurantmanagement.model.user.Address;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "user")
@RequiredArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails, Serializable {
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

    @Column(name = "email", nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String email;

    @Column(name = "date_of_birth", nullable = false)
    @JsonFormat(pattern = DateTimeConstant.FORMAT_DATE, timezone = DateTimeConstant.TIMEZONE)
    @JdbcTypeCode(SqlTypes.DATE)
    private Date dateOfBirth;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String password;

    @Column(name = "phone_number", nullable = false, unique = true, length = 30)
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

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @JsonIgnore
    private boolean enabled;

    public User(
            String username,
            String firstName,
            String lastName,
            String email,
            Date dateOfBirth,
            String password,
            String phoneNumber,
            Integer role,
            Address address,
            Integer status,
            Date createdDate,
            Date lastModifiedDate,
            boolean enabled
    ) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.address = address;
        this.enabled = enabled;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (role) {
            case 1 -> RoleConstant.ROLE.STAFF.getAuthorities();
            case 2 -> RoleConstant.ROLE.MANAGER.getAuthorities();
            case 3 -> RoleConstant.ROLE.OWNER.getAuthorities();
            default -> throw new IllegalStateException("Unexpected value: " + role);
        };
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}