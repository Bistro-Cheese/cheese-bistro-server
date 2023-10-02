package com.ooadprojectserver.restaurantmanagement.boostrap;

import com.ooadprojectserver.restaurantmanagement.model.Category;
import com.ooadprojectserver.restaurantmanagement.model.Role;
import com.ooadprojectserver.restaurantmanagement.repository.CategoryRepository;
import com.ooadprojectserver.restaurantmanagement.repository.RoleRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class Dataseeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        this.loadRoles();
        this.LoadCategory();
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
     private void LoadCategory(){
         List<Category> categories = new ArrayList<Category>(
                 List.of(
                         new Category(UUID.randomUUID(), "drink"),
                         new Category(UUID.randomUUID(), "main Course"),
                         new Category(UUID.randomUUID(), "appetizer"),
                         new Category(UUID.randomUUID(), "dessert")
                 )
         );
         categories.forEach(category -> {
             Optional<Category> optionalCategory = categoryRepository.findByName(category.getName());
             optionalCategory.ifPresentOrElse(System.out::println, () -> {
                 categoryRepository.save(category);
             });
         });
     }
}
