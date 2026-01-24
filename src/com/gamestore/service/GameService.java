package com.gamestore.service;

import com.gamestore.model.Game;
import com.gamestore.model.DigitalGame;
import com.gamestore.model.PhysicalGame;
import com.gamestore.repository.GameRepository;
import com.gamestore.exception.*;
import java.sql.SQLException;
import java.util.List;

public class GameService {
    private final GameRepository gameRepository;

    public GameService() {
        this.gameRepository = new GameRepository();
    }

    public void createGame(Game game) throws InvalidInputException, DuplicateResourceException, DatabaseOperationException {
        try {
            if (!game.validate()) {
                throw new InvalidInputException("Invalid game data");
            }

            List<Game> allGames = gameRepository.getAll();
            for (Game g : allGames) {
                if (g.getTitle().equalsIgnoreCase(game.getTitle()) &&
                        g.getReleaseYear() == game.getReleaseYear()) {
                    throw new DuplicateResourceException("Game with same title and year already exists");
                }
            }

            gameRepository.create(game);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while creating game", e);
        }
    }

    public List<Game> getAllGames() throws DatabaseOperationException {
        try {
            return gameRepository.getAll();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while fetching games", e);
        }
    }

    public Game getGameById(int id) throws ResourceNotFoundException, DatabaseOperationException {
        try {
            Game game = gameRepository.getById(id);
            if (game == null) {
                throw new ResourceNotFoundException("Game with id " + id + " not found");
            }
            return game;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while fetching game", e);
        }
    }

    public void updateGame(int id, Game game) throws ResourceNotFoundException, InvalidInputException, DatabaseOperationException {
        try {
            Game existingGame = gameRepository.getById(id);
            if (existingGame == null) {
                throw new ResourceNotFoundException("Cannot update: game with id " + id + " not found");
            }

            if (!game.validate()) {
                throw new InvalidInputException("Invalid game data");
            }

            boolean updated = gameRepository.update(id, game);
            if (!updated) {
                throw new DatabaseOperationException("Failed to update game");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while updating game", e);
        }
    }

    public void deleteGame(int id) throws ResourceNotFoundException, DatabaseOperationException {
        try {
            Game game = gameRepository.getById(id);
            if (game == null) {
                throw new ResourceNotFoundException("Cannot delete: game with id " + id + " not found");
            }

            boolean deleted = gameRepository.delete(id);
            if (!deleted) {
                throw new DatabaseOperationException("Failed to delete game");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while deleting game", e);
        }
    }

    public void applyDiscountToType(String gameType, double discountPercent) throws DatabaseOperationException {
        try {
            List<Game> allGames = gameRepository.getAll();
            for (Game game : allGames) {
                if (game.getGameType().equalsIgnoreCase(gameType)) {
                    game.applyDiscount(discountPercent);
                    gameRepository.update(game.getId(), game);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while applying discount", e);
        }
    }

    public List<Game> getGamesByPlatform(String platform) throws DatabaseOperationException {
        try {
            List<Game> allGames = gameRepository.getAll();
            return allGames.stream()
                    .filter(g -> {
                        if (g instanceof DigitalGame) {
                            return ((DigitalGame) g).getPlatform().equalsIgnoreCase(platform);
                        } else if (g instanceof PhysicalGame) {
                            return ((PhysicalGame) g).getPlatform().equalsIgnoreCase(platform);
                        }
                        return false;
                    })
                    .toList();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while fetching games by platform", e);
        }
    }
}