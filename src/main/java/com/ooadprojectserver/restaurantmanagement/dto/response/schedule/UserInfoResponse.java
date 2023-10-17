package com.ooadprojectserver.restaurantmanagement.dto.response.schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    @JsonProperty("schedule_id")
    private Long scheduleId;
    private String username;
    private String firstName;
    private String lastName;
    @JsonProperty("phone_number")
    private String phoneNumber;
}
