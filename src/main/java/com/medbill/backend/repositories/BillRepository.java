package com.medbill.backend.repositories;

import com.medbill.backend.models.Bill;
import com.medbill.backend.models.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query("SELECT SUM(b.totalAmount) FROM Bill b WHERE b.billDate >= :startDate AND b.billDate <= :endDate AND b.status = :status")
    Double getTotalSalesByStatusBetweenDates(LocalDateTime startDate, LocalDateTime endDate, BillStatus status);
}
