package com.supershop.shop.Service;

import com.supershop.shop.Entity.Products;
import com.supershop.shop.Repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductService {
    private ProductRepository productsRepository;

    public void setProductRepository(ProductRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public void addProduct(Products products) {
        productsRepository.save(products);
    }

    public void addBatchProducts(List<Products> products) {
        productsRepository.saveAll(products);
    }

    public void editProduct(Products products) {
        productsRepository.update(products);
    }

    public List<Products> getExpiringProducts() {
        LocalDate sevenDaysFromNow = LocalDate.now().plusDays(7);
        return productsRepository.findExpire(sevenDaysFromNow);
    }

    public List<Products> getExpiringProductsWithDiscount(BigDecimal discountPercentage) {
        List<Products> expiringProducts = getExpiringProducts();
        return expiringProducts.stream()
                .map(product -> {
                    BigDecimal discountAmount = product.getPrice().multiply(discountPercentage).divide(BigDecimal.valueOf(100));
                    product.setPrice(product.getPrice().subtract(discountAmount));
                    return product;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getTotalPriceByCategory() {
        return productsRepository.totalPriceByCategory();
    }
}
