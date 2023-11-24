package com.ooadprojectserver.restaurantmanagement.service.order.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderTableRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.order.OrderTable;
import com.ooadprojectserver.restaurantmanagement.model.order.TableStatus;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderTableRepository;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderTableServiceImpl implements OrderTableService {
    private final OrderTableRepository repository;

    @Override
    public void create(OrderTableRequest request) {
        repository.findByTableNumber(request.getNumber()).ifPresent(table -> {
            throw new CustomException(APIStatus.ORDER_TABLE_ALREADY_EXIST);
        });
        OrderTable table = OrderTable.builder()
                .tableNumber(request.getNumber())
                .seatNumber(request.getSeats())
                .tableStatus(TableStatus.EMPTY)
                .build();
        repository.save(table);
    }

    @Override
    public void update(Integer tableId, OrderTableRequest request) {
        OrderTable table = this.getTable(tableId);
        table.setTableNumber(request.getNumber());
        table.setSeatNumber(request.getSeats());
        repository.save(table);
    }

    @Override
    public void delete(Integer tableId) {
        OrderTable table = this.getTable(tableId);
        repository.delete(table);
    }

    @Override
    public List<OrderTable> getAll() {
        return repository.findAll();
    }

    @Override
    public OrderTable getById(Integer tableId) {
        return this.getTable(tableId);
    }

    @Override
    public void updateStatus(Integer tableId, TableStatus status) {
        OrderTable table = this.getTable(tableId);
        table.setTableStatus(status);
        repository.save(table);
    }

    private OrderTable getTable(Integer tableId) {
        return repository.findById(tableId).orElseThrow(() -> new CustomException(APIStatus.ORDER_TABLE_NOT_FOUND));
    }
}
