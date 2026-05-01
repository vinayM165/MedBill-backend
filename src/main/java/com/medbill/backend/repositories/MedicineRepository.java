package com.medbill.backend.repositories;

import com.medbill.backend.models.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByNameContainingIgnoreCase(String name);
    List<Medicine> findByStockQuantityLessThan(Integer quantity);
    long countByStockQuantityLessThan(Integer threshold);
}
