package com.PetCare.Service;

import com.PetCare.Entity.Product;
import com.PetCare.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EmailService emailService;

    private static final int LOW_STOCK_THRESHOLD = 10;

    public void checkLowStockAndAlert() {
        List<Product> allProducts = productRepository.findAll();
        for (Product product : allProducts) {
            if (product.getStockQuantity() < LOW_STOCK_THRESHOLD) {
                //System.out.println("LOW STOCK ALERT: " + product.getName() + " has only " + product.getStockQuantity() + " items left.");
                emailService.sendSimpleMessage("niloygomes088@gmail.com", "Low Stock Alert", "Product " + product.getName() + " is running low.");
            }
        }
    }
}
