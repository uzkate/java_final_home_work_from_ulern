
import java.sql.*;
import java.util.*;

class Game {
    private int rank;
    private String name;
    private String platform;
    private int year;
    private String genre;
    private String publisher;
    private double naSales;
    private double euSales;
    private double jpSales;
    private double otherSales;
    private double globalSales;

    public Game(int rank, String name, String platform, int year, String genre, String publisher,
                double naSales, double euSales, double jpSales, double otherSales, double globalSales) {
        this.rank = rank;
        this.name = name;
        this.platform = platform;
        this.year = year;
        this.genre = genre;
        this.publisher = publisher;
        this.naSales = naSales;
        this.euSales = euSales;
        this.jpSales = jpSales;
        this.otherSales = otherSales;
        this.globalSales = globalSales;
    }

    public int getRank() { return rank; }
    public String getName() { return name; }
    public String getPlatform() { return platform; }
    public int getYear() { return year; }
    public String getGenre() { return genre; }
    public String getPublisher() { return publisher; }
    public double getNaSales() { return naSales; }
    public double getEuSales() { return euSales; }
    public double getJpSales() { return jpSales; }
    public double getOtherSales() { return otherSales; }
    public double getGlobalSales() { return globalSales; }
}

class GameDataHandler {
    private List<Game> games;

    public GameDataHandler() {
        games = new ArrayList<>();
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public List<Game> getGames() {
        return games;
    }

    public List<Game> filterByYear(int startYear, int endYear) {
        List<Game> filtered = new ArrayList<>();
        for (Game game : games) {
            if (game.getYear() >= startYear && game.getYear() <= endYear) {
                filtered.add(game);
            }
        }
        return filtered;
    }

    public Game getHighestSalesGame(String region) {
        Game highest = null;
        double maxSales = 0;
        for (Game game : games) {
            double sales = 0;
            switch (region.toLowerCase()) {
                case "na":
                    sales = game.getNaSales();
                    break;
                case "eu":
                    sales = game.getEuSales();
                    break;
                case "jp":
                    sales = game.getJpSales();
                    break;
                case "global":
                    sales = game.getGlobalSales();
                    break;
            }
            if (sales > maxSales) {
                maxSales = sales;
                highest = game;
            }
        }
        return highest;
    }
}

public class Main {
    private static final String DATABASE_URL = "jdbc:sqlite:game_sales.db";

    public static void main(String[] args) {
        try {
            // Initialize the GameDataHandler
            GameDataHandler handler = new GameDataHandler();

            // Example: Adding games to the handler
            handler.addGame(new Game(1, "Wii Sports", "Wii", 2006, "Sports", "Nintendo", 41.49, 29.02, 3.77, 8.46, 82.74));
            handler.addGame(new Game(2, "Super Mario Bros.", "NES", 1985, "Platform", "Nintendo", 29.08, 3.58, 6.81, 0.77, 40.24));

            // Connect to SQLite database
            Connection connection = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connected to SQLite database!");

            // Create table
            createTable(connection);

            // Insert data into the database
            for (Game game : handler.getGames()) {
                insertGame(connection, game);
            }

            // Query for the game with the highest global sales
            queryHighestSalesGame(connection, "global");

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS games (" +
                "rank INTEGER PRIMARY KEY," +
                "name TEXT," +
                "platform TEXT," +
                "year INTEGER," +
                "genre TEXT," +
                "publisher TEXT," +
                "na_sales REAL," +
                "eu_sales REAL," +
                "jp_sales REAL," +
                "other_sales REAL," +
                "global_sales REAL);";
        Statement statement = connection.createStatement();
        statement.execute(createTableSQL);
        System.out.println("Table 'games' created successfully.");
    }

    private static void insertGame(Connection connection, Game game) throws SQLException {
        String insertSQL = "INSERT INTO games (rank, name, platform, year, genre, publisher, na_sales, eu_sales, jp_sales, other_sales, global_sales) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
        preparedStatement.setInt(1, game.getRank());
        preparedStatement.setString(2, game.getName());
        preparedStatement.setString(3, game.getPlatform());
        preparedStatement.setInt(4, game.getYear());
        preparedStatement.setString(5, game.getGenre());
        preparedStatement.setString(6, game.getPublisher());
        preparedStatement.setDouble(7, game.getNaSales());
        preparedStatement.setDouble(8, game.getEuSales());
        preparedStatement.setDouble(9, game.getJpSales());
        preparedStatement.setDouble(10, game.getOtherSales());
        preparedStatement.setDouble(11, game.getGlobalSales());
        preparedStatement.executeUpdate();
        System.out.println("Inserted game: " + game.getName());
    }

    private static void queryHighestSalesGame(Connection connection, String region) throws SQLException {
        String column = switch (region.toLowerCase()) {
            case "na" -> "na_sales";
            case "eu" -> "eu_sales";
            case "jp" -> "jp_sales";
            default -> "global_sales";
        };
        String querySQL = "SELECT name, " + column + " FROM games ORDER BY " + column + " DESC LIMIT 1;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(querySQL);
        if (resultSet.next()) {
            System.out.println("Highest " + region.toUpperCase() + " Sales Game: " + resultSet.getString("name") +
                               " with sales: " + resultSet.getDouble(column));
        }
    }
}
