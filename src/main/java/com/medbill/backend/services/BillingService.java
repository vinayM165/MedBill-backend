package com.medbill.backend.services;

import com.medbill.backend.dto.BillRequest;
import com.medbill.backend.models.Bill;
import com.medbill.backend.models.BillItem;
import com.medbill.backend.models.BillStatus;
import com.medbill.backend.models.BillType;
import com.medbill.backend.models.Medicine;
import com.medbill.backend.repositories.BillRepository;
import com.medbill.backend.repositories.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillRepository billRepository;
    private final MedicineRepository medicineRepository;
    private final MedicineService medicineService;

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public List<Bill> getOnlineOrders() {
        return billRepository.findAll().stream()
                .filter(b -> BillType.ONLINE_ORDER.name().equals(b.getType().name()))
                .toList();
    }

    @Transactional
    public Bill createBill(BillRequest request) {
        Bill bill = new Bill();
        bill.setCustomerName(request.getCustomerName());
        bill.setCustomerPhone(request.getCustomerPhone());
        
        // Handle flexible type mapping
        String typeStr = request.getType().toUpperCase();
        if (typeStr.equals("COUNTER")) typeStr = "COUNTER_BILL";
        if (typeStr.equals("ONLINE")) typeStr = "ONLINE_ORDER";
        
        bill.setType(BillType.valueOf(typeStr));
        bill.setPaymentMethod(request.getPaymentMethod());
        bill.setBillDate(LocalDateTime.now());
        
        // Handle Discount
        double discountPercent = request.getDiscountPercentage() != null ? request.getDiscountPercentage() : 0.0;
        bill.setDiscountPercentage(discountPercent);

        // Default logic
        if (bill.getType() == BillType.COUNTER_BILL) {
            bill.setStatus(BillStatus.COMPLETED);
        } else {
            bill.setStatus(BillStatus.PENDING);
        }

        double subTotal = 0.0;

        for (BillRequest.BillItemDto itemDto : request.getItems()) {
            Medicine medicine = medicineRepository.findById(itemDto.getMedicineId())
                    .orElseThrow(() -> new RuntimeException("Medicine not found with ID: " + itemDto.getMedicineId()));

            BillItem item = new BillItem();
            item.setMedicine(medicine);
            item.setQuantity(itemDto.getQuantity());
            item.setUnitPrice(medicine.getMrp());
            item.setTotalAmount(medicine.getMrp() * itemDto.getQuantity());

            bill.addItem(item);
            subTotal += item.getTotalAmount();

            if (bill.getStatus() == BillStatus.COMPLETED) {
                medicineService.updateStock(medicine.getId(), -itemDto.getQuantity());
            }
        }

        // Apply discount to subtotal
        double discountAmt = (subTotal * discountPercent) / 100.0;
        bill.setDiscountAmount(discountAmt);
        bill.setTotalAmount(subTotal - discountAmt);
        
        return billRepository.save(bill);
    }

    @Transactional
    public Bill updateOrderStatus(Long id, String status) {
        Bill bill = billRepository.findById(id).orElseThrow(() -> new RuntimeException("Bill/Order not found"));
        BillStatus newStatus = BillStatus.valueOf(status.toUpperCase());
        
        // If status changed to COMPLETED from PENDING, deduct stock
        if (bill.getStatus() == BillStatus.PENDING && newStatus == BillStatus.COMPLETED) {
            for(BillItem item : bill.getItems()) {
                medicineService.updateStock(item.getMedicine().getId(), -item.getQuantity());
            }
        }
        bill.setStatus(newStatus);
        return billRepository.save(bill);
    }
}
