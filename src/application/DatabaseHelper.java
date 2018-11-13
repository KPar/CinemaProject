package application;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper {

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:CinemaDatabase.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);

            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Users ( " +
                    "userId INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, password TEXT, " +
                    "accountType INTEGER DEFAULT (0));";
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS MoviesPlaying ( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, movieId INTEGER REFERENCES Movies (movieId), " +
                    "showTimeHour INTEGER, showTimeMinute INTEGER, " +
                    "cinemaId INTEGER REFERENCES Cinemas (cinemaId));";
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS Movies ( " +
                    "movieId INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, " +
                    "rating  TEXT);";
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS Cinemas ( " +
                    "cinemaId  INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, " +
                    "locationX INTEGER, locationY INTEGER);";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


}
