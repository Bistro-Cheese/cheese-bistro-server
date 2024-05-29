package com.ooadprojectserver.restaurantmanagement.service.customer;

import com.ooadprojectserver.restaurantmanagement.dto.request.customer.CustomerCreateRequest;
import com.ooadprojectserver.restaurantmanagement.model.customer.Customer;
import com.ooadprojectserver.restaurantmanagement.model.customer.CustomerType;
import com.ooadprojectserver.restaurantmanagement.repository.CustomerRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final UserDetailService userDetailService;

    @Override
    public Customer create(CustomerCreateRequest customerCreateRequest) {
        UUID staffId = userDetailService.getIdLogin();

        Optional<Customer> customer = customerRepository.findByPhoneNumber(
                customerCreateRequest.getPhoneNumber());

        if (customer.isPresent()) {
            return customer.get();
        } else {
            Customer newCustomer = Customer.builder()
                    .customerName(customerCreateRequest.getCustomerName())
                    .phoneNumber(customerCreateRequest.getPhoneNumber())
                    .totalSpent(BigDecimal.ZERO)
                    .visitCount(0)
                    .customerType(CustomerType.STANDARD)
                    .build();
            newCustomer.setCommonCreate(staffId);
            return customerRepository.save(newCustomer);
        }
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
    public void updateAfterOrder(UUID customerId, BigDecimal totalSpent) {
        Customer customer = this.getById(customerId);

        customer.addTotalSpent(totalSpent);
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
