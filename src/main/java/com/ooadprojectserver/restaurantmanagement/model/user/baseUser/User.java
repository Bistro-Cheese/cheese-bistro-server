package com.ooadprojectserver.restaurantmanagement.model.user.baseUser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.model.CommonEntity;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends CommonEntity implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "fst_name")
    private String firstName;

    @Column(name = "lst_name", nullable = false)
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "dob")
    @JdbcTypeCode(SqlTypes.TIMESTAMP)
    @JsonFormat(pattern = DateTimeConstant.FORMAT_DATE, timezone = DateTimeConstant.TIMEZONE)
    private Date dateOfBirth;

    @Column(name = "avt")
    private String avatar;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_num")
    private String phoneNumber;

    @Column(name = "role", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "addr_id")
    private Address address;

    private boolean enabled;

    public User(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.dateOfBirth = user.getDateOfBirth();
        this.avatar = user.getAvatar();
        this.password = user.getPassword();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.address = user.getAddress();
        this.enabled = user.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}