package com.ooadprojectserver.restaurantmanagement.dto.response.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String access_token;
    @JsonProperty("refresh_token")
    private String refresh_token;
}
