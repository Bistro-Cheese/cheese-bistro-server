package com.ooadprojectserver.restaurantmanagement.model.order;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    private Long id;

    @Column(name = "tab_num")
    private Integer tableNumber;

    @Column(name = "status", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private TableStatus tableStatus;

    @Column(name = "seat_num", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer seatNumber;
}