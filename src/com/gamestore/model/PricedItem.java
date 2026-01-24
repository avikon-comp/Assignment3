package com.gamestore.model;

public interface PricedItem {
    double getPrice();
    void applyDiscount(double percentage);
}
