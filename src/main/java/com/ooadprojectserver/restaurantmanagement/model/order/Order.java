package com.ooadprojectserver.restaurantmanagement.model.order;

import com.ooadprojectserver.restaurantmanagement.model.CommonEntity;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "res_order")
public class Order extends CommonEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private OrderTable orderTable;

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;
}