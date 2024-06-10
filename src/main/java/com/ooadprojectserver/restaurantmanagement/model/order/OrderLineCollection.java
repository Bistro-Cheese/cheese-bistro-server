package com.ooadprojectserver.restaurantmanagement.model.order;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderLineRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.order.iterator.OrderLineIterator;
import com.ooadprojectserver.restaurantmanagement.model.order.iterator.OrderLineRequestIteratorImpl;
import com.ooadprojectserver.restaurantmanagement.util.DataUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 6/10/2024, Monday
 * @description:
 **/
@Getter
public class OrderLineCollection<T> {
    private List<T> orderLines;

    public OrderLineCollection(){
        this.orderLines = new ArrayList<>();
    }

    public OrderLineCollection(List<T> orderLines){
        this.orderLines = orderLines;
        orderLines.iterator();
    }

    @SuppressWarnings("unchecked")
    public void addItem(OrderLine orderLine){
        if (orderLine.getQuantity() <= 0 || DataUtil.isNullObject(orderLine.getFood())){
            throw new CustomException(APIStatus.ORDER_LINE_INVALID_QUANTITY);
        } else {
            this.orderLines.add((T) orderLine);
        }
    }

    @SuppressWarnings("unchecked")
    public void addItem(OrderLineRequest request){
        if (request.getQuantity() <= 0){
            throw new CustomException(APIStatus.ORDER_LINE_NOT_FOUND);
        } else {
            this.orderLines.add((T) request);
        }
    }

    @SuppressWarnings("unchecked")
    public OrderLineIterator<OrderLineRequest> createOrderLineRequestIterator(){
        return new OrderLineRequestIteratorImpl((List<OrderLineRequest>) this.orderLines);
    }
}
