package com.ooadprojectserver.restaurantmanagement.service.specification;

import com.ooadprojectserver.restaurantmanagement.model.user.baseUser.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.LinkedList;
import java.util.List;

public class UserSpecification implements Specification<User> {
    private final String name;
    private final String role;
    private final String sort;

    public UserSpecification(List<?> params) {
        this.name = (String) params.get(0);
        this.role = (String) params.get(1);
        this.sort = (String) params.get(2);
    }

    @Override
    public Predicate toPredicate(@NonNull Root<User> root,@NonNull CriteriaQuery<?> query,@NonNull CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new LinkedList<>();
        List<Predicate> namePredicates = new LinkedList<>();

        if (isValid(name)) {
            namePredicates.add(criteriaBuilder.like(root.get("firstName"), "%" + name + "%"));
            namePredicates.add(criteriaBuilder.like(root.get("lastName"), "%" + name + "%"));
        }

        if (isValid(role)) {
            predicates.add(criteriaBuilder.equal(root.get("role"),  role));
        }

        if (isValid(sort)) {
            switch (sort) {
                case "asc":
                    query.orderBy(criteriaBuilder.asc(root.get("firstName")));
                    break;
                case "desc":
                    query.orderBy(criteriaBuilder.desc(root.get("firstName")));
                    break;
            }
        }

        if (isValid(name)) {
            return criteriaBuilder.and(
                    criteriaBuilder.and(predicates.toArray(new Predicate[0])),
                    criteriaBuilder.or(namePredicates.toArray(new Predicate[0]))
            );
        } else {
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }
    }

    private boolean isValid(String searchKey) {
        return searchKey != null && !searchKey.trim().isEmpty();
    }
}
