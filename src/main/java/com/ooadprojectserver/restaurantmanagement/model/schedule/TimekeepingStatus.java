package com.ooadprojectserver.restaurantmanagement.model.schedule;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimekeepingStatus {
        ON_TIME(1, "On-time"),
        ABSENT(2, "Absent"),
        LATE(3, "Late");

        private final int value;
        private final String status;
}
