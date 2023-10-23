package com.ooadprojectserver.restaurantmanagement.controller.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {
//    @GetMapping("/user")
//    public String handleGoogleCallback(@AuthenticationPrincipal OidcUser oidcUser) {
//        System.out.println(oidcUser);
//        return oidcUser.getEmail();
//    }
}
