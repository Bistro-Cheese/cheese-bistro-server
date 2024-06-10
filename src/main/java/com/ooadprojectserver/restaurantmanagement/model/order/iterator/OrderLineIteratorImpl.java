package com.ooadprojectserver.restaurantmanagement.model.order.iterator;

import com.ooadprojectserver.restaurantmanagement.model.order.OrderLine;

import java.util.List;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 6/10/2024, Monday
 * @description:
 **/
public class OrderLineIteratorImpl implements OrderLineIterator<OrderLine>{
    private List<OrderLine> orderLines;

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public OrderLine next() {
        return null;
    }
}
