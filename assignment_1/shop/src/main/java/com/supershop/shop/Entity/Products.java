package com.supershop.shop.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Products {
    private int id;
    private String name;
    private category category;
    private BigDecimal price;
    private LocalDate expiration_date;

    public Products() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public category getCategory() {
        return category;
    }

    public LocalDate getExpiration_date() {
        return expiration_date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(category category) {
        this.category = category;
    }

    public void setExpiration_date(LocalDate expiration_date) {
        this.expiration_date = expiration_date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
