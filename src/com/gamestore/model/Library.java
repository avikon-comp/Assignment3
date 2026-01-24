package com.gamestore.model;

import java.time.LocalDate;
public class Library {
    private int id;
    private User user;
    private Game game;
    private LocalDate purchaseDate;

    public Library(int id, User user, Game game, LocalDate purchaseDate) {
        this.id = id;
        this.user = user;
        this.game = game;
        this.purchaseDate = purchaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        return "Library [id=" + id + ", user=" + user.getUsername() +
                ", game=" + game.getTitle() + ", purchaseDate=" + purchaseDate + "]";
    }
}
