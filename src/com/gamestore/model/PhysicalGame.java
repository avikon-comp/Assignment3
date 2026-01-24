package com.gamestore.model;

public class PhysicalGame extends Game {
    private String platform;
    private int discCount;

    public PhysicalGame(int id, String title, double price, int releaseYear,
                        String platform, int discCount) {
        super(id, title, price, releaseYear);
        this.platform = platform;
        this.discCount = discCount;
    }

    @Override
    public String getGameType() {
        return "PHYSICAL";
    }

    @Override
    public String getPlatformDetails() {
        return "Platform: " + platform + ", Discs: " + discCount;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getDiscCount() {
        return discCount;
    }

    public void setDiscCount(int discCount) {
        if (discCount < 1) {
            throw new IllegalArgumentException("Disc count must be at least 1");
        }
        this.discCount = discCount;
    }

    @Override
    public String toString() {
        return super.getFullInfo() + ", Type: PHYSICAL, " + getPlatformDetails();
    }
}
