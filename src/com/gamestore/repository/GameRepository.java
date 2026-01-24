package com.gamestore.repository;

import com.gamestore.database.DatabaseConnection;
import com.gamestore.model.Game;
import com.gamestore.model.DigitalGame;
import com.gamestore.model.PhysicalGame;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameRepository {
    public void create(Game game) throws SQLException {
        String sql = "INSERT INTO games (title, price, release_year, game_type, platform, download_size, disc_count) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, game.getTitle());
            stmt.setDouble(2, game.getPrice());
            stmt.setInt(3, game.getReleaseYear());

            if (game instanceof DigitalGame) {
                DigitalGame dg = (DigitalGame) game;
                stmt.setString(4, "DIGITAL");
                stmt.setString(5, dg.getPlatform());
                stmt.setString(6, dg.getDownloadSize());
                stmt.setNull(7, Types.INTEGER);
            } else if (game instanceof PhysicalGame) {
                PhysicalGame pg = (PhysicalGame) game;
                stmt.setString(4, "PHYSICAL");
                stmt.setString(5, pg.getPlatform());
                stmt.setNull(6, Types.VARCHAR);
                stmt.setInt(7, pg.getDiscCount());
            }

            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    game.setId(rs.getInt(1));
                }
            }
        }
    }
    public List<Game> getAll() throws SQLException {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM games";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Game game = mapResultSetToGame(rs);
                games.add(game);
            }
        }
        return games;
    }

    public Game getById(int id) throws SQLException {
        String sql = "SELECT * FROM games WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGame(rs);
                }
            }
        }
        return null;
    }

    public boolean update(int id, Game game) throws SQLException {
        String sql = "UPDATE games SET title = ?, price = ?, release_year = ?, " +
                "platform = ?, download_size = ?, disc_count = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, game.getTitle());
            stmt.setDouble(2, game.getPrice());
            stmt.setInt(3, game.getReleaseYear());

            if (game instanceof DigitalGame) {
                DigitalGame dg = (DigitalGame) game;
                stmt.setString(4, dg.getPlatform());
                stmt.setString(5, dg.getDownloadSize());
                stmt.setNull(6, Types.INTEGER);
            } else if (game instanceof PhysicalGame) {
                PhysicalGame pg = (PhysicalGame) game;
                stmt.setString(4, pg.getPlatform());
                stmt.setNull(5, Types.VARCHAR);
                stmt.setInt(6, pg.getDiscCount());
            }

            stmt.setInt(7, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM games WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Game mapResultSetToGame(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        double price = rs.getDouble("price");
        int releaseYear = rs.getInt("release_year");
        String gameType = rs.getString("game_type");
        String platform = rs.getString("platform");

        if ("DIGITAL".equals(gameType)) {
            String downloadSize = rs.getString("download_size");
            return new DigitalGame(id, title, price, releaseYear, platform, downloadSize);
        } else {
            int discCount = rs.getInt("disc_count");
            return new PhysicalGame(id, title, price, releaseYear, platform, discCount);
        }
    }
}
