CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) NOT NULL
);

CREATE TABLE games (
                       id SERIAL PRIMARY KEY,
                       title VARCHAR(100) NOT NULL,
                       price DECIMAL(10, 2) CHECK (price >= 0),
                       release_year INT,
                       game_type VARCHAR(20) NOT NULL CHECK (game_type IN ('DIGITAL', 'PHYSICAL')),
                       platform VARCHAR(50),
                       download_size VARCHAR(20),
                       disc_count INT DEFAULT 1
);

CREATE TABLE library (
                         id SERIAL PRIMARY KEY,
                         user_id INT NOT NULL,
                         game_id INT NOT NULL,
                         purchase_date DATE NOT NULL DEFAULT CURRENT_DATE,
                         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                         FOREIGN KEY (game_id) REFERENCES games(id) ON DELETE CASCADE,
                         UNIQUE (user_id, game_id)
);

INSERT INTO users (username, email) VALUES
                                        ('alice_gamer', 'alice@example.com'),
                                        ('bob_player', 'bob@example.com'),
                                        ('charlie_stream', 'charlie@example.com');

INSERT INTO games (title, price, release_year, game_type, platform, download_size, disc_count) VALUES
                                                                                                   ('Cyberpunk 2077', 39.99, 2020, 'DIGITAL', 'Steam', '70 GB', NULL),
                                                                                                   ('The Witcher 3', 29.99, 2015, 'DIGITAL', 'GOG', '50 GB', NULL),
                                                                                                   ('Elden Ring', 59.99, 2022, 'PHYSICAL', 'PS5', NULL, 1),
                                                                                                   ('Zelda: Tears of the Kingdom', 69.99, 2023, 'PHYSICAL', 'Nintendo Switch', NULL, 1),
                                                                                                   ('Half-Life 3', 0.00, 2026, 'DIGITAL', 'Steam', '30 GB', NULL);

INSERT INTO library (user_id, game_id, purchase_date) VALUES
                                                          (1, 1, '2023-01-15'),
                                                          (1, 3, '2023-02-20'),
                                                          (2, 2, '2023-03-10'),
                                                          (2, 4, '2023-04-05'),
                                                          (3, 5, '2026-01-01');