package com.gamestore.service;

import com.gamestore.model.User;
import com.gamestore.repository.UserRepository;
import com.gamestore.exception.*;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public void createUser(User user) throws InvalidInputException, DuplicateResourceException, DatabaseOperationException {
        try {
            validateUser(user);

            User existingUser = userRepository.getByUsername(user.getUsername());
            if (existingUser != null) {
                throw new DuplicateResourceException("Username '" + user.getUsername() + "' already exists");
            }

            userRepository.create(user);
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while creating user", e);
        }
    }

    public List<User> getAllUsers() throws DatabaseOperationException {
        try {
            return userRepository.getAll();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while fetching users", e);
        }
    }

    public User getUserById(int id) throws ResourceNotFoundException, DatabaseOperationException {
        try {
            User user = userRepository.getById(id);
            if (user == null) {
                throw new ResourceNotFoundException("User with id " + id + " not found");
            }
            return user;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while fetching user", e);
        }
    }

    public void updateUser(int id, User user) throws ResourceNotFoundException, InvalidInputException, DuplicateResourceException, DatabaseOperationException {
        try {
            User existingUser = userRepository.getById(id);
            if (existingUser == null) {
                throw new ResourceNotFoundException("Cannot update: user with id " + id + " not found");
            }

            validateUser(user);

            if (!existingUser.getUsername().equals(user.getUsername())) {
                User userWithSameUsername = userRepository.getByUsername(user.getUsername());
                if (userWithSameUsername != null) {
                    throw new DuplicateResourceException("Username '" + user.getUsername() + "' already taken");
                }
            }

            boolean updated = userRepository.update(id, user);
            if (!updated) {
                throw new DatabaseOperationException("Failed to update user");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while updating user", e);
        }
    }

    public void deleteUser(int id) throws ResourceNotFoundException, DatabaseOperationException {
        try {
            User user = userRepository.getById(id);
            if (user == null) {
                throw new ResourceNotFoundException("Cannot delete: user with id " + id + " not found");
            }

            boolean deleted = userRepository.delete(id);
            if (!deleted) {
                throw new DatabaseOperationException("Failed to delete user");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while deleting user", e);
        }
    }

    private void validateUser(User user) throws InvalidInputException {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new InvalidInputException("Username cannot be empty");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new InvalidInputException("Invalid email format");
        }
        if (user.getUsername().length() < 3 || user.getUsername().length() > 50) {
            throw new InvalidInputException("Username must be 3-50 characters");
        }
    }

    public User getUserByEmail(String email) throws DatabaseOperationException {
        try {
            List<User> allUsers = userRepository.getAll();
            for (User user : allUsers) {
                if (user.getEmail().equalsIgnoreCase(email)) {
                    return user;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Database error while finding user by email", e);
        }
    }
}