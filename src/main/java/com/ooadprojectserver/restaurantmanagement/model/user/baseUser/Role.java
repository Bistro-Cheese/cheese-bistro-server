package com.ooadprojectserver.restaurantmanagement.model.user.baseUser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Role {
    OWNER(
            Set.of(
                    Permission.OWNER_READ,
                    Permission.OWNER_WRITE,
                    Permission.OWNER_UPDATE,
                    Permission.OWNER_DELETE,
                    Permission.MANAGER_READ,
                    Permission.MANAGER_WRITE,
                    Permission.MANAGER_UPDATE,
                    Permission.MANAGER_DELETE
            )
    ),
    MANAGER(
            Set.of(
                    Permission.MANAGER_READ,
                    Permission.MANAGER_WRITE,
                    Permission.MANAGER_UPDATE,
                    Permission.MANAGER_DELETE,
                    Permission.STAFF_READ,
                    Permission.STAFF_WRITE,
                    Permission.STAFF_UPDATE,
                    Permission.STAFF_DELETE
            )
    ),
    STAFF(
            Set.of(
                    Permission.STAFF_READ,
                    Permission.STAFF_WRITE,
                    Permission.STAFF_UPDATE,
                    Permission.STAFF_DELETE
            )
    );

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                        .stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                        .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

    public static Role convertIntegerToRole(int role) {
        return switch (role) {
            case 0 -> Role.OWNER;
            case 1 -> Role.MANAGER;
            case 2 -> Role.STAFF;
            default -> null;
        };
    }
}
