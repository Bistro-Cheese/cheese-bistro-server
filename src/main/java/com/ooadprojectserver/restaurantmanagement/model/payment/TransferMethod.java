package com.ooadprojectserver.restaurantmanagement.model.payment;

import com.ooadprojectserver.restaurantmanagement.model.CommonEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transfer_method")
public class TransferMethod extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "method_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private MethodType methodType;

    @NotNull
    @Column(name = "method_name", nullable = false)
    private String methodName;

    @NotNull
    @Column(name = "acc_num", nullable = false)
    private String accountNumber;

    @NotNull
    @Column(name = "acc_holder_name", nullable = false)
    private String accountHolderName;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
