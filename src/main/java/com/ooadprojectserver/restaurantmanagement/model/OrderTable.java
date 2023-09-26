package com.ooadprojectserver.restaurantmanagement.model;

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
@Table(name = "order_table")
public class OrderTable implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "table_name", nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer tableName;

    @Column(name = "seat_number", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer seatNumber;

    @Column(name = "is_empty")
    @JdbcTypeCode(SqlTypes.BOOLEAN)
    private Boolean isEmpty;

    @Column(name = "is_reserved")
    @JdbcTypeCode(SqlTypes.BOOLEAN)
    private Boolean isReserved;

    @Column(name = "name_customer")
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String nameCustomer;

}