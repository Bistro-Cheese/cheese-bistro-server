package com.ooadprojectserver.restaurantmanagement.repository.common;

import com.ooadprojectserver.restaurantmanagement.dto.common.pagination.PageInfo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface QueryRepo {

    <T> T query(String sql, Map<String, Object> params, Class<T> classTarget);

    <T> List<T> queryList(String sql, Map<String, Object> params, Class<T> classTarget);

    <T> Page<T> queryPage(String sqlCountAll, String sqlGetData, String sqlConditional, String sqlSort,
                          Map<String, Object> params, Class<T> classTarget, PageInfo pageInfo);

    <T> Page<T> queryPage(String sqlCountAll, String sqlGetData, String sqlConditional, Map<String, Object> params,
                          Class<T> classTarget, PageInfo pageInfo);

    <T> Page<T> queryPage(String sql, Map<String, Object> params, Class<T> classTarget, PageInfo pageInfo);
}
