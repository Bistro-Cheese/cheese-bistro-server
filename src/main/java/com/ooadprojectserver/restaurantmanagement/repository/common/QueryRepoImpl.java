package com.ooadprojectserver.restaurantmanagement.repository.common;


import com.ooadprojectserver.restaurantmanagement.dto.common.pagination.PageInfo;
import com.ooadprojectserver.restaurantmanagement.exception.AppException;
import com.ooadprojectserver.restaurantmanagement.util.DataUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ooadprojectserver.restaurantmanagement.constant.APIStatus.ERROR_GET_TOTAL_ITEM_FAILED;
import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.*;
import static com.ooadprojectserver.restaurantmanagement.util.DateTimeUtils.safeToDate;

@Repository
@RequiredArgsConstructor
@Slf4j
public class QueryRepoImpl implements QueryRepo {
    private final EntityManager em;

    Logger logger = LoggerFactory.getLogger(QueryRepoImpl.class);

    @Override
    public <T> T query(String sql, Map<String, Object> params, @NotNull Class<T> classTarget) {
        DataUtil.DataTypeClassName targetFieldClassName = DataUtil.DataTypeClassName.get(classTarget.getName());
        if (isNullObject(targetFieldClassName)) {
            Query query = em.createNativeQuery(sql, Tuple.class);
            if (!isNullOrEmpty(params)) params.forEach(query::setParameter);

            List<Tuple> queryResult = query.getResultList();

            if (isNullOrEmpty(queryResult)) return null;

            return tupleToObject(queryResult.get(0), classTarget);
        }

        Query query = em.createNativeQuery(sql);
        if (!isNullOrEmpty(params)) params.forEach(query::setParameter);

        List<Object> queryResult = query.getResultList();

        if (isNullOrEmpty(queryResult)) return null;
        Object firstItem = queryResult.get(0);

        switch (targetFieldClassName) {
            case STRING:
                return (T) safeToString(firstItem);
            case LONG:
            case PRIMITIVE_LONG:
                return (T) safeToLong(firstItem);
            case DOUBLE:
            case PRIMITIVE_DOUBLE:
                return (T) safeToDouble(firstItem);
            case BOOLEAN:
            case PRIMITIVE:
                return (T) safeToBoolean(safeToString(firstItem));
            case DATE:
                return (T) safeToDate(firstItem);
            case BIG_DECIMAL:
                return (T) safeToBigDecimal(firstItem);
            case INTEGER:
            case INT:
                return (T) safeToInt(firstItem);
            default:
                return null;
        }
    }

