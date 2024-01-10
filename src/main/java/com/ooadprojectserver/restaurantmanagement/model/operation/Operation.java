package com.ooadprojectserver.restaurantmanagement.model.operation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ooadprojectserver.restaurantmanagement.model.CommonEntity;
import com.ooadprojectserver.restaurantmanagement.model.inventory.Inventory;
import com.ooadprojectserver.restaurantmanagement.model.user.Manager;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "operation")
public class Operation extends CommonEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private OperationType type;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;


}