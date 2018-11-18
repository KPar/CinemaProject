package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class AdminController {
        DatabaseHelper dbHelper;

        @FXML
        private TextField movietitle;

        @FXML
        private ComboBox movieS;

        @FXML
        private TextField cinemaname;

        @FXML
        private ComboBox cinemaS;

        @FXML
        private TextField x_address;

        @FXML
        private TextField y_address;

        @FXML
        private ComboBox rating;

        @FXML
        private ComboBox hours;

        @FXML
        private ComboBox minutes;

        @FXML
        private ComboBox daytime;

        @FXML
        private RadioButton All;

        @FXML
        private RadioButton G;

        @FXML
        private RadioButton PG;

        @FXML
        private RadioButton PG13;

        @FXML
        private RadioButton R;

        @FXML
        private RadioButton NC17;

        @FXML
        private ListView<String> movieListView;

        @FXML
        private ListView<String> cinemaListView;

        @FXML
        private ListView<String> showtimesListView;

    public void initialize(){
        rating.getItems().addAll("G","PG","PG-13","R","NC17");
        hours.getItems().addAll(IntStream.rangeClosed(1,12).boxed().collect(Collectors.toList()));
        minutes.getItems().addAll(IntStream.rangeClosed(0,59).boxed().collect(Collectors.toList()));
        daytime.getItems().addAll("am","pm");
        movieS.getItems().addAll("movies in database");
        cinemaS.getItems().addAll("cinemas in database");
        dbHelper=new DatabaseHelper();

        x_address.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    x_address.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        } );

        y_address.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    y_address.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        } );

        All.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                All.setSelected(true);
                if (G.isSelected()) {
                    G.setSelected(false);
                }
                if (PG.isSelected()) {
                    PG.setSelected(false);
                }
                if (PG13.isSelected()) {
                    PG13.setSelected(false);
                }
                if (R.isSelected()) {
                    R.setSelected(false);
                }
                if (NC17.isSelected()) {
                    NC17.setSelected(false);
                }
            }
        });

        G.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!G.isSelected() && !PG.isSelected() && !PG13.isSelected() && !R.isSelected() && !NC17.isSelected()){
                    All.setSelected(true);
                }
                else
                    All.setSelected(false);
            }
        });

        PG.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!G.isSelected() && !PG.isSelected() && !PG13.isSelected() && !R.isSelected() && !NC17.isSelected()){
                    All.setSelected(true);
                }
                else
                    All.setSelected(false);
            }
        });

        PG13.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!G.isSelected() && !PG.isSelected() && !PG13.isSelected() && !R.isSelected() && !NC17.isSelected()){
                    All.setSelected(true);
                }
                else
                    All.setSelected(false);
            }
        });

        R.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!G.isSelected() && !PG.isSelected() && !PG13.isSelected() && !R.isSelected() && !NC17.isSelected()){
                    All.setSelected(true);
                }
                else
                    All.setSelected(false);
            }
        });

        NC17.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!G.isSelected() && !PG.isSelected() && !PG13.isSelected() && !R.isSelected() && !NC17.isSelected()){
                    All.setSelected(true);
                }
                else
                    All.setSelected(false);
            }
        });

        List list = dbHelper.getMovies("All");
        if(list!=null){
            ObservableList<String> observableList = FXCollections.observableList(list);
            movieListView.setItems(observableList);
            movieListView.setCellFactory(lv -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null) {
                        setText(null);
                        setStyle(null);
                    } else {
                        setText(item);
                    }
                }
            });
        }
            else {
            ObservableList<String> observableList = FXCollections.observableList(new ArrayList<>());
            movieListView.setItems(observableList);
        }

        list = dbHelper.getCinemas();
        if(list!=null) {
            ObservableList<String> observableList = FXCollections.observableList(list);
            cinemaListView.setItems(observableList);
            cinemaListView.setCellFactory(lv -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null) {
                        setText(null);
                        setStyle(null);
                    } else {
                        setText(item);
                    }
                }
            });
        }
            else{
                ObservableList<String> observableList = FXCollections.observableList(new ArrayList<>());
                cinemaListView.setItems(observableList);
            }

    }

        public void Public(ActionEvent event) throws IOException {
            Parent account = FXMLLoader.load(getClass().getResource("Public.fxml"));
            Scene accountscene = new Scene(account);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            window.setScene(accountscene);
            window.show();

        }

        public void movieApply(ActionEvent e){
            if(movietitle.getText().isEmpty()){
                AlertBox("Movietitle", "Movie title field is empty.");
                return;
            }

            else if(rating.getSelectionModel().getSelectedItem() == null){
                AlertBox("Rating", "No rating selected");
                return;
            }

            else
                dbHelper.addMovie(movietitle.getText(),rating.getSelectionModel().getSelectedItem().toString());
                System.out.println("movie added " + movietitle.getText() + " rating: " + rating.getSelectionModel().getSelectedItem().toString());
        }

        public void cinemaApply(ActionEvent e) {
            if (cinemaname.getText().isEmpty()) {
                AlertBox("Cinema", "cinema field is empty.");
                return;
            }
            else if (x_address.getText().isEmpty() || y_address.getText().isEmpty()) {
                AlertBox("Address", "address field is incomplete.");
                return;
            }

            else if (G.isSelected() || PG.isSelected() || PG13.isSelected() || R.isSelected() || NC17.isSelected()) {
                ArrayList<String> rating = new ArrayList<String>();
                if (G.isSelected()) {
                    rating.add("G");
                }
                if (PG.isSelected()) {
                    rating.add("PG");
                }
                if (PG13.isSelected()) {
                    rating.add("PG13");
                }
                if (R.isSelected()) {
                    rating.add("R");
                }
                if (NC17.isSelected()) {
                    rating.add("NC17");
                }

                dbHelper.addCinema(cinemaname.getText(), Integer.parseInt(x_address.getText()), Integer.parseInt(y_address.getText()), rating);
                AlertBox("Cinema Added", "Cinema added to database with " + rating + " restrictions");
            }

            else {
                dbHelper.addCinema(cinemaname.getText(), Integer.parseInt(x_address.getText()), Integer.parseInt(y_address.getText()));
                AlertBox("Cinema Added", "Cinema added with no restrictions");
            }
        }

        public void showtimeApply(ActionEvent e){
            if(movieS.getSelectionModel().getSelectedItem() == null){
                AlertBox("Movie Title", "no movie selected");
                return;
            }

            else if(cinemaS.getSelectionModel().getSelectedItem() == null){
                AlertBox("Cinema", "no Cinema selected");
                return;
            }

            else if(hours.getSelectionModel().getSelectedItem() == null){
                AlertBox("hours", "no hours selected");
                return;
            }

            else if(minutes.getSelectionModel().getSelectedItem() == null){
                AlertBox("minutes", "no minutes selected");
                return;
            }

            else if(daytime.getSelectionModel().getSelectedItem() == null){
                AlertBox("daytime", "no daytime selected");
                return;
            }

            else
                //dbHelper.addShowtime(movieS.getSelectionModel().getSelectedIndex(),hours.getSelectionModel().getSelectedIndex()+1,minutes.getSelectionModel().getSelectedIndex(),daytime.getSelectionModel().getSelectedItem().toString(),cinemaS.getSelectionModel().getSelectedIndex());
                AlertBox("Showtime", "Showtime added");

        }

        public void movieDelete(ActionEvent e){
            System.out.println(movieListView.getSelectionModel().getSelectedItem());
            //dbHelper.removeMovie(movieid);
            AlertBox("Delete", "Movie deleted");
        }

        public void cinemaDelete(ActionEvent e){
            AlertBox("Delete", "Cinema deleted");
        }

        private void AlertBox(String title, String message){
            Stage window = new Stage();

            //Block events to other windows
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle(title);
            window.setMinWidth(250);

            javafx.scene.control.Label label = new javafx.scene.control.Label();
            label.setText(message);
            javafx.scene.control.Button closeButton = new javafx.scene.control.Button("OK");
            closeButton.setOnAction(e -> window.close());

            VBox layout = new VBox(10);
            layout.getChildren().addAll(label, closeButton);
            layout.setAlignment(Pos.CENTER);

            //Display window and wait for it to be closed before returning
            Scene scene = new Scene(layout);
            scene.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
            window.setScene(scene);
            window.showAndWait();

        }
}
