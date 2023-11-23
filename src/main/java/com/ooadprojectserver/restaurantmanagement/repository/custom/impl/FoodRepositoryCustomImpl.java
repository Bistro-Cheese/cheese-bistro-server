package com.ooadprojectserver.restaurantmanagement.repository.custom.impl;

import com.ooadprojectserver.restaurantmanagement.dto.common.pagination.PageInfo;
import com.ooadprojectserver.restaurantmanagement.dto.request.food.FoodSearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.food.FoodResponse;
import com.ooadprojectserver.restaurantmanagement.repository.common.QueryRepo;
import com.ooadprojectserver.restaurantmanagement.repository.custom.FoodRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.isNullObject;
import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.toLikeConditional;

@RequiredArgsConstructor
@Repository
public class FoodRepositoryCustomImpl implements FoodRepositoryCustom {

    private final QueryRepo queryRepo;

    Logger logger = LoggerFactory.getLogger(FoodRepositoryCustomImpl.class);

    @Override
    public Page<FoodResponse> search(FoodSearchRequest request, PageInfo pageInfo) {

        String name = request.getName();
        String category = request.getCategory();
        String fromPrice = request.getFromPrice();
        String toPrice = request.getToPrice();
        String sortCase = request.getSortCase();
        Boolean isAscSort = request.getIsAscSort();

        String sqlCountAll = "select count(1) ";

        Map<String, Object> queryParams = new HashMap<>();

        String sqlGetData = "select f.id as id, f.name as name, f.description as description, f.price as price, f.image as image, f.status as status, c.name as category ";

        StringBuilder sqlConditional = new StringBuilder();
        sqlConditional.append(" from food f join category c on f.category_id = c.id ");

        if(!isNullObject(name)){
            sqlConditional.append("where f.name like :name ");
            queryParams.put("name", toLikeConditional(name));
        }

        if(!isNullObject(category)){
            sqlConditional.append(" and c.name like :category ");
            queryParams.put("category", toLikeConditional(category));
        }

        if(!isNullObject(fromPrice)){
            sqlConditional.append(" and f.price >= :fromPrice ");
            queryParams.put("fromPrice", fromPrice);
        }

        if(!isNullObject(toPrice)){
            sqlConditional.append(" and f.price <= :toPrice ");
            queryParams.put("toPrice", toPrice);
        }

        String sqlSort = null;

        if (!isNullObject(sortCase)){
            if(!isNullObject(isAscSort) && isAscSort){
                sqlSort = "order by " + sortCase  + " asc ";
            } else sqlSort = "order by " + sortCase  + " desc ";
        } else sqlSort = "order by f.name asc ";

        logger.info("sql Sort: {}", sqlSort);

        return queryRepo.queryPage(
                sqlCountAll, sqlGetData, sqlConditional.toString(), sqlSort,
                queryParams, FoodResponse.class, pageInfo
        );
    }
}
