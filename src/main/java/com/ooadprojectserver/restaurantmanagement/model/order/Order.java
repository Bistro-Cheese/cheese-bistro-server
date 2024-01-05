package com.ooadprojectserver.restaurantmanagement.model.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.model.CommonEntity;
import com.ooadprojectserver.restaurantmanagement.model.customer.Customer;
import com.ooadprojectserver.restaurantmanagement.model.discount.Discount;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.util.DateTimeUtils;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
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

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    @ManyToOne
    @JoinColumn(name = "cus_id")
    private Customer customer;

    @Column(name = "number_of_customer", nullable = false)
    private Integer numberOfCustomer;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "sub_total")
    @JdbcTypeCode(SqlTypes.DECIMAL)
    private BigDecimal subTotal;

    @Column(name = "deposit")
    private BigDecimal deposit;

    @Column(name = "cus_in")
    @JsonFormat(timezone = DateTimeConstant.TIMEZONE)
    private Timestamp cusIn;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private OrderTable orderTable;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;

    public void calculateDeposit() {
        if (status == OrderStatus.RESERVED) {
            this.deposit = this.total.multiply(BigDecimal.valueOf(0.8));
        } else {
            this.deposit = BigDecimal.valueOf(0);
        }
    }

    public void setCusInTime(Timestamp cusIn) {
        if (status == OrderStatus.RESERVED) {
            this.cusIn = cusIn;
            return;
        }
        this.cusIn =DateTimeUtils.resultTimestamp();
    }

    public BigDecimal calculateSubTotal() {
        return this.subTotal.subtract(this.discount.calculateDiscount(this.subTotal));
    }
}