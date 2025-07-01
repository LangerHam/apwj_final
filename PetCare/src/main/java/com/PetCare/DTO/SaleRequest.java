package com.PetCare.DTO;

import lombok.Data;

import java.util.List;

@Data
public class SaleRequest {
    private int customerId;
    private List<SaleItemRequest> items;
}

