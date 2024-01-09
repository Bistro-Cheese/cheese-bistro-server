package com.ooadprojectserver.restaurantmanagement.service.operation;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.operation.OperationRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.inventory.Inventory;
import com.ooadprojectserver.restaurantmanagement.model.operation.Operation;
import com.ooadprojectserver.restaurantmanagement.model.operation.OperationType;
import com.ooadprojectserver.restaurantmanagement.repository.inventory.InventoryRepository;
import com.ooadprojectserver.restaurantmanagement.repository.operation.OperationRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService{

    private final InventoryRepository inventoryRepository;
    private final OperationRepository operationRepository;
    private final UserDetailService userDetailService;

    @Override
    public List<Operation> getAllOperation() {
        return operationRepository.findAll();
    }

    @Override
    public MessageResponse stockInventory(OperationRequest req) {
        return switch (req.getType().ordinal()) {
            case 0 -> importInventory(req.getInventoryId(), req.getQuantity());
            case 1 -> exportInventory(req.getInventoryId(), req.getQuantity());
            default -> new MessageResponse(MessageConstant.INVALID_OPERATION_TYPE);
        };
    }

    private MessageResponse importInventory(UUID inventoryId, Double quantity) {

        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(
                () -> new RuntimeException(MessageConstant.INVENTORY_NOT_FOUND)
        );
        inventory.importInventory(quantity);

        Operation operation = Operation.builder()
                .inventory(inventory)
                .quantity(quantity)
                .type(OperationType.IMPORT)
                .build();
        operation.setCommonCreate(userDetailService.getIdLogin());

        operationRepository.save(operation);
        inventoryRepository.save(inventory);

        return new MessageResponse(MessageConstant.IMPORT_INVENTORY_SUCCESSFULLY);
    }

    private MessageResponse exportInventory(UUID inventoryId, Double quantity) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(
                () -> new RuntimeException(MessageConstant.INVENTORY_NOT_FOUND)
        );
        if(!inventory.isEnough(quantity))
            throw new CustomException(APIStatus.INGREDIENT_NOT_ENOUGH);
        inventory.exportInventory(quantity);

        Operation operation = Operation.builder()
                .inventory(inventory)
                .quantity(quantity)
                .type(OperationType.EXPORT)
                .build();
        operation.setCommonCreate(userDetailService.getIdLogin());

        operationRepository.save(operation);
        inventoryRepository.save(inventory);

        return new MessageResponse(MessageConstant.EXPORT_INVENTORY_SUCCESSFULLY);
    }
}
