package com.ooadprojectserver.restaurantmanagement.model.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "res_table")
public class OrderTable implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "tab_num", nullable = false, unique = true)
    private Integer tableNumber;

    @NotNull
    @Column(name = "seat_num", nullable = false)
    private Integer seatNumber;

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TableStatus tableStatus;
}