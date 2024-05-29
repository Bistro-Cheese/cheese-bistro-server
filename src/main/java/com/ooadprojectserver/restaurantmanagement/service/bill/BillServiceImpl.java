package com.ooadprojectserver.restaurantmanagement.service.bill;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.bill.BillRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.PaymentRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderLineSearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.bill.BillResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderLineResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.bill.Bill;
import com.ooadprojectserver.restaurantmanagement.model.order.*;
import com.ooadprojectserver.restaurantmanagement.model.payment.Payment;
import com.ooadprojectserver.restaurantmanagement.repository.bill.BillRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderLineRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderTableRepository;
import com.ooadprojectserver.restaurantmanagement.service.customer.CustomerService;
import com.ooadprojectserver.restaurantmanagement.service.payment.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.copyProperties;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final PaymentService paymentService;
    private final OrderTableRepository orderTableRepository;
    private final CustomerService customerService;

    @Override
    @Transactional
    public Bill create(BillRequest billRequest) {
        Bill bill = new Bill();

        Order order = getOrderById(billRequest.getOrderId());
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new CustomException(APIStatus.ORDER_COMPLETED);
        }

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        OrderTable table = order.getOrderTable();
        table.setTableStatus(TableStatus.EMPTY);
        orderTableRepository.save(table);

        // Create payment
        PaymentRequest paymentRequest = copyProperties(billRequest, PaymentRequest.class);
        Payment payment = paymentService.create(paymentRequest);

        // Check payment, paid must be greater than or equal to mustPay
        BigDecimal mustPay = order.getTotal();
        BigDecimal paid = billRequest.getPaid().add(order.getDeposit());

        if (paid.compareTo(mustPay) < 0) {
            throw new CustomException(APIStatus.PAYMENT_NOT_ENOUGH);
        }

        // update Customer
        customerService.updateAfterOrder(order.getCustomer().getId(), order.getTotal());

        bill.setOrder(order);
        bill.setTotal(order.getTotal());
        bill.setPayment(payment);
        bill.setPaid(paid);
        bill.setChange(paid.subtract(order.getTotal()));
        bill.setCusIn(order.getCusIn());
        bill.setCusOut(new Date());

        return billRepository.save(bill);
    }

    @Override
    public List<BillResponse> getBillByOrderId(UUID orderId) {
         List<BillResponse> bills = billRepository.getBillByOrderId(orderId);

         List<OrderLineResponse> orderLines = orderLineRepository.search(
                 OrderLineSearchRequest.builder()
                         .orderId(orderId).build()
         );
         bills.get(0).setOrderLines(orderLines);

        return bills;
    }

    private Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_NOT_FOUND)
        );
    }
}
