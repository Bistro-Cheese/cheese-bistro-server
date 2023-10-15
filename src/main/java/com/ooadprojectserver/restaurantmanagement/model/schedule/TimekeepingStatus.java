package com.ooadprojectserver.restaurantmanagement.model.schedule;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimekeepingStatus {
        ON_TIME,
        ABSENCE,
        LATE;
}
