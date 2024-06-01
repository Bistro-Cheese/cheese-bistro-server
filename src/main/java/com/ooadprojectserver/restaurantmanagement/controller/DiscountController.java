package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.DiscountRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.model.discount.Discount;
import com.ooadprojectserver.restaurantmanagement.service.discount.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@EnableCaching
@RequestMapping(APIConstant.DISCOUNTS)
public class DiscountController {
    private final DiscountService discountService;

    @PostMapping()
    public ResponseEntity<MessageResponse> createDiscount(
            @RequestBody DiscountRequest request
    ) {
        discountService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse(
                        MessageConstant.CREATE_DISCOUNT_SUCCESS
                )
        );
    }

    @GetMapping()
    @Cacheable(value = "discounts")
    public ResponseEntity<APIResponse<List<Discount>>> getAllDiscounts() {
        return ResponseEntity.ok(
                new APIResponse<>(
                        MessageConstant.GET_DISCOUNTS_SUCCESS,
                        discountService.getAll()
                )
        );
    }

    @GetMapping(APIConstant.ID)
    @Cacheable(value = "discounts", key = "#discountId")
    public ResponseEntity<APIResponse<Discount>> getDetailDiscount(
            @PathVariable("id") Integer discountId
    ) {
        return ResponseEntity.ok(
                new APIResponse<>(
                        MessageConstant.GET_DISCOUNT_BY_ID_SUCCESS,
                        discountService.getById(discountId)
                )
        );
    }

    @PutMapping(APIConstant.ID)
    public ResponseEntity<MessageResponse> updateDiscount(
            @PathVariable("id") Integer discountId,
            @RequestBody DiscountRequest request
    ) {
        discountService.update(discountId, request);
        return ResponseEntity.ok(
                new MessageResponse(
                        MessageConstant.UPDATE_DISCOUNT_SUCCESS
                )
        );
    }

    @DeleteMapping(APIConstant.ID)
    public ResponseEntity<MessageResponse> deleteDiscount(
            @PathVariable("id") Integer discountId
    ) {
        discountService.delete(discountId);
        return ResponseEntity.ok(
                new MessageResponse(
                        MessageConstant.DELETE_DISCOUNT_SUCCESS
                )
        );
    }
}
