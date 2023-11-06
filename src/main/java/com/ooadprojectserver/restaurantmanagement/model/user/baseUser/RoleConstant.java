package com.ooadprojectserver.restaurantmanagement.model.user.baseUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleConstant {
    //PERMISSION ROLE
    @RequiredArgsConstructor
    @Getter
    public enum PERMISSION {

        //owner's permission
        OWNER_READ("owner:read"),
        OWNER_UPDATE("owner:update"),
        OWNER_CREATE("owner:create"),
        OWNER_DELETE("owner:delete"),

        //manager's permission
        MANAGER_READ("management:read"),
        MANAGER_UPDATE("management:update"),
        MANAGER_CREATE("management:create"),
        MANAGER_DELETE("management:delete"),

        //staff's permission
        STAFF_READ("staff:read"),
        STAFF_UPDATE("staff:update"),
        STAFF_CREATE("staff:create"),
        STAFF_DELETE("staff:delete")
        ;

        private final String permission;
    }
    @RequiredArgsConstructor
    @Getter
    public enum ROLE{
        STAFF(1, Set.of(
                PERMISSION.STAFF_READ,
                PERMISSION.STAFF_UPDATE,
                PERMISSION.STAFF_CREATE,
                PERMISSION.STAFF_DELETE
        )),

        MANAGER(2, Set.of(
                PERMISSION.MANAGER_READ,
                PERMISSION.MANAGER_UPDATE,
                PERMISSION.MANAGER_CREATE,
                PERMISSION.MANAGER_DELETE,
                PERMISSION.STAFF_READ,
                PERMISSION.STAFF_UPDATE,
                PERMISSION.STAFF_CREATE,
                PERMISSION.STAFF_DELETE
        )),
        OWNER(3, Set.of(
                PERMISSION.OWNER_READ,
                PERMISSION.OWNER_UPDATE,
                PERMISSION.OWNER_CREATE,
                PERMISSION.OWNER_DELETE,
                PERMISSION.MANAGER_READ,
                PERMISSION.MANAGER_UPDATE,
                PERMISSION.MANAGER_CREATE,
                PERMISSION.MANAGER_DELETE
        ));

        private final Integer value;

        private final Set<PERMISSION> permissions;

        public List<SimpleGrantedAuthority> getAuthorities() {
            var authorities = getPermissions()
                    .stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                    .collect(Collectors.toList());
            authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
            return authorities;
        }
    }
}
