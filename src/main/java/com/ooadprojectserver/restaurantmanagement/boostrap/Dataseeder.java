package com.ooadprojectserver.restaurantmanagement.boostrap;

import com.ooadprojectserver.restaurantmanagement.model.Role;
import com.ooadprojectserver.restaurantmanagement.repository.RoleRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Dataseeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        this.loadRoles();
    }

    private void loadRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, "STAFF"));
        roles.add(new Role(2, "MANAGER"));
        roles.add(new Role(3, "OWNER"));
        roles.forEach(role -> {
            Optional<Role> optionalRole = roleRepository.findById(role.getId());
            optionalRole.ifPresentOrElse(System.out::println, () -> {
                roleRepository.save(role);
            });
        });
    }
}
