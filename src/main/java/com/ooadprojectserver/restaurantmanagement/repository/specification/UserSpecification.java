package com.ooadprojectserver.restaurantmanagement.repository.specification;

import com.ooadprojectserver.restaurantmanagement.model.user.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class UserSpecification implements Specification<User> {
    private final String name;
    private final String role;
    private final String sort;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new LinkedList<>();
        List<Predicate> namePredicates = new LinkedList<>();

        boolean nameCondition = name != null && !name.trim().isEmpty();

        if (nameCondition) {
            namePredicates.add(criteriaBuilder.like(root.get("firstName"), "%" + name + "%"));
            namePredicates.add(criteriaBuilder.like(root.get("lastName"), "%" + name + "%"));
        }

        if (role != null) {
            predicates.add(criteriaBuilder.equal(root.get("role"),  role));
        }

        if (sort != null) {
            switch (sort) {
                case "asc":
                    query.orderBy(criteriaBuilder.asc(root.get("firstName")));
                    break;
                case "desc":
                    query.orderBy(criteriaBuilder.desc(root.get("firstName")));
                    break;
            }
        }

        if (nameCondition) {
            return criteriaBuilder.and(
                    criteriaBuilder.and(predicates.toArray(new Predicate[0])),
                    criteriaBuilder.or(namePredicates.toArray(new Predicate[0]))
            );
        } else {
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }
    }
}
