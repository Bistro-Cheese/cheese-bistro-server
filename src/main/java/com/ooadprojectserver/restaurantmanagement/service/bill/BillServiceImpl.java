package com.ooadprojectserver.restaurantmanagement.service.bill;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.bill.BillRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.PaymentRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.bill.Bill;
import com.ooadprojectserver.restaurantmanagement.model.order.*;
import com.ooadprojectserver.restaurantmanagement.model.payment.Payment;
import com.ooadprojectserver.restaurantmanagement.repository.bill.BillRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderTableRepository;
import com.ooadprojectserver.restaurantmanagement.service.payment.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.copyProperties;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final OrderTableRepository orderTableRepository;


    @Override
    @Transactional
    public Bill create(BillRequest billRequest) {
        Bill bill = new Bill();
//         Find order by id
        Order order = getOrderById(billRequest.getOrderId());
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new CustomException(APIStatus.ORDER_COMPLETED);
        }

//         Update order status
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

//        bill.setSubTotal(order.getTotal());

//         Update table status
        OrderTable table = order.getOrderTable();
        table.setTableStatus(TableStatus.EMPTY);
        orderTableRepository.save(table);

        // Create payment
        PaymentRequest paymentRequest = copyProperties(billRequest, PaymentRequest.class);
        Payment payment = paymentService.create(paymentRequest);

        // Find discount by id
//        if (billRequest.getDiscountId() != null) {
//            Discount discount = discountService.getById(billRequest.getDiscountId());
//            if (!discount.canUseDiscount()){
//                throw new CustomException(APIStatus.DISCOUNT_CANNOT_USE);
//            }
//            bill.setDiscount(discount);
//            BigDecimal total = bill.getSubTotal().subtract(bill.getDiscount().calculateDiscount(bill.getSubTotal()));
//            bill.setTotal(total);
//        } else {
//            bill.setTotal(bill.getSubTotal());
//        }

        // Create customer
//        BigDecimal mustPay = bill.getTotal().subtract(order.getDeposit());
//        CustomerCreateRequest customerCreateRequest = copyProperties(billRequest, CustomerCreateRequest.class);
//        customerCreateRequest.setSpend(mustPay);
//        Customer customer = customerService.create(customerCreateRequest);

        // Check payment, paid must be greater than or equal to mustPay
        if (billRequest.getPaid().compareTo(order.getTotal()) < 0) {
            throw new CustomException(APIStatus.PAYMENT_NOT_ENOUGH);
        }

        bill.setOrder(order);
        bill.setTotal(order.getTotal());
        bill.setPayment(payment);
        bill.setPaid(billRequest.getPaid());
        bill.setChange(billRequest.getPaid().subtract(order.getTotal()));
        bill.setCusIn(order.getCusIn());
        bill.setCusOut(new Date());

        return billRepository.save(bill);
    }

    @Override
    public void update(BillRequest billRequest) {

    }

    private Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_NOT_FOUND)
        );
    }

}
