package com.ooadprojectserver.restaurantmanagement.repository.user;

import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.repository.user.BaseUserRepository;
import jakarta.transaction.Transactional;

@Transactional
public interface StaffRepository extends BaseUserRepository<Staff> {

}
