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
            String sql = "CREATE TABLE IF NOT EXISTS MoviesPlaying ( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, movieId INTEGER REFERENCES Movies (movieId), " +
                    "showtimeHour INTEGER, showtimeMinute INTEGER, showtimeAMPM TEXT, " +
                    "cinemaId INTEGER REFERENCES Cinemas (cinemaId));";
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS Movies ( " +
                    "movieId INTEGER PRIMARY KEY AUTOINCREMENT, movieTitle TEXT, " +
                    "rating  TEXT);";
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS Cinemas ( " +
                    "cinemaId  INTEGER PRIMARY KEY AUTOINCREMENT, cinemaName TEXT, " +
                    "locationX INTEGER, locationY INTEGER);";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public List getMovies(String rating){
        String sql;
        switch (rating){
            case "G":
                sql = "SELECT * FROM Movies WHERE rating = 'G'";
                break;
            case "PG":
                sql = "SELECT * FROM Movies WHERE rating = 'PG'";
                break;
            case "PG-13":
                sql = "SELECT * FROM Movies WHERE rating = 'PG-13'";
                break;
            case "R":
                sql = "SELECT * FROM Movies WHERE rating = 'R'";
                break;
            case "NC17":
                sql = "SELECT * FROM Movies WHERE rating = 'NC17'";
                break;
            default:
                sql = "SELECT * FROM Movies";
                break;
        }

        List<String> list = new ArrayList<>();
        String data ="";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            if (!rs.isBeforeFirst()){
                return null;
            }else{
                while((rs.next())){
                    data=rs.getString("movieTitle")+"\n"+rs.getString("rating");
                    list.add(data);
                }
                return list;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List getMovies(int cinemaId){
        String sql;
        sql = "SELECT * FROM Movies INNER JOIN MoviesPlaying USING (MovieId) WHERE cinemaId ="+cinemaId+" ORDER BY showtimeHour AND showtimeAMPM";

        List<String> list = new ArrayList<>();
        String data ="";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            if (!rs.isBeforeFirst()){
                return null;
            }else{
                while((rs.next())){
                    data=rs.getString("movieTitle")+"\n"+rs.getString("rating");
                    list.add(data);
                }
                return list;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List getCinemas(){
        String sql;
        sql = "SELECT * FROM Cinemas";

        List<String> list = new ArrayList<>();
        String data ="";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            if (!rs.isBeforeFirst()){
                return null;
            }else{
                while((rs.next())){
                    data=rs.getString("cinemaName")+"\n"+"Located at: ("+rs.getString("locationX")+","+rs.getString("locationY")+")";
                    list.add(data);
                }
                return list;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List getCinemas(int movieId){
        String sql;
        sql = "SELECT * FROM Cinemas INNER JOIN MoviesPlaying USING (cinemaId) WHERE movieId="+movieId+" ORDER BY cinemaId";

        List<String> list = new ArrayList<>();
        String data ="";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            System.out.println("hi");
            if (!rs.isBeforeFirst()){
                return null;
            }else{
                while((rs.next())){

                    data=rs.getString("cinemaName")+"\n"+"Located at: ("+rs.getString("locationX")
                            +","+rs.getString("locationY")+")\n"+"At "+rs.getInt("showtimeHour")
                    +":"+rs.getInt("showtimeMinute")+rs.getString("showtimeAMPM");
                    list.add(data);
                }
                return list;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void removeMovie(int movieId){
        String sql;
        sql = "DELETE FROM MoviesPlaying WHERE movieId="+movieId;

        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement()){
            stmt.executeUpdate(sql);
            sql = "DELETE FROM Movies WHERE movieId="+movieId;
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeCinema(int cinemaId){
        String sql;
        sql = "DELETE FROM MoviesPlaying WHERE cinemaId="+cinemaId;

        try (Connection conn = this.connect(); Statement stmt  = conn.createStatement()){
            stmt.executeUpdate(sql);
            sql = "DELETE FROM Cinemas WHERE cinemaId="+cinemaId;
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean addMovie(String movieTitle, String rating, int hour, int minute, String timePeriod, int cinemaId){
        //first check that on the existence of movieTitle in Movies table
        //if it does exist: do query from Movies to get the movieId to add row to MoviesPlaying
        //else 1) add it to Movies, 2) query for movieId, 3) add row to MoviesPlaying

        String sql;
        sql = "SELECT * FROM Movies WHERE movieTitle='"+movieTitle+"'";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            if (!rs.isBeforeFirst()){
                //movie doesn't exist
                sql = "INSERT INTO Movies (movieTitle,rating) VALUES(?,?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, movieTitle);
                    pstmt.setString(2, rating);
                    pstmt.executeUpdate();
                    addMovie(movieTitle,rating,hour,minute,timePeriod,cinemaId);  /*now it should start method again and
                                                                                 see that movie exists, so it just goes
                                                                                to else statement*/
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    return false;
                }
            }else{
                rs.next();
                int movieId = rs.getInt("movieId");
                sql = "INSERT INTO MoviesPlaying(movieId,showtimeHour,showtimeMinute,showtimeAMPM, cinemaId) VALUES(?,?,?,?,?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, movieId);
                    pstmt.setInt(2, hour);
                    pstmt.setInt(3, minute);
                    pstmt.setString(4, timePeriod);
                    pstmt.setInt(5, cinemaId);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    return false;
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }


        return true;
    }

    public boolean addCinema(String cinemaName, int locationX, int locationY){
        String sql;
            sql = "SELECT * FROM Cinemas WHERE cinemaName='"+cinemaName+"'";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            if (!rs.isBeforeFirst()){
                //cinema doesn't exist
                sql = "INSERT INTO Cinemas (cinemaName,locationX,locationY) VALUES(?,?,?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, cinemaName);
                    pstmt.setInt(2, locationX);
                    pstmt.setInt(2, locationY);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    return false;
                }
            }else{
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }


}
