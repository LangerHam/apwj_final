package com.supershop.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal price;
    private Integer quantity;
    private LocalDate expiryDate;
    private BigDecimal discount;
    private boolean available;
}
