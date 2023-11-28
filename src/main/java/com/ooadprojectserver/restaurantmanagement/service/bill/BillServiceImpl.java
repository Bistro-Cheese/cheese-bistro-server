package com.ooadprojectserver.restaurantmanagement.service.bill;

import com.ooadprojectserver.restaurantmanagement.dto.request.BillRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.PaymentRequest;
import com.ooadprojectserver.restaurantmanagement.model.bill.Bill;
import com.ooadprojectserver.restaurantmanagement.model.discount.Discount;
import com.ooadprojectserver.restaurantmanagement.model.order.Order;
import com.ooadprojectserver.restaurantmanagement.model.order.OrderStatus;
import com.ooadprojectserver.restaurantmanagement.model.order.OrderTable;
import com.ooadprojectserver.restaurantmanagement.model.order.TableStatus;
import com.ooadprojectserver.restaurantmanagement.model.payment.Payment;
import com.ooadprojectserver.restaurantmanagement.repository.bill.BillRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderTableRepository;
import com.ooadprojectserver.restaurantmanagement.service.discount.DiscountService;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderService;
import com.ooadprojectserver.restaurantmanagement.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.copyProperties;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final OrderRepository orderRepository;

    private final OrderService orderService;
    private final DiscountService discountService;
    private final PaymentService paymentService;
    private final OrderTableRepository orderTableRepository;

    @Override
    public void create(BillRequest billRequest) {
        Bill bill = new Bill();

        // Find order by id
        Order order = orderService.getById(billRequest.getOrderId());

        // Update order status
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        // Update table status
        OrderTable table = order.getOrderTable();
        table.setTableStatus(TableStatus.EMPTY);
        orderTableRepository.save(table);

        // Create payment
        PaymentRequest paymentRequest = copyProperties(billRequest, PaymentRequest.class);
        Payment payment = paymentService.create(paymentRequest);

        // Find discount by id
        if (billRequest.getDiscountId() != null) {
            Discount discount = discountService.getById(billRequest.getDiscountId());
            bill.setDiscount(discount);
        }

        BigDecimal total = orderService.calculateSubTotal(billRequest.getOrderId())
                .subtract(discountService.calculateDiscount(
                                orderService.calculateSubTotal(billRequest.getOrderId()),
                                bill.getDiscount()
                        )
                );

        bill.setOrder(order);
        bill.setPayment(payment);
        bill.setTotal(total);
        bill.setSubTotal(orderService.calculateSubTotal(billRequest.getOrderId()));
        bill.setPaid(BigDecimal.valueOf(billRequest.getPaid()));
        bill.setChange(BigDecimal.valueOf(billRequest.getPaid()).subtract(total));

        bill.setCusIn(order.getCreatedAt());
        bill.setCusOut(new Date());

        billRepository.save(bill);
    }
}
