package com.ooadprojectserver.restaurantmanagement.repository.custom.impl;

import com.ooadprojectserver.restaurantmanagement.dto.common.pagination.PageInfo;
import com.ooadprojectserver.restaurantmanagement.dto.request.inventory.InventorySearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.inventory.InventoryResponse;
import com.ooadprojectserver.restaurantmanagement.repository.common.QueryRepo;
import com.ooadprojectserver.restaurantmanagement.repository.custom.InventoryRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.isNullObject;
import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.toLikeConditional;

@RequiredArgsConstructor
@Repository
public class InventoryRepositoryCustomImpl implements InventoryRepositoryCustom {

    private final QueryRepo queryRepo;

    @Override
    public Page<InventoryResponse> search(InventorySearchRequest request, PageInfo pageInfo) {

        String name = request.getName();
        String supplier = request.getSupplier();

        Map<String, Object> queryParams = new HashMap<>();

        String sqlCountAll = "select count(1) ";

        //Đặt tên cột trong sql phải giống với tên thuộc tính trong class response
        String sqlGetData = "select i.id as id, ig.name as ingredientName, ig.supplier as supplier, i.total_quan as totalQuantity, ig.unit as unit ";

        StringBuilder sqlConditional = new StringBuilder();
        sqlConditional.append("from inventory i join ingredient ig on i.ingredient_id = ig.id ");

        if (!isNullObject(name)) {
            sqlConditional.append("where ig.name like :name ");
            queryParams.put("name", toLikeConditional(name));
        }

        if (!isNullObject(supplier)) {
            sqlConditional.append(" and ig.supplier like :supplier ");
            queryParams.put("supplier", toLikeConditional(supplier));
        }

        String sqlSort = "order by ig.name asc ";

        return queryRepo.queryPage(
                sqlCountAll, sqlGetData, sqlConditional.toString(), sqlSort,
                queryParams, InventoryResponse.class, pageInfo
        );
    }
}
