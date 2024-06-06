package com.ooadprojectserver.restaurantmanagement.repository.custom.impl;

import com.ooadprojectserver.restaurantmanagement.dto.response.bill.BillResponse;
import com.ooadprojectserver.restaurantmanagement.repository.common.QueryRepo;
import com.ooadprojectserver.restaurantmanagement.repository.custom.BillRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class BillRepositoryCustomImpl implements BillRepositoryCustom {
    private final QueryRepo queryRepo;

    @Override
    public List<BillResponse> getBillByOrderId(UUID orderId) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("orderId", orderId);

        String sqlGetData = "select b.id as id, " +
                "ro.id as orderId, " +
                "c.cus_nme as customerName, " +
                "ro.number_of_customer as numberOfCustomer, " +
                "rt.tab_num as tableNumber, " +
                "p.type as paymentType, " +
                "d.type as discountType, " +
                "ro.sub_total as subTotal, " +
                "d.value as discountValue, " +
                "d.type as discountType, " +
                "ro.total as total, " +
                "ro.deposit as deposit, " +
                "b.paid as paid, " +
                "b.change_paid as changePaid, " +
                "b.cus_in as cusIn, " +
                "b.cus_out as cusOut";

        StringBuilder sqlConditional = new StringBuilder();
        sqlConditional.append(" from bill b join res_order ro on b.order_id = ro.id" +
                " join res_table rt on ro.table_id = rt.id" +
                " join customer c on ro.cus_id = c.id" +
                " left join discount d on ro.discount_id = d.id" +
                " join payment p on b.pay_id = p.id" +
                " where ro.id = :orderId");

        return queryRepo.queryList(sqlGetData + sqlConditional.toString(), queryParams, BillResponse.class);
    }
}
