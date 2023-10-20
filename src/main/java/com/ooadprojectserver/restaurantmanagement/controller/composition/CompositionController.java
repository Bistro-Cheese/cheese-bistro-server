package com.ooadprojectserver.restaurantmanagement.controller.composition;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.composition.CompositionRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.model.composition.Composition;
import com.ooadprojectserver.restaurantmanagement.service.food.CompositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping(APIConstant.COMPOSITION)
@PreAuthorize("hasRole('OWNER')")
public class CompositionController {
    private final CompositionService compositionService;

    @GetMapping()
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER')")
    public ResponseEntity<APIResponse<List<Composition>>> getAllCompositions() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_COMPOSITION_SUCCESS,
                        compositionService.getAllCompositions()
                )
        );
    }

    @PostMapping(APIConstant.FOOD_ID)
    public ResponseEntity<MessageResponse> createComposition(
            @PathVariable("food_id") UUID foodId,
            @RequestBody CompositionRequest compositionRequest
    ) {
        compositionService.createComposition(foodId, compositionRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.CREATE_COMPOSITION_SUCCESS)
        );
    }

    @DeleteMapping(APIConstant.COMPOSITION_ID)
    public ResponseEntity<MessageResponse> deleteComposition(
            @PathVariable("composition_id") UUID compositionId
    ) {
        compositionService.deleteComposition(compositionId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.DELETE_COMPOSITION_SUCCESS)
        );
    }

    @PutMapping(APIConstant.FOOD_ID)
    public ResponseEntity<MessageResponse> updateComposition(
            @PathVariable("food_id") UUID foodId,
            @RequestBody CompositionRequest compositionRequest
    ) {
        compositionService.updateComposition(foodId, compositionRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.UPDATE_COMPOSITION_SUCCESS)
        );
    }
}
