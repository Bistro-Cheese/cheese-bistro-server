package com.ooadprojectserver.restaurantmanagement.model.order.iterator;

import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderLineRequest;
import com.ooadprojectserver.restaurantmanagement.util.DataUtil;

import java.util.List;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 6/10/2024, Monday
 * @description:
 **/
public class OrderLineRequestIteratorImpl implements OrderLineIterator<OrderLineRequest>{
    private List<OrderLineRequest> orderLineRequests;
    public OrderLineRequestIteratorImpl(List<OrderLineRequest> orderLineRequests){
        this.orderLineRequests = orderLineRequests;
    }

    private int current = 0;
    @Override
    public boolean hasNext() {
        return current < orderLineRequests.size() && DataUtil.isNullObject(orderLineRequests.get(current)) ;
    }

    @Override
    public OrderLineRequest next() {
        return orderLineRequests.get(current++);
    }
}
