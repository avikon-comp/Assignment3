package com.gamestore.repository;

import com.gamestore.database.DatabaseConnection;
import com.gamestore.model.Library;
import com.gamestore.model.User;
import com.gamestore.model.Game;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibraryRepository {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public LibraryRepository() {
        this.userRepository = new UserRepository();
        this.gameRepository = new GameRepository();
    }

    public void create(Library library) throws SQLException {
        String sql = "INSERT INTO library (user_id, game_id, purchase_date) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, library.getUser().getId());
            stmt.setInt(2, library.getGame().getId());
            stmt.setDate(3, Date.valueOf(library.getPurchaseDate()));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    library.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Library> getAll() throws SQLException {
        List<Library> libraries = new ArrayList<>();
        String sql = "SELECT * FROM library";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                libraries.add(mapResultSetToLibrary(rs));
            }
        }
        return libraries;
    }

    public Library getById(int id) throws SQLException {
        String sql = "SELECT * FROM library WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLibrary(rs);
                }
            }
        }
        return null;
    }

    public List<Library> getByUserId(int userId) throws SQLException {
        List<Library> libraries = new ArrayList<>();
        String sql = "SELECT * FROM library WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libraries.add(mapResultSetToLibrary(rs));
                }
            }
        }
        return libraries;
    }

    public boolean update(int id, Library library) throws SQLException {
        String sql = "UPDATE library SET user_id = ?, game_id = ?, purchase_date = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, library.getUser().getId());
            stmt.setInt(2, library.getGame().getId());
            stmt.setDate(3, Date.valueOf(library.getPurchaseDate()));
            stmt.setInt(4, id);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM library WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Library mapResultSetToLibrary(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        int gameId = rs.getInt("game_id");
        LocalDate purchaseDate = rs.getDate("purchase_date").toLocalDate();

        User user = userRepository.getById(userId);
        Game game = gameRepository.getById(gameId);

        return new Library(id, user, game, purchaseDate);
    }
}
