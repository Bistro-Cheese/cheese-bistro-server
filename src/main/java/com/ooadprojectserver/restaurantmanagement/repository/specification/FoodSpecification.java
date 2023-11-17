package com.ooadprojectserver.restaurantmanagement.repository.specification;

import com.ooadprojectserver.restaurantmanagement.constant.SortCase;
import com.ooadprojectserver.restaurantmanagement.model.food.Category;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;
import jakarta.persistence.criteria.*;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.LinkedList;
import java.util.List;


public class FoodSpecification implements Specification<Food> {
    private final String category;
    private final String searchKey;
    private final String minPrice;
    private final String maxPrice;
    private final String sortCase;
    private final String isAscSort;

    public FoodSpecification(List<?> params) {
        this.category = (String) params.get(0);
        this.searchKey = (String) params.get(1);
        this.minPrice = (String) params.get(2);
        this.maxPrice = (String) params.get(3);
        this.sortCase = (String) params.get(4);
        this.isAscSort = (String) params.get(5);
    }

    @Override
    public Predicate toPredicate(@NonNull Root<Food> root,@NonNull CriteriaQuery<?> cq,@NonNull CriteriaBuilder cb) {

        List<Predicate> predicates = new LinkedList<>();

        if (isValid(searchKey)) {
            predicates.add(cb.like(root.get("name"), "%" + searchKey.trim() + "%"));
        }

        if (isValid(category)) {
            Join<Category, Food> foodCategory = root.join("category");
            predicates.add(cb.equal(foodCategory.get("name"), category));
        }

        // validate price range
        if (isValid(minPrice)) {
            // price >= minPrice
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), Long.valueOf(minPrice)));
        }

        if (isValid(maxPrice)) {
            // price <= maxPrice
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), Long.valueOf(maxPrice)));
        }

        // sort
        Expression<?> orderClause = switch (Integer.parseInt(sortCase)) {
            case SortCase.SORT_BY_PRODUCT_PRICE -> root.get("price");
            case SortCase.SORT_BY_PRODUCT_QUANTITY -> root.get("quantity");
            case SortCase.SORT_BY_PRODUCT_CREATE_DATE -> root.get("createdDate");
            default -> root.get("name");
        };

        if (isValid(isAscSort)) {
            cq.orderBy(cb.asc(orderClause));
        } else {
            cq.orderBy(cb.desc(orderClause));
        }

        return cb.and(predicates.toArray(new Predicate[]{}));
    }

    private boolean isValid(String searchKey) {
        return searchKey != null && !searchKey.trim().isEmpty();
    }
}
