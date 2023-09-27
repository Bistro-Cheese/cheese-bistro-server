package com.ooadprojectserver.restaurantmanagement.repository.user;

import com.ooadprojectserver.restaurantmanagement.model.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@Transactional
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
