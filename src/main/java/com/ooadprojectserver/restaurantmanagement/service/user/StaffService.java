package com.ooadprojectserver.restaurantmanagement.service.user;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderLineRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.PaymentRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderLineResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.TableInfoResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.composition.Composition;
import com.ooadprojectserver.restaurantmanagement.model.composition.food.Food;
import com.ooadprojectserver.restaurantmanagement.model.order.*;
import com.ooadprojectserver.restaurantmanagement.model.order.payment.Payment;
import com.ooadprojectserver.restaurantmanagement.model.order.payment.PaymentType;
import com.ooadprojectserver.restaurantmanagement.model.user.AccountStatus;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.UpdateProfileRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.UserRegisterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.schedule.StaffScheduleResponse;
import com.ooadprojectserver.restaurantmanagement.model.schedule.Schedule;
import com.ooadprojectserver.restaurantmanagement.model.schedule.Timekeeping;
import com.ooadprojectserver.restaurantmanagement.model.user.Address;
import com.ooadprojectserver.restaurantmanagement.model.user.type.Staff;
import com.ooadprojectserver.restaurantmanagement.repository.food.CompositionRepository;
import com.ooadprojectserver.restaurantmanagement.repository.food.FoodRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderLineRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderTableRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.PaymentRepository;
import com.ooadprojectserver.restaurantmanagement.repository.schedule.TimekeepingRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.AddressRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.StaffRepository;
import com.ooadprojectserver.restaurantmanagement.service.authentication.JwtService;
import com.ooadprojectserver.restaurantmanagement.service.inventory.InventoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final InventoryService inventoryService;

    private final StaffRepository staffRepository;
    private final AddressRepository addressRepository;
    private final TimekeepingRepository timekeepingRepository;
    private final FoodRepository foodRepository;
    private final CompositionRepository compositionRepository;

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineRepository orderLineRepository;

    public void createUser(UserRegisterRequest request) {
        String sDob = request.getDateOfBirth();
        Date dob;
        try {
            dob = new SimpleDateFormat(DateTimeConstant.FORMAT_DATE).parse(sDob);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        staffRepository.save(Staff.staffBuilder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(dob)
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .status(AccountStatus.INACTIVE.getValue())
                .address(
                        addressRepository.save(
                                Address.builder()
                                        .addressLine(request.getAddressLine())
                                        .city(request.getCity())
                                        .region(request.getRegion())
                                        .build()
                        )
                )
                .academicLevel(request.getAcademicLevel())
                .foreignLanguage(request.getForeignLanguage())
                .email(request.getEmail())
                .createdDate(new Date())
                .lastModifiedDate(new Date())
                .enabled(false)
                .build());
    }

    public void updateUser(UUID user_id, UpdateProfileRequest request) {
        staffRepository.updateStaff(
                request.getAcademicLevel(),
                request.getForeignLanguage(),
                user_id
        );
    }

    public StaffScheduleResponse getSchedule(HttpServletRequest request) {
        String username = jwtService.getUsernameFromHeader(request);
        List<Timekeeping> timekeepingList = timekeepingRepository.findStaffSchedule(username);
        List<Schedule> scheduleList = new ArrayList<>();
        for (Timekeeping timekeeping : timekeepingList) {
            scheduleList.add(timekeeping.getSchedule());
        }

        Comparator<Schedule> comparator = Comparator
                .comparing(Schedule::getDay)
                .thenComparing(Schedule::getShift);

        scheduleList.sort(comparator);

        return StaffScheduleResponse.builder()
                .schedule(scheduleList)
                .build();
    }

    public List<OrderResponse> getOrders(HttpServletRequest request) {
        String username = jwtService.getUsernameFromHeader(request);
        Staff staff = (Staff) staffRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );

        List<OrderResponse> orderResponseList = new ArrayList<>();

        List<OrderTable> orderTableList = orderTableRepository.findAll();
        for (OrderTable orderTable : orderTableList) {
            if (orderTable.getTableStatus().equals(TableStatus.EMPTY)) {
                orderResponseList.add(
                        OrderResponse.builder()
                                .table(
                                        TableInfoResponse.builder()
                                                .id(orderTable.getId())
                                                .number(orderTable.getTableNumber())
                                                .status(orderTable.getTableStatus().ordinal())
                                                .build()
                                )
                                .build()
                );
                continue;
            }
            List<OrderLineResponse> orderLineResponseList = new ArrayList<>();
            Order order = orderRepository.findOrder(staff.getId(), orderTable.getId(), OrderStatus.PENDING);
            List<OrderLine> orderLineList = orderLineRepository.findByOrder_Id(order.getId());
            for (OrderLine orderLine : orderLineList) {
                orderLineResponseList.add(
                        OrderLineResponse.builder()
                                .id(orderLine.getId())
                                .foodId(orderLine.getFood().getId())
                                .foodName(orderLine.getFood().getName())
                                .foodPrice(orderLine.getFood().getPrice())
                                .quantity(orderLine.getQuantity())
                                .build()
                );
            }
            orderResponseList.add(
                    OrderResponse.builder()
                            .table(
                                    TableInfoResponse.builder()
                                            .id(orderTable.getId())
                                            .number(orderTable.getTableNumber())
                                            .status(orderTable.getTableStatus().ordinal())
                                            .build()
                            )
                            .orderId(order.getId())
                            .orderLines(orderLineResponseList)
                            .build()
            );
        }
        return orderResponseList;
    }

    public void createOrder(HttpServletRequest request, OrderRequest orderRequest) {
        String username = jwtService.getUsernameFromHeader(request);
        Staff staff = (Staff) staffRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getTableId()).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_TABLE_NOT_FOUND)
        );
        if (!orderTable.getTableStatus().equals(TableStatus.EMPTY)) {
            throw new CustomException(APIStatus.ORDER_TABLE_NOT_EMPTY);
        }
        orderTableRepository.updateTableStatusById(TableStatus.OCCUPIED, orderTable.getId());
        orderRepository.save(Order.builder()
                .staff(staff)
                .orderTable(orderTable)
                .orderDate(new Date())
                .status(OrderStatus.PENDING)
                .build());
    }

    public void deleteOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_NOT_FOUND)
        );
        List<OrderLine> orderLineList = orderLineRepository.findByOrder_Id(orderId);
        for (OrderLine orderLine : orderLineList) {
            List<Composition> compositionList = compositionRepository.findByFood(orderLine.getFood().getId());
            for (Composition composition : compositionList) {
                inventoryService.importIngredient(
                        (double) (composition.getPortion() * orderLine.getQuantity()),
                        composition.getIngredient().getId()
                );
            }
            orderLineRepository.deleteById(orderLine.getId());
        }
        orderRepository.deleteById(orderId);
        orderTableRepository.updateTableStatusById(TableStatus.EMPTY, order.getOrderTable().getId());
    }

    public void createOrderLine(UUID orderId, OrderLineRequest orderLineRequest) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_NOT_FOUND)
        );
        Food food = foodRepository.findById(orderLineRequest.getFoodId()).orElseThrow(
                () -> new CustomException(APIStatus.FOOD_NOT_FOUND)
        );

        List<Composition> compositionList = compositionRepository.findByFood(food.getId());
        for (Composition composition : compositionList) {
            inventoryService.useIngredient(
                    composition.getIngredient().getId(),
                    composition.getPortion() * orderLineRequest.getQuantity()
            );
        }

        orderLineRepository.save(OrderLine.builder()
                .order(order)
                .food(food)
                .quantity(orderLineRequest.getQuantity())
                .build());
    }

    public void updateOrderLine(UUID orderLineId, OrderLineRequest orderLineRequest) {
        OrderLine orderLine = orderLineRepository.findById(orderLineId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_LINE_NOT_FOUND)
        );

        List<Composition> compositionList = compositionRepository.findByFood(orderLine.getFood().getId());
        if (orderLineRequest.getQuantity() < orderLine.getQuantity()) {
            for (Composition composition : compositionList) {
                inventoryService.importIngredient(
                        (double) (composition.getPortion() * (orderLine.getQuantity() - orderLineRequest.getQuantity())),
                        composition.getIngredient().getId()
                );
            }
        } else {
            for (Composition composition : compositionList) {
                inventoryService.useIngredient(
                        composition.getIngredient().getId(),
                        composition.getPortion() * (orderLineRequest.getQuantity() - orderLine.getQuantity())
                );
            }
        }
        orderLineRepository.updateQuantityById(
                orderLineRequest.getQuantity(),
                orderLineId
        );
    }

    public void deleteOrderLine(UUID orderLineId) {
        OrderLine orderLine = orderLineRepository.findById(orderLineId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_LINE_NOT_FOUND)
        );
        List<Composition> compositionList = compositionRepository.findByFood(orderLine.getFood().getId());
        for (Composition composition : compositionList) {
            inventoryService.importIngredient(
                    (double) (composition.getPortion() * orderLine.getQuantity()),
                    composition.getIngredient().getId()
            );
        }
        orderLineRepository.deleteById(orderLineId);
    }
}

