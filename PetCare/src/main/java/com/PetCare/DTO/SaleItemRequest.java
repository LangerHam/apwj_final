package com.PetCare.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleItemRequest {
    private int productId;
    private int quantity;
    private BigDecimal price;
}
