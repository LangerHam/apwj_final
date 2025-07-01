package com.PetCare.Service;

import com.PetCare.Entity.Product;
import com.PetCare.Entity.Sale;
import com.PetCare.Entity.SaleItem;
import com.PetCare.Repository.ProductRepository;
import com.PetCare.Repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void processSale(Sale sale, java.util.List<SaleItem> items) {
        saleRepository.saveSale(sale);

        for (SaleItem item : items) {
            saleRepository.saveSaleItem(item);
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            int newStock = product.getStockQuantity() - item.getQuantity();
            if (newStock < 0) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }
            productRepository.updateStock(product.getProductId(), newStock);
        }
    }
}
