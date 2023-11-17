package com.ooadprojectserver.restaurantmanagement.model.schedule;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "schedule_line")
public class ScheduleLine {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @NotNull
    @Column(name = "shift", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Shift shift;

    @NotNull
    @Column(name = "work_date", nullable = false)
    private Instant workDate;

    @Column(name = "timekeeping_by", length = 16)
    private UUID timekeepingBy;

    @Column(name = "status")
    private Byte status;

}