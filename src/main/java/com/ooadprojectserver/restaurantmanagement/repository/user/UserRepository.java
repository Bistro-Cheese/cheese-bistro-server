package com.ooadprojectserver.restaurantmanagement.repository.user;

import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.Role;
import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    List<User> findByRole(Role role);
}
