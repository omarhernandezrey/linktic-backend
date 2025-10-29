package com.linktic.inventory.service;

import com.linktic.inventory.model.Inventory;
import com.linktic.inventory.repo.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

class InventoryServiceTest {

    @Test
    void getQuantity_returnsZeroWhenNotFound(){
        InventoryRepository repo = Mockito.mock(InventoryRepository.class);
        Mockito.when(repo.findByProductId(anyLong())).thenReturn(Optional.empty());
        InventoryService svc = new InventoryService(repo);
        assertEquals(0L, svc.getQuantity(1L));
    }

    @Test
    void setQuantity_throwsOnNegative(){
        InventoryRepository repo = Mockito.mock(InventoryRepository.class);
        InventoryService svc = new InventoryService(repo);
        assertThrows(IllegalArgumentException.class, () -> svc.setQuantity(1L, -1));
    }
}
