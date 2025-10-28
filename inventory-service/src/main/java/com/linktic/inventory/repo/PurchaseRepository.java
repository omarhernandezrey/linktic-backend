package com.linktic.inventory.repo;

import com.linktic.inventory.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> { }
