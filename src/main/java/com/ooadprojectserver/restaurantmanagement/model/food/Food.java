package com.ooadprojectserver.restaurantmanagement.model.food;

import com.ooadprojectserver.restaurantmanagement.model.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "food")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "description", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String description;

    @Column(name = "product_image", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String product_image;

    @Column(name = "price", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private BigDecimal price;


}