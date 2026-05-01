package com.medbill.backend.services;

import com.medbill.backend.models.Dealer;
import com.medbill.backend.repositories.DealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DealerService {

    private final DealerRepository dealerRepository;

    public List<Dealer> getAllDealers() {
        return dealerRepository.findAll();
    }

    public Dealer getDealerById(Long id) {
        return dealerRepository.findById(id).orElseThrow(() -> new RuntimeException("Dealer not found"));
    }

    public Dealer addDealer(Dealer dealer) {
        return dealerRepository.save(dealer);
    }

    public Dealer updateDealer(Long id, Dealer updatedDealer) {
        Dealer existing = dealerRepository.findById(id).orElseThrow(() -> new RuntimeException("Dealer not found"));
        existing.setName(updatedDealer.getName());
        existing.setContactPerson(updatedDealer.getContactPerson());
        existing.setPhone(updatedDealer.getPhone());
        existing.setEmail(updatedDealer.getEmail());
        existing.setAddress(updatedDealer.getAddress());
        existing.setGstin(updatedDealer.getGstin());
        existing.setPartyType(updatedDealer.getPartyType());
        existing.setAlternatePhone(updatedDealer.getAlternatePhone());
        return dealerRepository.save(existing);
    }

    public void deleteDealer(Long id) {
        dealerRepository.deleteById(id);
    }
}
