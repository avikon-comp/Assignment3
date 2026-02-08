import com.gamestore.model.*;
import com.gamestore.service.*;
import com.gamestore.exception.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static GameService gameService = new GameService();
    private static UserService userService = new UserService();
    private static LibraryService libraryService = new LibraryService();

    public static void main(String[] args) {
        System.out.println("Добро пожаловать в Game Store API! \n");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = getIntInput("Выберите действие: ");

            try {
                switch (choice) {
                    case 1 -> showAllGames();
                    case 2 -> addNewGame();
                    case 3 -> updateGame();
                    case 4 -> deleteGame();
                    case 5 -> showAllUsers();
                    case 6 -> addNewUser();
                    case 7 -> addGameToLibrary();
                    case 8 -> showUserLibrary();
                    case 9 -> demonstratePolymorphism();
                    case 10 -> demonstrateInterfaces();
                    case 11 -> testValidationErrors();
                    case 0 -> {
                        System.out.println("Выход...");
                        running = false;
                    }
                    default -> System.out.println("Неверный выбор!");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }

            if (running) {
                System.out.println("\nНажмите Enter чтобы продолжить...");
                scanner.nextLine();
            }
        }
        scanner.close();

    }

    private static void printMenu() {
        System.out.println("=".repeat(40));
        System.out.println("1. Показать все игры");
        System.out.println("2. Добавить новую игру");
        System.out.println("3. Обновить игру");
        System.out.println("4. Удалить игру");
        System.out.println("5. Показать всех пользователей");
        System.out.println("6. Добавить нового пользователя");
        System.out.println("7. Добавить игру в библиотеку пользователя");
        System.out.println("8. Показать библиотеку пользователя");
        System.out.println("9. Полиморфизм");
        System.out.println("10. Интерфейсы (скидка)");
        System.out.println("11. Ошибки валидации");
        System.out.println("0. Выход");
        System.out.println("=".repeat(40));
    }


    private static void showAllGames() throws Exception {
        System.out.println("\nВсе игры:");
        List<Game> games = gameService.getAllGames();
        System.out.println("\n=== LAMBDA SORTING BY PRICE ===");
        games.sort((g1, g2) -> Double.compare(g1.getPrice(), g2.getPrice()));
        for (Game game : games) {
            System.out.println("   " + game);
        }
        System.out.println("Всего игр: " + games.size());
    }

    private static void addNewGame() throws Exception {
        System.out.println("\nДобавить новую игру:");
        System.out.println("1. Цифровая игра");
        System.out.println("2. Физическая игра");
        int type = getIntInput("Тип: ");

        String title = getStringInput("Название: ");
        double price = getDoubleInput("Цена: ");
        int year = getIntInput("Год выпуска: ");
        String platform = getStringInput("Платформа (Steam/PS5/Xbox): ");

        if (type == 1) {
            String size = getStringInput("Размер загрузки (например, '50 GB'): ");
            DigitalGame game = new DigitalGame(0, title, price, year, platform, size);
            gameService.createGame(game);
            System.out.println("Цифровая игра создана с ID: " + game.getId());
        } else {
            int discs = getIntInput("Количество дисков: ");
            PhysicalGame game = new PhysicalGame(0, title, price, year, platform, discs);
            gameService.createGame(game);
            System.out.println("Физическая игра создана с ID: " + game.getId());
        }
    }

    private static void updateGame() throws Exception {
        int id = getIntInput("ID игры для обновления: ");
        Game game = gameService.getGameById(id);

        System.out.println("Текущая игра: " + game);
        String newTitle = getStringInput("Новое название (Enter чтобы оставить '" + game.getTitle() + "'): ");
        if (!newTitle.isEmpty()) game.setTitle(newTitle);

        double newPrice = getDoubleInput("Новая цена (Enter чтобы оставить " + game.getPrice() + "): ");
        if (newPrice > 0) game.setPrice(newPrice);

        gameService.updateGame(id, game);
        System.out.println("Игра обновлена!");
    }

    private static void deleteGame() throws Exception {
        int id = getIntInput("ID игры для удаления: ");
        gameService.deleteGame(id);
        System.out.println("Игра удалена!");
    }

    private static void showAllUsers() throws Exception {
        System.out.println("\nВсе пользователи:");
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            System.out.println("   " + user);
        }
    }

    private static void addNewUser() throws Exception {
        String username = getStringInput("Имя пользователя: ");
        String email = getStringInput("Email: ");

        User user = new User(0, username, email);
        userService.createUser(user);
        System.out.println("Пользователь создан с ID: " + user.getId());
    }

    private static void addGameToLibrary() throws Exception {
        int userId = getIntInput("ID пользователя: ");
        int gameId = getIntInput("ID игры: ");

        libraryService.addGameToLibrary(userId, gameId);
        System.out.println(" Игра добавлена в библиотеку!");
    }

    private static void showUserLibrary() throws Exception {
        int userId = getIntInput("ID пользователя: ");
        List<Library> library = libraryService.getUserLibrary(userId);

        if (library.isEmpty()) {
            System.out.println("Библиотека пуста");
        } else {
            System.out.println("Библиотека пользователя:");
            for (Library lib : library) {
                System.out.println("   - " + lib.getGame().getTitle() +
                        " (куплена: " + lib.getPurchaseDate() + ")");
            }
        }
    }

    private static void demonstratePolymorphism() {
        System.out.println("\nПолиморфизм:");

        Game digitalGame = new DigitalGame(0, "Test Digital", 29.99, 2023, "Steam", "20 GB");
        Game physicalGame = new PhysicalGame(0, "Test Physical", 39.99, 2023, "PS5", 2);

        Game[] games = {digitalGame, physicalGame};

        for (Game game : games) {
            System.out.println("Игра: " + game.getTitle());
            System.out.println("  Тип: " + game.getGameType()); // Полиморфизм!
            System.out.println("  Платформа: " + game.getPlatformDetails());
            System.out.println();
        }
    }

    private static void demonstrateInterfaces() {
        System.out.println("\nДемонстрация интерфейсов:");

        DigitalGame game = new DigitalGame(0, "Discount Game", 100.0, 2024, "Steam", "30 GB");
        System.out.println("Игра: " + game.getTitle());
        System.out.println("Цена до скидки: $" + game.getPrice());

        game.applyDiscount(25);
        System.out.println("Цена после 25% скидки: $" + game.getPrice());

        System.out.println("Проверка валидности: " + (game.validate() ? " Валидна" : "Невалидна"));
    }

    private static void testValidationErrors() {
        System.out.println("\nТест ошибок валидации:");

        try {
            User badUser = new User(0, "test", "not-an-email");
            userService.createUser(badUser);
        } catch (Exception e) {
            System.out.println("1. Поймана ошибка валидации email: " + e.getMessage());
        }

        try {
            DigitalGame badGame = new DigitalGame(0, "Bad Game", -10.0, 2024, "Steam", "10 GB");
            badGame.setPrice(-5.0);
        } catch (Exception e) {
            System.out.println("2. Поймана ошибка отрицательной цены: " + e.getMessage());
        }

        System.out.println(" Все ошибки обработаны корректно!");
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Введите число: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }

    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Введите число: ");
            scanner.next();
        }
        double value = scanner.nextDouble();
        scanner.nextLine();
        return value;
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }




}