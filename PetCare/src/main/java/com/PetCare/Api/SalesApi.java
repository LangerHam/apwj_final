package com.PetCare.Api;

import com.PetCare.DTO.SaleRequest;
import com.PetCare.Entity.Sale;
import com.PetCare.Entity.SaleItem;
import com.PetCare.Service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sales")
public class SalesApi {
    @Autowired
    private SaleService saleService;

    @PostMapping
    public ResponseEntity<?> createSale(@RequestBody SaleRequest saleRequest) {
        try {
            Sale sale = new Sale();
            sale.setCustomerId(saleRequest.getCustomerId());
            sale.setSaleDate(LocalDateTime.now());

            var total = saleRequest.getItems().stream()
                    .map(item -> item.getPrice().multiply(new java.math.BigDecimal(item.getQuantity())))
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            sale.setTotalAmount(total);

            List<SaleItem> saleItems = saleRequest.getItems().stream().map(dto -> {
                SaleItem item = new SaleItem();
                item.setProductId(dto.getProductId());
                item.setQuantity(dto.getQuantity());
                item.setUnitPrice(dto.getPrice());
                return item;
            }).collect(Collectors.toList());

            saleService.processSale(sale, saleItems);
            return ResponseEntity.ok("Sale processed successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
