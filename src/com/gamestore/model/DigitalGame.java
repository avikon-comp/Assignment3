package com.gamestore.model;

public class DigitalGame extends Game {
    private String platform;
    private String downloadSize;

    public DigitalGame(int id, String title, double price, int releaseYear,
                       String platform, String downloadSize) {
        super(id, title, price, releaseYear);
        this.platform = platform;
        this.downloadSize = downloadSize;
    }

    @Override
    public String getGameType() {
        return "DIGITAL";
    }

    @Override
    public String getPlatformDetails() {
        return "Platform: " + platform + ", Download Size: " + downloadSize;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(String downloadSize) {
        this.downloadSize = downloadSize;
    }

    @Override
    public String toString() {
        return super.getFullInfo() + ", Type: DIGITAL, " + getPlatformDetails();
    }
}
