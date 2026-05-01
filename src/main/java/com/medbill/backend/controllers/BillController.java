package com.medbill.backend.controllers;

import com.medbill.backend.dto.BillRequest;
import com.medbill.backend.models.Bill;
import com.medbill.backend.services.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillingService billingService;

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        return ResponseEntity.ok(billingService.getAllBills());
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Bill>> getOnlineOrders() {
        return ResponseEntity.ok(billingService.getOnlineOrders());
    }

    @PostMapping
    public ResponseEntity<Bill> createBill(@RequestBody BillRequest request) {
        return ResponseEntity.ok(billingService.createBill(request));
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<Bill> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(billingService.updateOrderStatus(id, body.get("status")));
    }
}
