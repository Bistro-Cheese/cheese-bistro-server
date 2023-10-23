package com.ooadprojectserver.restaurantmanagement.controller.ingredient;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.IngredientRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.model.composition.ingredient.Ingredient;
import com.ooadprojectserver.restaurantmanagement.service.ingredient.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(APIConstant.INGREDIENT)
@PreAuthorize("hasRole('OWNER')")
public class IngredientController {
    private final IngredientService ingredientService;

    @GetMapping()
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER')")
    public ResponseEntity<APIResponse<List<Ingredient>>> getAllIngredients() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_INGREDIENT_SUCCESS,
                        ingredientService.getAllIngredients()
                )
        );
    }

    @PostMapping()
    public ResponseEntity<MessageResponse> createIngredient(@RequestBody IngredientRequest request) {
        ingredientService.createIngredient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse(MessageConstant.CREATE_INGREDIENT_SUCCESS)
        );
    }

    @PutMapping(APIConstant.INGREDIENT_ID)
    public ResponseEntity<MessageResponse> updateIngredient(
            @PathVariable("ingredient_id") Long ingredientId,
            @RequestBody IngredientRequest request
    ) {
        ingredientService.updateIngredient(ingredientId, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.UPDATE_INGREDIENT_SUCCESS)
        );
    }

    @DeleteMapping(APIConstant.INGREDIENT_ID)
    public ResponseEntity<MessageResponse> deleteIngredient(
            @PathVariable("ingredient_id") Long ingredientId
    ) {
        ingredientService.deleteIngredient(ingredientId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.DELETE_INGREDIENT_SUCCESS)
        );
    }
}
