package com.ooadprojectserver.restaurantmanagement.service.customer;

import com.ooadprojectserver.restaurantmanagement.dto.request.CustomerRequest;
import com.ooadprojectserver.restaurantmanagement.model.customer.Customer;
import com.ooadprojectserver.restaurantmanagement.model.customer.CustomerType;
import com.ooadprojectserver.restaurantmanagement.repository.CustomerRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final UserDetailService userDetailService;

    @Override
    public void create(CustomerRequest customerRequest) {
        UUID staffId = userDetailService.getIdLogin();

        Customer customer = Customer.builder()
                .customerName(customerRequest.getCustomerName())
                .phoneNumber(customerRequest.getPhoneNumber())
                .visitCount(1)
                .totalSpent(customerRequest.getSpend())
                .customerType(CustomerType.STANDARD)
                .build();
        customer.setCommonCreate(staffId);

        customerRepository.save(customer);
    }

    @Override
    public Customer getById(UUID customerId) {
        return customerRepository.findById(customerId).orElseThrow(
                () -> new RuntimeException("Customer not found")
        );
    }

    @Override
    public Customer getByNameAndPhone(String customerName, String phoneNumber) {
        return customerRepository.findByCustomerNameAndPhoneNumber(customerName, phoneNumber).orElseThrow(
                () -> new RuntimeException("Customer not found")
        );
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public void updateAfterOrder(UUID customerId, CustomerRequest customerRequest) {
        Customer customer = this.getById(customerId);

        customer.addTotalSpent(customerRequest.getSpend());
        customer.addVisitCount();
        customer.setCustomerType();

        customer.setCommonUpdate(userDetailService.getIdLogin());

        customerRepository.save(customer);
    }

    @Override
    public void delete(UUID customerId) {
        Customer customer = this.getById(customerId);
        customerRepository.delete(customer);
    }
}
