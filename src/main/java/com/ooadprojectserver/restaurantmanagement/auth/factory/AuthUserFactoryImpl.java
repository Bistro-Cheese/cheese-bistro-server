package com.ooadprojectserver.restaurantmanagement.auth.factory;

import com.ooadprojectserver.restaurantmanagement.auth.model.AuthUser;
import com.ooadprojectserver.restaurantmanagement.model.Role;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import com.ooadprojectserver.restaurantmanagement.repository.RoleRepository;
import com.ooadprojectserver.restaurantmanagement.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthUserFactoryImpl implements AuthUserFactory{
    private final RoleRepository roleRepository;
    @Override
    public AuthUser createAuthUser(User user) {
        return AuthUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getHashPassword())
                .role(getUserRoleString(user.getRole()))
                .enabled(Objects.equals(user.getStatus(), Constant.STATUS.ACTIVE_STATUS.getValue()))
                .build();
    }
    private Constant.ROLE getUserRoleString (Integer roleId){
        Optional<Role> role = roleRepository.findById(roleId);
        if(role.isPresent()){
            return switch(role.get().getName()){
                case "MANAGER" -> Constant.ROLE.MANAGER;
                case "STAFF" -> Constant.ROLE.STAFF;
                case "OWNER" -> Constant.ROLE.OWNER;
                default -> throw new RuntimeException("NOT FOUND ROLE");
            };
        }else {
            throw new RuntimeException("NOT FOUND ROLE_ID IN DATABASE");
        }
    }
}
