package com.medbill.backend.services;

import com.medbill.backend.models.Medicine;
import com.medbill.backend.repositories.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;

    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    public List<Medicine> searchMedicines(String query) {
        return medicineRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Medicine> getLowStockMedicines(Integer threshold) {
        return medicineRepository.findByStockQuantityLessThan(threshold);
    }

    public Medicine addMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    public Medicine updateMedicine(Long id, Medicine updated) {
        Medicine existing = medicineRepository.findById(id).orElseThrow(() -> new RuntimeException("Medicine not found"));
        existing.setName(updated.getName());
        existing.setGenericName(updated.getGenericName());
        existing.setManufacturer(updated.getManufacturer());
        existing.setBatchNo(updated.getBatchNo());
        existing.setExpiryDate(updated.getExpiryDate());
        existing.setPurchasePrice(updated.getPurchasePrice());
        existing.setMrp(updated.getMrp());
        existing.setStockQuantity(updated.getStockQuantity());
        existing.setRackNumber(updated.getRackNumber());
        existing.setHsnCode(updated.getHsnCode());
        existing.setPackSize(updated.getPackSize());
        existing.setNoOfStrips(updated.getNoOfStrips());
        existing.setTabletsPerStrip(updated.getTabletsPerStrip());
        existing.setTotalTablets(updated.getTotalTablets());
        return medicineRepository.save(existing);
    }

    @Transactional
    public void updateStock(Long medicineId, Integer quantityChange) {
        Medicine medicine = getMedicineById(medicineId);
        int currentStock = medicine.getStockQuantity() != null ? medicine.getStockQuantity() : 0;
        int newStock = currentStock + quantityChange;
        
        if (newStock < 0) {
            throw new RuntimeException("Insufficient stock for medicine: " + medicine.getName() + 
                ". Current stock: " + currentStock + ", requested change: " + quantityChange);
        }
        
        medicine.setStockQuantity(newStock);
        medicineRepository.save(medicine);
    }

    @Transactional
    public void updatePurchasePrice(Long medicineId, Double newPrice) {
        Medicine medicine = getMedicineById(medicineId);
        medicine.setPurchasePrice(newPrice);
        medicineRepository.save(medicine);
    }

    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id).orElseThrow(() -> new RuntimeException("Medicine not found with ID: " + id));
    }

    public void deleteMedicine(Long id) {
        medicineRepository.deleteById(id);
    }
}
