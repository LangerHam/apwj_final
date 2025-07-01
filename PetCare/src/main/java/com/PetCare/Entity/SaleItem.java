package com.PetCare.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("sale_items")
public class SaleItem {
    private Integer saleId;
    private Integer productId;
    private int quantity;
    private BigDecimal unitPrice;
}
