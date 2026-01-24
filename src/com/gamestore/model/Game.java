package com.gamestore.model;

public abstract class Game implements Validatable, PricedItem {
    private int id;
    private String title;
    private double price;
    private int releaseYear;

    public Game(int id, String title, double price, int releaseYear) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.releaseYear = releaseYear;
    }

    public abstract String getGameType();
    public abstract String getPlatformDetails();

    public String getFullInfo() {
        return "ID: " + id + ", Title: " + title + ", Price: $" + price + ", Year: " + releaseYear;
    }

    @Override
    public boolean validate() {
        if (title == null || title.trim().isEmpty()) return false;
        if (price < 0) return false;
        if (releaseYear < 1950 || releaseYear > 2100) return false;
        return true;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void applyDiscount(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Discount must be 0-100%");
        }
        this.price = this.price * (1 - percentage / 100);
        this.price = Math.round(this.price * 100.0) / 100.0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) {
        if (releaseYear < 1950 || releaseYear > 2100) {
            throw new IllegalArgumentException("Invalid release year");
        }
        this.releaseYear = releaseYear;
    }
}