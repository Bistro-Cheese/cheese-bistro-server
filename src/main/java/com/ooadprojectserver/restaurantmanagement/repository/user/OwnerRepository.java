package com.ooadprojectserver.restaurantmanagement.repository.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

@Transactional
public interface OwnerRepository extends UserRepository {
    @Modifying
    @Query("""
            update Owner o set
            o.branch = :branch,
            o.licenseBusiness = :licenseBusiness
            where o.id = :id
    """)
    void updateOwner(
            String branch,
            String licenseBusiness,
            UUID id
    );

}

/*
* select o from Owner o inner join User u
            on o.id = u.id
            where u.id = :id
            update o set
            o.branch = :branch,
            o.licenseBusiness = :licenseBusiness*/