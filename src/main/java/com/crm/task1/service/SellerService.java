package com.crm.task1.service;

import com.crm.task1.model.Seller;
import com.crm.task1.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Seller getSellerById(Long id) {
        return sellerRepository.findById(id).orElseThrow(() -> new IllegalStateException("Seller not found"));
    }

    public Seller createSeller(Seller seller) {
        seller.setRegistrationDate(LocalDateTime.now());
        return sellerRepository.save(seller);
    }

    public Seller updateSeller(Long id, Seller sellerDetails) {
        Seller seller = getSellerById(id);
        seller.setName(sellerDetails.getName());
        seller.setContactInfo(sellerDetails.getContactInfo());
        return sellerRepository.save(seller);
    }

    public void deleteSeller(Long id) {
        sellerRepository.deleteById(id);
    }
}
