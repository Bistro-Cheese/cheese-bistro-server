package com.ooadprojectserver.restaurantmanagement.dto.response.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import lombok.*;
import org.springframework.http.ResponseCookie;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String access_token;

    @JsonProperty("refresh_token_cookie")
    @JsonIgnore
    private ResponseCookie refreshTokenCookie;
}
