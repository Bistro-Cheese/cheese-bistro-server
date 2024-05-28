package com.ooadprojectserver.restaurantmanagement.repository.custom.impl;

import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderSearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderSearchResponse;
import com.ooadprojectserver.restaurantmanagement.repository.common.QueryRepo;
import com.ooadprojectserver.restaurantmanagement.repository.custom.OrderRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.isNullObject;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final QueryRepo queryRepo;
    Logger logger = LoggerFactory.getLogger(OrderRepositoryCustomImpl.class);

    @Override
    public List<OrderSearchResponse> search(OrderSearchRequest request) {

        Integer status = request.getStatus();

        logger.info("Order Search Status: " + status);

        Map<String, Object> queryParams = new HashMap<>();

        String sqlGetData = "select ro.id as id, " +
                "rt.tab_num as tableNumber, " +
                "rt.id as tableId, " +
                "ro.number_of_customer as numberOfCustomer, " +
                "ro.deposit as deposit, " +
                "ro.sub_total as subTotal, " +
                "ro.total as total, " +
                "ro.status as status, " +
                "ro.cus_in as cusIn, " +
                "ro.discount_id as discount, " +
                "d.type as discountType, " +
                "d.value as discountValue ";


        StringBuilder sqlConditional = new StringBuilder();
        sqlConditional.append(" from res_order ro join res_table rt on ro.table_id = rt.id" +
                " left join discount d on ro.discount_id = d.id");


        if(!isNullObject(status)){
            sqlConditional.append("where ro.status = :status ");
            queryParams.put("status", status);
        }

        return queryRepo.queryList(sqlGetData + sqlConditional.toString(), queryParams, OrderSearchResponse.class);
    }
}
