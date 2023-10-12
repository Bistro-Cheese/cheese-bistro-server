package com.ooadprojectserver.restaurantmanagement.repository.user;

import com.ooadprojectserver.restaurantmanagement.model.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Transactional
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);

    @Modifying
    @Query("""
            update User u set
            u.firstName = ?1,
            u.lastName = ?2,
            u.lastModifiedDate = ?3,
            u.dateOfBirth = ?4,
            u.phoneNumber = ?5
            where u.id = ?6
    """)
    void updateUserById(
            String firstName,
            String lastName,
            Date lastModifiedDate,
            Date dateOfBirth,
            String phoneNumber,
            UUID id
    );

}
