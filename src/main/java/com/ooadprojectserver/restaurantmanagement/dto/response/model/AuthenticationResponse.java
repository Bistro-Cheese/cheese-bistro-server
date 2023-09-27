package com.ooadprojectserver.restaurantmanagement.dto.response.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String access_token;
    private String refresh_token;
}
