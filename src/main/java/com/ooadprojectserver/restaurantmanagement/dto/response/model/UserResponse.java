package com.ooadprojectserver.restaurantmanagement.dto.response.model;

import com.ooadprojectserver.restaurantmanagement.model.user.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;
    private Date dateOfBirth;
    private Address address;
    private String status;
}