    @Override
    public <T> List<T> queryList(String sql, Map<String, Object> params, Class<T> classTarget) {
        DataUtil.DataTypeClassName targetFieldClassName = DataUtil.DataTypeClassName.get(classTarget.getName());
        if (isNullObject(targetFieldClassName)) {
            Query query = em.createNativeQuery(sql, Tuple.class);
            if (!isNullOrEmpty(params)) params.forEach(query::setParameter);

            List<Tuple> queryResult = query.getResultList();
            logger.info("queryResult: {}", queryResult);

            return tupleToObject(queryResult, classTarget);
        }

        Query query = em.createNativeQuery(sql);
        if (!isNullOrEmpty(params)) params.forEach(query::setParameter);

        List<Object> queryResult = query.getResultList();

        if (isNullOrEmpty(queryResult)) return Collections.emptyList();
        Object firstItem = queryResult.get(0);

        switch (targetFieldClassName) {
            case STRING:
                return queryResult.stream().map(item -> (T) safeToInt(item)).collect(Collectors.toList());
            case LONG:
            case PRIMITIVE_LONG:
                return queryResult.stream().map(item -> (T) safeToLong(item)).collect(Collectors.toList());
            case DOUBLE:
            case PRIMITIVE_DOUBLE:
                return queryResult.stream().map(item -> (T) safeToDouble(item)).collect(Collectors.toList());
            case BOOLEAN:
            case PRIMITIVE:
                return queryResult.stream().map(item -> (T) safeToBoolean(safeToString(firstItem))).collect(Collectors.toList());
            case DATE:
                return queryResult.stream().map(item -> (T) safeToDate(firstItem)).collect(Collectors.toList());
            case BIG_DECIMAL:
                return queryResult.stream().map(item -> (T) safeToBigDecimal(firstItem)).collect(Collectors.toList());
            case INTEGER:
            case INT:
                return queryResult.stream().map(item -> (T) safeToInt(firstItem)).collect(Collectors.toList());
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public <T> Page<T> queryPage(
            String sqlCountAll, String sqlGetData, String sqlConditional, String sqlSort,
            Map<String, Object> params, Class<T> classTarget, PageInfo pageInfo
    ) {
        return this.queryPage(
                sqlCountAll, sqlGetData, sqlConditional, sqlSort,
                params, classTarget, PageRequest.of(pageInfo.getCurrentPage(), pageInfo.getPageSize())
        );
    }

    @Override
    public <T> Page<T> queryPage(String sqlCountAll, String sqlGetData, String sqlConditional,
                                 Map<String, Object> params, Class<T> classTarget, PageInfo pageInfo) {
        return this.queryPage(sqlCountAll, sqlGetData, sqlConditional, params, classTarget,
                PageRequest.of(pageInfo.getCurrentPage(), pageInfo.getPageSize()));
    }

    @Override
    public <T> Page<T> queryPage(String sql, Map<String, Object> params, Class<T> classTarget, PageInfo pageInfo) {
        return this.queryPage(sql, params, classTarget, PageRequest.of(pageInfo.getCurrentPage(), pageInfo.getPageSize()));
    }

    private <T> Page<T> queryPage(String sqlCountAll, String sqlGetData, String sqlConditional,
                                  Map<String, Object> params, Class<T> classTarget, Pageable pageable) {
        int maxResults = pageable.getPageSize();
        int firstResult = maxResults * pageable.getPageNumber();

        Query query = em.createNativeQuery(sqlGetData + sqlConditional, Tuple.class);
        query.setMaxResults(maxResults);
        query.setFirstResult(firstResult);

        Query queryCountAll = em.createNativeQuery(sqlCountAll + sqlConditional);
        if (!isNullOrEmpty(params)) params.forEach((key, value) -> {
            query.setParameter(key, value);
            queryCountAll.setParameter(key, value);
        });

        List<Tuple> queryResult = query.getResultList();
        List<T> listData = tupleToObject(queryResult, classTarget);

        Object countAll = queryCountAll.getSingleResult();

        return new PageImpl<>(listData, pageable, safeToLong(countAll));
    }

    private <T> Page<T> queryPage(String sql, Map<String, Object> params, Class<T> classTarget, Pageable pageable) {
        int maxResults = pageable.getPageSize();
        int firstResult = maxResults * pageable.getPageNumber();

        Query query = em.createNativeQuery(sql, Tuple.class);
        query.setMaxResults(maxResults);
        query.setFirstResult(firstResult);
        if (!isNullOrEmpty(params)) params.forEach(query::setParameter);

        List<Tuple> queryResult = query.getResultList();
        List<T> listData = tupleToObject(queryResult, classTarget);

        int totalItem = 0;
        if (!isNullOrEmpty(listData)) {
            try {
                T firstItem = listData.get(0);

                Field field = firstItem.getClass().getDeclaredField("totalItem");
                field.setAccessible(true);
                totalItem = safeToInt(field.get(firstItem));
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
                throw new AppException(ERROR_GET_TOTAL_ITEM_FAILED.getMessage());
            }
        }

        return new PageImpl<>(listData, pageable, totalItem);
    }

    private <T> Page<T> queryPage(
            String sqlCountAll, String sqlGetData, String sqlConditional, String sqlSort,
            Map<String, Object> params, Class<T> classTarget, Pageable pageable
    ) {
        int maxResults = pageable.getPageSize();
        int firstResult = maxResults * pageable.getPageNumber();

        Query query = em.createNativeQuery( sqlGetData + sqlConditional + sqlSort, Tuple.class);
        query.setMaxResults(maxResults);
        query.setFirstResult(firstResult);

        Query queryCountAll = em.createNativeQuery(sqlCountAll + sqlConditional);
        if (!isNullOrEmpty(params)) {
            var paramQueryCountAll = queryCountAll.getParameters().stream().map(d -> d.getName()).collect(Collectors.toSet());
            params.forEach((key, value) -> {
                query.setParameter(key, value);
                if (paramQueryCountAll.contains(key))
                    queryCountAll.setParameter(key, value);
            });
        }

        List<Tuple> queryResult = query.getResultList();
        List<T> listData = tupleToObject(queryResult, classTarget);
        log.info("listData: {}", listData);

        Object countAll = queryCountAll.getSingleResult();

        return new PageImpl<>(listData, pageable, safeToLong(countAll));
    }
}
