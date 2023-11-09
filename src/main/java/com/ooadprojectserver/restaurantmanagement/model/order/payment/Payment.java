package com.ooadprojectserver.restaurantmanagement.model.order.payment;

import com.ooadprojectserver.restaurantmanagement.model.order.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "payment_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private PaymentType paymentType;

}