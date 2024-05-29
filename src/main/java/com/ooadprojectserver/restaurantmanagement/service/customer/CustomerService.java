package com.ooadprojectserver.restaurantmanagement.service.customer;

import com.ooadprojectserver.restaurantmanagement.dto.request.customer.CustomerCreateRequest;
import com.ooadprojectserver.restaurantmanagement.model.customer.Customer;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CustomerService {
    Customer create(CustomerCreateRequest customerCreateRequest);
    Customer getById(UUID customerId);
    Customer getByNameAndPhone(String customerName, String phoneNumber);
    List<Customer> getAll();
    void updateAfterOrder(UUID customerId, BigDecimal totalSpent);
    void delete(UUID customerId);
}
