package com.ooadprojectserver.restaurantmanagement.model.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.model.user.Manager;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Table(name = "timekeeping")
public class Timekeeping implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "sche_id")
    private Schedule schedule;

    @Column(name = "crt_at")
    @JsonFormat(pattern = DateTimeConstant.FORMAT_DATE_TIME, timezone = DateTimeConstant.TIMEZONE)
    private LocalDateTime workDate;

    @Column(name = "status")
    private Integer status;
}