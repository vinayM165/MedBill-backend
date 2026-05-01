package com.medbill.backend.services;

import com.medbill.backend.dto.PurchaseRequest;
import com.medbill.backend.models.Dealer;
import com.medbill.backend.models.Medicine;
import com.medbill.backend.models.Purchase;
import com.medbill.backend.models.PurchaseItem;
import com.medbill.backend.repositories.DealerRepository;
import com.medbill.backend.repositories.MedicineRepository;
import com.medbill.backend.repositories.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final DealerRepository dealerRepository;
    private final MedicineRepository medicineRepository;
    private final MedicineService medicineService;

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    @Transactional
    public Purchase createPurchase(PurchaseRequest request) {
        Dealer dealer = dealerRepository.findById(request.getDealerId())
                .orElseThrow(() -> new RuntimeException("Dealer not found with ID: " + request.getDealerId()));

        Purchase purchase = new Purchase();
        purchase.setDealer(dealer);
        purchase.setInvoiceNo(request.getInvoiceNo());
        purchase.setPurchaseDate(request.getPurchaseDate());
        purchase.setStatus(request.getStatus());

        double totalAmount = 0.0;

        for (PurchaseRequest.PurchaseItemDto itemDto : request.getItems()) {
            Medicine medicine = medicineRepository.findById(itemDto.getMedicineId())
                    .orElseThrow(() -> new RuntimeException("Medicine not found with ID: " + itemDto.getMedicineId()));

            PurchaseItem item = new PurchaseItem();
            item.setMedicine(medicine);
            item.setQuantity(itemDto.getQuantity());
            item.setUnitPrice(itemDto.getUnitPrice());
            item.setTotalAmount(itemDto.getQuantity() * itemDto.getUnitPrice());

            purchase.addItem(item);
            totalAmount += item.getTotalAmount();

            // Automatically update stock balance
            medicineService.updateStock(medicine.getId(), item.getQuantity());
            
            // Automatically update medicine purchase price
            medicineService.updatePurchasePrice(medicine.getId(), item.getUnitPrice());
        }

        purchase.setTotalAmount(totalAmount);
        return purchaseRepository.save(purchase);
    }
}
