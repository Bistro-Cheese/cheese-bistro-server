package com.ooadprojectserver.restaurantmanagement.service.operation;

import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.operation.OperationRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.model.inventory.Inventory;
import com.ooadprojectserver.restaurantmanagement.repository.inventory.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService{

    private final InventoryRepository inventoryRepository;

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

        inventoryRepository.save(inventory);

        return new MessageResponse(MessageConstant.IMPORT_INVENTORY_SUCCESSFULLY);
    }

    private MessageResponse exportInventory(UUID inventoryId, Double quantity) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(
                () -> new RuntimeException(MessageConstant.INVENTORY_NOT_FOUND)
        );
        if(!inventory.isEnough(quantity))
            return new MessageResponse(MessageConstant.NOT_ENOUGH_INVENTORY);
        inventory.exportInventory(quantity);

        inventoryRepository.save(inventory);

        return new MessageResponse(MessageConstant.EXPORT_INVENTORY_SUCCESSFULLY);
    }
}
