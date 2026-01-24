package com.gamestore.service;

import com.gamestore.model.Library;
import com.gamestore.model.User;
import com.gamestore.model.Game;
import com.gamestore.repository.LibraryRepository;
import com.gamestore.repository.UserRepository;
import com.gamestore.repository.GameRepository;
import com.gamestore.exception.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LibraryService {
    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public LibraryService() {
        this.libraryRepository = new LibraryRepository();
        this.userRepository = new UserRepository();
        this.gameRepository = new GameRepository();
    }

    public void addGameToLibrary(int userId, int gameId) throws ResourceNotFoundException, InvalidInputException, DatabaseOperationException {
        try {
            User user = userRepository.getById(userId);
            if (user == null) {
                throw new ResourceNotFoundException("User with id " + userId + " not found");
            }

            Game game = gameRepository.getById(gameId);
            if (game == null) {
                throw new ResourceNotFoundException("Game with id " + gameId + " not found");
            }

            List<Library> userLibrary = libraryRepository.getByUserId(userId);
            for (Library lib : userLibrary) {
                if (lib.getGame().getId() == gameId) {
                    throw new InvalidInputException("User already owns this game");
                }
            }

            Library library = new Library(0, user, game, LocalDate.now());
            libraryRepository.create(library);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while adding game to library", e);
        }
    }

    public List<Library> getUserLibrary(int userId) throws ResourceNotFoundException, DatabaseOperationException {
        try {
            User user = userRepository.getById(userId);
            if (user == null) {
                throw new ResourceNotFoundException("User with id " + userId + " not found");
            }
            return libraryRepository.getByUserId(userId);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while fetching user library", e);
        }
    }

    public void removeGameFromLibrary(int userId, int gameId) throws ResourceNotFoundException, InvalidInputException, DatabaseOperationException {
        try {
            List<Library> userLibrary = libraryRepository.getByUserId(userId);
            Library libraryToRemove = null;

            for (Library lib : userLibrary) {
                if (lib.getGame().getId() == gameId) {
                    libraryToRemove = lib;
                    break;
                }
            }

            if (libraryToRemove == null) {
                throw new InvalidInputException("User does not own this game");
            }

            boolean deleted = libraryRepository.delete(libraryToRemove.getId());
            if (!deleted) {
                throw new DatabaseOperationException("Failed to remove game from library");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while removing game from library", e);
        }
    }

    public List<Library> getAllLibraryEntries() throws DatabaseOperationException {
        try {
            return libraryRepository.getAll();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while fetching library entries", e);
        }
    }

    public List<User> getUsersWhoOwnGame(int gameId) throws DatabaseOperationException {
        try {
            List<Library> allEntries = libraryRepository.getAll();
            return allEntries.stream()
                    .filter(lib -> lib.getGame().getId() == gameId)
                    .map(Library::getUser)
                    .distinct()
                    .toList();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while fetching game owners", e);
        }
    }

    public List<Game> getUserGames(int userId) throws ResourceNotFoundException, DatabaseOperationException {
        try {
            User user = userRepository.getById(userId);
            if (user == null) {
                throw new ResourceNotFoundException("User with id " + userId + " not found");
            }
            List<Library> userLibrary = libraryRepository.getByUserId(userId);
            return userLibrary.stream()
                    .map(Library::getGame)
                    .toList();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while fetching user games", e);
        }
    }


    public boolean userOwnsGame(int userId, int gameId) throws DatabaseOperationException {
        try {
            List<Library> userLibrary = libraryRepository.getByUserId(userId);
            return userLibrary.stream()
                    .anyMatch(lib -> lib.getGame().getId() == gameId);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while checking ownership", e);
        }
    }
}