package com.ooadprojectserver.restaurantmanagement.repository.user;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

@Transactional
public interface ManagerRepository extends UserRepository {
    @Modifying
    @Query("""
            update Manager m set
            m.certificationManagement = :certificationManagement,
            m.experiencedYear = :experiencedYear,
            m.foreignLanguage = :foreignLanguage
            where m.id = :id
    """)
    void updateManager(
            String certificationManagement,
            String experiencedYear,
            String foreignLanguage,
            UUID id
    );
}
