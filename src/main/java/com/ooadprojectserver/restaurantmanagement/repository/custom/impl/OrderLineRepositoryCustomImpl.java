package com.ooadprojectserver.restaurantmanagement.repository.custom.impl;

import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderLineSearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderLineResponse;
import com.ooadprojectserver.restaurantmanagement.repository.common.QueryRepo;
import com.ooadprojectserver.restaurantmanagement.repository.custom.OrderLineRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.isNullObject;

@RequiredArgsConstructor
public class OrderLineRepositoryCustomImpl implements OrderLineRepositoryCustom {

    private final QueryRepo queryRepo;

    @Override
    public List<OrderLineResponse> search(OrderLineSearchRequest request) {
        UUID orderId = request.getOrderId();

        Map<String, Object> queryParams = new HashMap<>();

        String sqlGetData = "select ol.id as id, f.name as name, f.image as image, f.price as price, ol.quantity as quantity, ol.food_id as food_id";

        StringBuilder sqlConditional = new StringBuilder();
        sqlConditional.append(" from order_line ol join food f on ol.food_id = f.id  ");

        if(!isNullObject(orderId)){
            sqlConditional.append("where ol.order_id = :orderId ");
            queryParams.put("orderId", orderId);
        }

        return queryRepo.queryList(sqlGetData + sqlConditional.toString(), queryParams, OrderLineResponse.class);
    }
}
