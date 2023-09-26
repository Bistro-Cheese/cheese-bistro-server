package com.ooadprojectserver.restaurantmanagement.repository;

import com.ooadprojectserver.restaurantmanagement.model.Role;
import com.ooadprojectserver.restaurantmanagement.util.Constant;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findById(Integer id);
}
