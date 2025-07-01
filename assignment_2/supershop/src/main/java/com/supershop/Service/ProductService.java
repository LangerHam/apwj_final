package com.supershop.Service;

import com.supershop.Entity.Product;
import com.supershop.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private static final BigDecimal expiryDiscountPercentage = new BigDecimal("0.20");

    public Product addProduct(Product product) {
        Long id = productRepository.save(product);
        product.setId(id);
        return product;
    }
    public void addProducts(List<Product> products) {
        productRepository.saveAll(products);
    }
    public Product updateProduct(Long id, Product productDetails) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        existingProduct.setName(productDetails.getName());
        existingProduct.setCategoryId(productDetails.getCategoryId());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setQuantity(productDetails.getQuantity());
        existingProduct.setExpiryDate(productDetails.getExpiryDate());
        existingProduct.setDiscount(productDetails.getDiscount());
        existingProduct.setAvailable(productDetails.isAvailable());

        productRepository.update(existingProduct);
        return existingProduct;
    }
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public List<Product> getProductsNearingExpiry() {
        LocalDate sevenDaysFromNow = LocalDate.now().plusDays(7);
        List<Product> expiringProducts = productRepository.findProductsExpiringBy(sevenDaysFromNow);

        for (Product product : expiringProducts) {
            product.setDiscount(expiryDiscountPercentage);
        }
        return expiringProducts;
    }
    public void updateStatusOfExpiredProducts() {
        List<Product> allProducts = productRepository.findAll();
        LocalDate today = LocalDate.now();
        for (Product product : allProducts) {
            if (product.getExpiryDate() != null && product.getExpiryDate().isBefore(today)) {
                if (product.isAvailable()) {
                    product.setAvailable(false);
                    productRepository.update(product);
                    System.out.println("Marked product as unavailable: " + product.getName());
                }
            }
        }
    }
}
