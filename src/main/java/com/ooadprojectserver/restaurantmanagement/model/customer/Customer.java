package com.ooadprojectserver.restaurantmanagement.model.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ooadprojectserver.restaurantmanagement.model.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer")
public class Customer extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "cus_nme",nullable = false)
    private String customerName;

    @Column(name = "phone_num",nullable = false)
    private String phoneNumber;

    @Column(name = "cus_type",nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private CustomerType customerType;

    @Column(name = "visit",nullable = false)
    private int visitCount;

    @Column(name = "total_spent",nullable = false)
    private BigDecimal totalSpent;

    public void addVisitCount(){
        this.visitCount++;
    }

    public void addTotalSpent(BigDecimal amount){
        this.totalSpent = this.totalSpent.add(amount);
    }

    public void setCustomerType(){
        if (this.visitCount >= CustomerType.VIP.getVisitCount() &&
                this.totalSpent.compareTo(BigDecimal.valueOf(CustomerType.VIP.getTotalSpent())) > 0){
            this.customerType = CustomerType.VIP;
        } else if (this.visitCount >= CustomerType.MEMBERSHIP.getVisitCount() &&
                this.totalSpent.compareTo(BigDecimal.valueOf(CustomerType.MEMBERSHIP.getTotalSpent())) > 0){
            this.customerType = CustomerType.MEMBERSHIP;
        } else {
            this.customerType = CustomerType.STANDARD;
        }
    }

    public BigDecimal calculateDiscount(BigDecimal total){
        if (this.customerType == CustomerType.VIP){
            return total.subtract(total.multiply(BigDecimal.valueOf(0.1)));
        } else if (this.customerType == CustomerType.MEMBERSHIP){
            return total.subtract(total.multiply(BigDecimal.valueOf(0.05)));
        } else {
            return total;
        }
    }
}
