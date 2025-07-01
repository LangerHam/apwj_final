package com.PetCare.Service;

import org.springframework.stereotype.Service;

@Service
public class LoyaltyService {
    public int getCustomerPoints(int customerId) {
        System.out.println("Fetching loyalty points for customer " + customerId);
        return 100;
    }
}
