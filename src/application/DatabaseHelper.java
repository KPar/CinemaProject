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
        sql = "SELECT * FROM Movies INNER JOIN MoviesPlaying USING (MovieId) WHERE cinemaId ="+cinemaId+" ORDER BY showTimeHour AND showTimeAMPM";

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

            if (!rs.isBeforeFirst()){
                return null;
            }else{
                while((rs.next())){
                    data=rs.getString("cinemaName")+"\n"+"Located at: ("+rs.getString("locationX")
                            +","+rs.getString("locationY")+")\n"+"At "+rs.getInt("showTimeHour")
                    +":"+rs.getInt("showTimeMinute")+rs.getString("showTimeAMPM");
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

   /* public boolean addMovie(String movieTitle, String rating, int hour, int minute, String timePeriod, int cinemaId){
        //first check that on the existence of movieTitle in Movies table
        //if it does exist: do query from Movies to get the movieId to add row to MoviesPlaying
        //else 1) add it to Movies, 2) query for movieId, 3) add row to MoviesPlaying


        int recipientUserId=0;

        if (emailStatus==1){
            if(recipient.length()!=0) {
                if(getUserId(recipient).length!=0){
                    recipientUserId = getUserId(recipient)[0];
                }else{
                    return false;
                }
            }
        }else{
            if(getUserId(recipient).length!=0){
                recipientUserId = getUserId(recipient)[0];
            }else{
                return false;
            }
        }


        Date date = new Date();
        long dateNum = date.getTime();

        SimpleDateFormat ft =
                new SimpleDateFormat ("MMM dd, yyyy 'at' hh:mm:ss a zzz");

        String dateText= ft.format(date);

        String sql = "INSERT INTO EmailsTable(subject,content,emailStatus,senderId,recipientId,dateText,dateInteger) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, subject);
            pstmt.setString(2, content);
            pstmt.setInt(3, emailStatus);
            pstmt.setInt(4, senderUserId);
            pstmt.setInt(5, recipientUserId);
            pstmt.setString(6, dateText);
            pstmt.setLong(7, dateNum);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if(emailStatus==0){
            System.out.println("Email sent to: "+recipient+" from "+getEmailAddress(senderUserId)+" with Subject: "+subject+" and Content: "+content);
        }else{
            System.out.println("Email draft saved to: "+recipient+" from "+getEmailAddress(senderUserId)+"  with Subject:"+subject+" and Content: "+content);
        }
        return true;
    }
*/


}
