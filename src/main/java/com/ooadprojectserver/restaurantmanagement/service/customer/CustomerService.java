package com.ooadprojectserver.restaurantmanagement.service.customer;

import com.ooadprojectserver.restaurantmanagement.dto.request.CustomerRequest;
import com.ooadprojectserver.restaurantmanagement.model.customer.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    void create(CustomerRequest customerRequest);
    Customer getById(UUID customerId);
    Customer getByNameAndPhone(String customerName, String phoneNumber);
    List<Customer> getAll();
    void updateAfterOrder(UUID customerId,CustomerRequest customerRequest);
    void delete(UUID customerId);
}
