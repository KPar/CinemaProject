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
                    "locationX INTEGER, locationY INTEGER, hasRatingRestriction INTEGER);";
            stmt.execute(sql);
            sql  = "CREATE TABLE IF NOT EXISTS Restrictions ( "+
                    "restrictionId INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "cinemaId INTEGER REFERENCES Cinemas (cinemaId),"+
                    "restrictedRating TEXT);";
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
        sql = "SELECT DISTINCT movieId FROM MoviesPlaying WHERE cinemaId ="+ cinemaId;
        List<String> list = new ArrayList<>();
        String data ="";
        Boolean isFirst = true;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            System.out.println("hi");
            if (!rs.isBeforeFirst()){
                return null;
            }else{
                while((rs.next())){
                    int movieId = rs.getInt("movieId");
                    String sql2 = "SELECT * FROM Cinemas INNER JOIN MoviesPlaying USING (cinemaId) INNER JOIN Movies USING (movieId) WHERE movieId ="+ movieId + " AND cinemaId="+cinemaId
                            +" ORDER BY showtimeAMPM, showtimeHour, showtimeMinute ASC";
                    try (Connection conn2 = this.connect();
                         Statement stmt2  = conn2.createStatement();
                         ResultSet rs2    = stmt2.executeQuery(sql2)){
                        if (!rs2.isBeforeFirst()){
                            return null;
                        }else {
                            while ((rs2.next())) {
                                if(isFirst){
                                    data=data.concat(rs2.getString("movieTitle")+"\n"+"Rated: "+rs2.getString("rating")+"\n"+"Playing at ");
                                    isFirst=false;
                                }
                                if(rs2.getInt("showtimeMinute")==0) {
                                    data = data.concat(" "+rs2.getInt("showtimeHour")
                                            + ":00" + " " + rs2.getString("showtimeAMPM"));
                                }else if(rs2.getInt("showtimeMinute")<=9) {
                                    data = data.concat(" "+rs2.getInt("showtimeHour")
                                            + ":0"+rs2.getInt("showtimeMinute")+ " " + rs2.getString("showtimeAMPM"));
                                }else{
                                    data = data.concat(" "+rs2.getInt("showtimeHour")
                                            +":"+rs2.getInt("showtimeMinute")+" "+rs2.getString("showtimeAMPM"));
                                }
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                    isFirst=true;
                    list.add(data);
                    data="";
                }
                return list;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List getMoviesShowtimes() {
        String sql;
        sql = "SELECT * FROM Movies";
        List<String> list = new ArrayList<>();
        String data = "";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("hi");
            if (!rs.isBeforeFirst()) {
                return null;
            } else {
                while ((rs.next())) {
                    int movieId = rs.getInt("movieId");

                    data = data.concat("[ "+rs.getString("movieTitle") + " " + "  Rated: " + rs.getString("rating")+" ]\n\n");

                    List<String> cinemaList = getCinemas(movieId);
                    if(cinemaList!=null){
                        for (String item : cinemaList) {
                            data = data.concat(item + "\n\n");
                        }
                    }

                    list.add(data);
                    data = "";
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
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

    public List getCinemas(int x, int y, int radius){
        String sql;
        sql = "SELECT * FROM Cinemas WHERE (locationX BETWEEN "+(x-radius)+" AND "+(x+radius)+") AND (locationY BETWEEN "+(y-radius)+" AND "+(y+radius)+")";

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


    public int getMovieId(String movieTitle){
        String sql = "SELECT * FROM Movies WHERE movieTitle='"+movieTitle+"'";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            if (!rs.isBeforeFirst()){
                return -1;
            }else{
                rs.next();
                return rs.getInt("movieId");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }

    }

    public int getCinemaId(String cinemaName){
        String sql = "SELECT * FROM Cinemas WHERE cinemaName='"+cinemaName+"'";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            if (!rs.isBeforeFirst()){
                return -1;
            }else{
                rs.next();
                return rs.getInt("cinemaId");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public List getCinemas(int movieId){
        String sql;
        sql = "SELECT DISTINCT cinemaId FROM MoviesPlaying WHERE movieId ="+ movieId;
        List<String> list = new ArrayList<>();
        String data ="";
        Boolean isFirst = true;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            System.out.println("hi");
            if (!rs.isBeforeFirst()){
                return null;
            }else{
                while((rs.next())){
                    int cinemaId = rs.getInt("cinemaId");
                    String sql2 = "SELECT * FROM Cinemas INNER JOIN MoviesPlaying USING (cinemaId) WHERE movieId ="+ movieId + " AND cinemaId="+cinemaId
                            +" ORDER BY showtimeAMPM, showtimeHour, showtimeMinute ASC";
                    try (Connection conn2 = this.connect();
                         Statement stmt2  = conn2.createStatement();
                         ResultSet rs2    = stmt2.executeQuery(sql2)){
                        if (!rs2.isBeforeFirst()){
                            return null;
                        }else {
                            while ((rs2.next())) {
                                if(isFirst){
                                    data=data.concat(rs2.getString("cinemaName")+"\n"+"Located on: ("+rs2.getString("locationX")
                                            +","+rs2.getString("locationY")+")\n"+"Playing at ");
                                    isFirst=false;
                                }
                                if(rs2.getInt("showtimeMinute")==0) {
                                    data = data.concat(" "+rs2.getInt("showtimeHour")
                                            + ":00" + " " + rs2.getString("showtimeAMPM"));
                                }else if(rs2.getInt("showtimeMinute")<=9) {
                                    data = data.concat(" "+rs2.getInt("showtimeHour")
                                            + ":0"+rs2.getInt("showtimeMinute")+ " " + rs2.getString("showtimeAMPM"));
                                }else{
                                    data = data.concat(" "+rs2.getInt("showtimeHour")
                                            +":"+rs2.getInt("showtimeMinute")+" "+rs2.getString("showtimeAMPM"));
                                }
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                    isFirst=true;
                    list.add(data);
                    data="";
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
            sql = "DELETE FROM Restrictions WHERE cinemaId="+cinemaId;
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean addMovie(String movieTitle, String rating, String releaseType){
        String sql = "SELECT * FROM Movies WHERE movieTitle='"+movieTitle+"'";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            if (!rs.isBeforeFirst()){
                //movie doesn't exist

                sql = "INSERT INTO Movies (movieTitle,rating,releaseType) VALUES(?,?,?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, movieTitle);
                    pstmt.setString(2, rating);
                    pstmt.setString(3, releaseType);
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

    public String getRating(int movieId){
        String sql = "SELECT * FROM Movies WHERE movieId="+movieId;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            if (!rs.isBeforeFirst()){
                return null;
            }else{
                rs.next();
                return rs.getString("rating");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean addShowtime(int movieId, int hour, int minute, String timePeriod, int cinemaId){
        //query for hasRatingRestriction of cinema
        String sql = "SELECT * FROM Cinemas WHERE cinemaId="+cinemaId+"";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) {
            }else {
                rs.next();  //added this
                if(rs.getInt("hasRatingRestriction")==1){ //limited release
                    //query Restrictions table to see if rating is allowed in cinema
                    sql = "SELECT * FROM Restrictions WHERE restrictedRating='"+getRating(movieId)+"' AND cinemaId="+cinemaId;
                    System.out.println(getRating(movieId));
                    try (ResultSet rs2    = stmt.executeQuery(sql)) {
                        if (!rs2.isBeforeFirst()) {
                            //movie rating not allowed
                            System.out.println("not allowed");
                            return false;
                        }
                    }catch (SQLException e){

                    }
                }
            }
        } catch (SQLException e){

        }
        sql = "SELECT * FROM Movies WHERE movieId="+movieId+"";
        String releaseType="";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) {
            }else {
                rs.next();
                releaseType=rs.getString("releaseType");
                System.out.println(rs.getInt("movieId"));
            }
        } catch (SQLException e){

        }

        sql = "SELECT * FROM Cinemas WHERE cinemaId="+cinemaId+"";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) {
            }else {
                rs.next();
                if(!(true||false)){
                    System.out.println("rhii");
                }
                if(!(rs.getString("releaseTypeRestriction").equals("All")||rs.getString("releaseTypeRestriction").equals(releaseType))){
                    System.out.println("release type not allowed here "+releaseType+"  "+rs.getString("releaseTypeRestriction").equals("All")
                            +" "+rs.getString("releaseTypeRestriction").equals(releaseType));
                    return false;
                }


            }
        } catch (SQLException e){

        }


        sql = "INSERT INTO MoviesPlaying(movieId,showtimeHour,showtimeMinute,showtimeAMPM, cinemaId) VALUES(?,?,?,?,?)";
        try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
        return true;
    }

    public boolean addCinema(String cinemaName, int locationX, int locationY, String releaseTypeRestriction){
        String sql;
            sql = "SELECT * FROM Cinemas WHERE cinemaName='"+cinemaName+"'";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            if (!rs.isBeforeFirst()){
                //cinema doesn't exist
                sql = "INSERT INTO Cinemas (cinemaName,locationX,locationY, hasRatingRestriction, releaseTypeRestriction) VALUES(?,?,?,?,?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, cinemaName);
                    pstmt.setInt(2, locationX);
                    pstmt.setInt(3, locationY);
                    pstmt.setInt(4, 0);
                    pstmt.setString(5, releaseTypeRestriction);

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
    public boolean addCinema(String cinemaName, int locationX, int locationY, ArrayList<String> restrictionList, String releaseTypeRestriction){
        String sql;
        sql = "SELECT * FROM Cinemas WHERE cinemaName='"+cinemaName+"'";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            if (!rs.isBeforeFirst()){
                //cinema doesn't exist
                sql = "INSERT INTO Cinemas (cinemaName,locationX,locationY, hasRatingRestriction, releaseTypeRestriction) VALUES(?,?,?,?,?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, cinemaName);
                    pstmt.setInt(2, locationX);
                    pstmt.setInt(3, locationY);
                    pstmt.setInt(4, 1);
                    pstmt.setString(5, releaseTypeRestriction);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    return false;
                }

                //add restrictions to restriction table
                for(String rating : restrictionList){
                        sql = "INSERT INTO Restrictions (cinemaId,restrictedRating) VALUES(?,?)";
                    System.out.println("hi");

                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setInt(1, getCinemaId(cinemaName));
                            pstmt.setString(2, rating);
                            pstmt.executeUpdate();
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                            return false;
                        }
                    }
                } else{
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

}
