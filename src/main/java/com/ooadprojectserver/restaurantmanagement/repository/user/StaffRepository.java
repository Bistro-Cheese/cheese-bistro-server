package com.ooadprojectserver.restaurantmanagement.repository.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

@Transactional
public interface StaffRepository extends UserRepository {
    @Modifying
    @Query("""
            update Staff s set
            s.academicLevel = :academicLevel,
            s.foreignLanguage = :foreignLanguage
            where s.id = :id
    """)
    void updateStaff(
            String academicLevel,
            String foreignLanguage,
            UUID id
    );
}
