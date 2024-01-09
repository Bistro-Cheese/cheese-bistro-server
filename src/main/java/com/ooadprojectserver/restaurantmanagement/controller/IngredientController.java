package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.ingredient.IngredientRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.model.ingredient.Ingredient;
import com.ooadprojectserver.restaurantmanagement.service.ingredient.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(APIConstant.INGREDIENT)
public class IngredientController {
    private final IngredientService ingredientService;

    @GetMapping()
    public ResponseEntity<APIResponse<List<Ingredient>>> getAllIngredients() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_INGREDIENT_SUCCESS,
                        ingredientService.getAll()
                )
        );
    }

    @PostMapping()
    public ResponseEntity<MessageResponse> createIngredient(@RequestBody IngredientRequest request) {
        ingredientService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse(MessageConstant.CREATE_INGREDIENT_SUCCESS)
        );
    }

    @GetMapping("/{ingredient_name}")
    public ResponseEntity<APIResponse<Ingredient>> getIngredientByName(
            @PathVariable("ingredient_name") String ingredientName
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_INGREDIENT_SUCCESS,
                        ingredientService.getByName(ingredientName)
                )
        );
    }

    @PutMapping(APIConstant.INGREDIENT_ID)
    public ResponseEntity<MessageResponse> updateIngredient(
            @PathVariable("ingredient_id") Long ingredientId,
            @RequestBody IngredientRequest request
    ) {
        ingredientService.update(ingredientId, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.UPDATE_INGREDIENT_SUCCESS)
        );
    }

    @DeleteMapping(APIConstant.INGREDIENT_ID)
    public ResponseEntity<MessageResponse> deleteIngredient(
            @PathVariable("ingredient_id") Long ingredientId
    ) {
        ingredientService.delete(ingredientId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.DELETE_INGREDIENT_SUCCESS)
        );
    }
}
