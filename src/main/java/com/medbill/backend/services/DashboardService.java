package com.medbill.backend.services;

import com.medbill.backend.models.BillStatus;
import com.medbill.backend.repositories.BillRepository;
import com.medbill.backend.repositories.DealerRepository;
import com.medbill.backend.repositories.MedicineRepository;
import com.medbill.backend.repositories.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final BillRepository billRepository;
    private final MedicineRepository medicineRepository;
    private final DealerRepository dealerRepository;
    private final PurchaseRepository purchaseRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();

        // Fetch sum of COMPLETED bills today
        Double todaySales = billRepository.getTotalSalesByStatusBetweenDates(startOfDay, endOfDay, BillStatus.COMPLETED);
        stats.put("todaySales", todaySales != null ? todaySales : 0.0);
        
        // Fetch sum of all purchases this month
        Double monthPurchase = purchaseRepository.getTotalPurchasesBetweenDates(LocalDate.now().withDayOfMonth(1), LocalDate.now());
        stats.put("monthPurchase", monthPurchase != null ? monthPurchase : 0.0);
        
        stats.put("totalMedicines", medicineRepository.count());
        stats.put("lowStockCount", medicineRepository.countByStockQuantityLessThan(10));
        stats.put("totalDealers", dealerRepository.count());
        
        // Add pending orders count
        long pendingOrdersCount = billRepository.findAll().stream()
            .filter(b -> b.getStatus() == BillStatus.PENDING || b.getStatus() == BillStatus.NEW_ORDER)
            .count();
        stats.put("pendingOrdersCount", pendingOrdersCount);
        
        return stats;
    }
}
