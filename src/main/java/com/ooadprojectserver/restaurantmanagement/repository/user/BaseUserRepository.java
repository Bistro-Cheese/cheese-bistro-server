package com.ooadprojectserver.restaurantmanagement.repository.user;

import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.model.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Transactional
@Repository
public interface BaseUserRepository<T extends User> extends JpaRepository<T, UUID> {

}

