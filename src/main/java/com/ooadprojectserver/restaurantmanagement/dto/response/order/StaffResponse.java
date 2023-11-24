package com.ooadprojectserver.restaurantmanagement.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffResponse {
    private UUID id;
    private String username;
    private String avatar;
    private String firstName;
    private String lastName;
}
