package com.ooadprojectserver.restaurantmanagement.dto.response;

import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.Address;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.RoleConstant;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.Status;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
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

    public static UserResponse covertUserToUserResponse(User user) {
        String sRole = switch (user.getRole()) {
            case 1 -> RoleConstant.ROLE.OWNER.name().toLowerCase();
            case 2 -> RoleConstant.ROLE.MANAGER.name().toLowerCase();
            case 3 -> RoleConstant.ROLE.STAFF.name().toLowerCase();
            default -> throw new IllegalStateException("Unexpected value: " + user.getRole());
        };
        Integer status = switch (user.getStatus()) {
            case 0 -> Status.ACTIVE.ordinal();
            case 1 -> Status.INACTIVE.ordinal();
            default -> throw new IllegalStateException("Unexpected value: " + user.getStatus());
        };
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .status(status)
                .role(sRole)
                .build();
    }
}
