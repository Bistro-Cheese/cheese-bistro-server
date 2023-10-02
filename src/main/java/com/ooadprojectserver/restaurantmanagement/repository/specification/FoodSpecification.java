package com.ooadprojectserver.restaurantmanagement.repository.specification;

import com.ooadprojectserver.restaurantmanagement.constant.SortCase;
import com.ooadprojectserver.restaurantmanagement.model.Category;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;
import jakarta.persistence.criteria.*;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;


public class FoodSpecification implements Specification<Food> {

    private final String category;
    private final String searchKey;
    private BigDecimal minPrice;
    private final BigDecimal maxPrice;
//    private Integer bestSeller;
    private final int sortCase;
    private final boolean isAscSort;

    public FoodSpecification(String category, String searchKey, BigDecimal minPrice, BigDecimal maxPrice, int sortCase,
                             boolean isAscSort) {
        this.category = category;
        this.searchKey = searchKey;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.sortCase = sortCase;
        this.isAscSort = isAscSort;
    }


    @Override
    public Predicate toPredicate(Root<Food> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        List<Predicate> predicates = new LinkedList<>();


        //filter by status
//        predicates.add(cb.equal(root.get("status"), FoodStatus.AVAILABLE.getValue()));
        // filter by product name [search key]
        if (searchKey != null && !searchKey.trim().isEmpty()) {
            predicates.add(cb.like(root.<String>get("name"), "%" + searchKey.trim() + "%"));
        }

        if (category != null) {
            Root<Category> categoryRoot = cq.from(Category.class);
            predicates.add(cb.equal(categoryRoot.get("name"), category));
        }

        // validate price range
        if (minPrice==null){
            minPrice = BigDecimal.valueOf(0);
        }

        if (maxPrice == null){
            // price >= minPrice
            predicates.add(cb.greaterThanOrEqualTo(root.<BigDecimal>get("price"), minPrice));
        }else if (minPrice.compareTo(maxPrice) == 0){
            // price == minPrice
            predicates.add(cb.equal(root.get("sale"), minPrice));
        }else if (minPrice.compareTo(BigDecimal.valueOf(0)) > 0){
            // minPrice <= price <= maxPrice
            predicates.add(cb.between(root.<BigDecimal>get("price"), minPrice, maxPrice));
        }


        // sort
        Path orderClause = switch (sortCase) {
            case SortCase.SORT_BY_PRODUCT_NAME -> root.get("name");
            case SortCase.SORT_BY_PRODUCT_PRICE -> root.get("price");
            case SortCase.SORT_BY_PRODUCT_QUANTITY -> root.get("quantity");
            case SortCase.SORT_BY_PRODUCT_CREATE_DATE -> root.get("createdDate");
            default -> // sort by product name
                    root.get("name");
        };

        if (isAscSort) {
            cq.orderBy(cb.asc(orderClause));
        } else {
            cq.orderBy(cb.desc(orderClause));
        }


        return cb.and(predicates.toArray(new Predicate[]{}));
    }
}
