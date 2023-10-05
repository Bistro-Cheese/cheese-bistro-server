package com.ooadprojectserver.restaurantmanagement.repository;

import com.ooadprojectserver.restaurantmanagement.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    @Transactional
    @Modifying
    @Query("update Address a set a.addressLine = ?1, a.city = ?2, a.region = ?3 where a.id = ?4")
    void updateAddressById(String addressLine, String city, String region, UUID id);
}
