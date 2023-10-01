package com.ooadprojectserver.restaurantmanagement.model.token;

import com.ooadprojectserver.restaurantmanagement.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token")
public class Token implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "token", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String token;

    @Column(name = "token_type", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(name = "expired", nullable = false)
    @JdbcTypeCode(SqlTypes.BOOLEAN)
    private boolean expired;

    @Column(name = "revoked", nullable = false)
    @JdbcTypeCode(SqlTypes.BOOLEAN)
    private boolean revoked;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;
}
