package com.ooadprojectserver.restaurantmanagement.dto.response;

import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String role;
    private Date dateOfBirth;
    private Address address;
    private Integer status;
}
