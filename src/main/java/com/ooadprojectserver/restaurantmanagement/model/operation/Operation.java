package com.ooadprojectserver.restaurantmanagement.model.operation;

import com.ooadprojectserver.restaurantmanagement.model.inventory.Inventory;
import com.ooadprojectserver.restaurantmanagement.model.user.Manager;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "operation")
public class Operation {
    @Id
    @Size(max = 16)
    @Column(name = "id", nullable = false, length = 16)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @NotNull
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private OperationType type;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @NotNull
    @Column(name = "crt_at", nullable = false)
    private Date createAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "crt_by", nullable = false)
    private Manager createBy;

}