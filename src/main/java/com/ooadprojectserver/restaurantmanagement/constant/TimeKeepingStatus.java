package com.ooadprojectserver.restaurantmanagement.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeKeepingStatus {
        ABSENCE(0),
        LATE(1);

        private final int status;
}
