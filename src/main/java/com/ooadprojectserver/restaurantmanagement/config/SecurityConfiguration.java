package com.ooadprojectserver.restaurantmanagement.config;

import com.ooadprojectserver.restaurantmanagement.config.filter.JwtAuthenticationFilter;
import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private LogoutHandler logoutHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(handlerExceptionResolver);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(withDefaults())
                .csrf(cors -> cors.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/foods/search").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/**").hasAuthority(
                                Permission.OWNER_WRITE.getPermission())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasAuthority(
                                Permission.OWNER_UPDATE.getPermission())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAuthority(
                                Permission.OWNER_DELETE.getPermission())
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/profile").hasAnyAuthority(
                                Permission.OWNER_READ.getPermission(),
                                Permission.MANAGER_READ.getPermission(),
                                Permission.STAFF_READ.getPermission()
                        )
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyAuthority(
                                Permission.OWNER_READ.getPermission(),
                                Permission.MANAGER_READ.getPermission()
                        )
                        .requestMatchers(HttpMethod.GET, "api/v1/foods/**").hasAnyAuthority(
                                Permission.OWNER_READ.getPermission(),
                                Permission.MANAGER_READ.getPermission(),
                                Permission.STAFF_READ.getPermission()
                        )
                        .requestMatchers(HttpMethod.POST, "api/v1/foods/**").hasAnyAuthority(
                                Permission.OWNER_WRITE.getPermission()
                        )
                        .requestMatchers(HttpMethod.PUT, "api/v1/foods/**").hasAnyAuthority(
                                Permission.OWNER_UPDATE.getPermission()
                        )
                        .requestMatchers(HttpMethod.DELETE, "api/v1/foods/**").hasAnyAuthority(
                                Permission.OWNER_DELETE.getPermission()
                        )
                        .requestMatchers(HttpMethod.GET, "api/v1/orders/**").hasAnyAuthority(
                                Permission.OWNER_READ.getPermission(),
                                Permission.MANAGER_READ.getPermission(),
                                Permission.STAFF_READ.getPermission()
                        )
                        .requestMatchers(HttpMethod.POST, "api/v1/orders/**").hasAnyAuthority(
                                Permission.STAFF_WRITE.getPermission()
                        ).
                        requestMatchers(HttpMethod.PUT, "api/v1/orders/**").hasAnyAuthority(
                                Permission.STAFF_UPDATE.getPermission()
                        )
                        .requestMatchers(HttpMethod.GET, "api/v1/tables/**").hasAnyAuthority(
                                Permission.OWNER_READ.getPermission(),
                                Permission.MANAGER_READ.getPermission(),
                                Permission.STAFF_READ.getPermission()
                        ).requestMatchers(HttpMethod.POST, "api/v1/tables/**").hasAnyAuthority(
                                Permission.MANAGER_WRITE.getPermission()
                        ).requestMatchers(HttpMethod.PUT, "api/v1/tables/**").hasAnyAuthority(
                                Permission.MANAGER_UPDATE.getPermission()
                        ).requestMatchers(HttpMethod.DELETE, "api/v1/tables/**").hasAnyAuthority(
                                Permission.MANAGER_DELETE.getPermission()
                        ).requestMatchers(HttpMethod.GET, "api/v1/discounts/**").hasAnyAuthority(
                                Permission.OWNER_READ.getPermission(),
                                Permission.STAFF_READ.getPermission()
                        )
                        .requestMatchers(HttpMethod.POST, "api/v1/discounts/**").hasAnyAuthority(
                                Permission.OWNER_WRITE.getPermission()
                        )
                        .requestMatchers(HttpMethod.PUT, "api/v1/discounts/**").hasAnyAuthority(
                                Permission.OWNER_UPDATE.getPermission()
                        )
                        .requestMatchers(HttpMethod.DELETE, "api/v1/discounts/**").hasAnyAuthority(
                                Permission.OWNER_DELETE.getPermission()
                        )
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic(withDefaults())
                .logout(logout -> logout
                        .logoutUrl(APIConstant.AUTH + APIConstant.LOGOUT)
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) ->
                                SecurityContextHolder.clearContext())
                );
        return httpSecurity.build();
    }
}
