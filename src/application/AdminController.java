package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javax.swing.*;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class AdminController {

        @FXML
        private TextField movietitle;

        @FXML
        private TextField cinemaname;

        @FXML
        private TextField x_address;

        @FXML
        private TextField y_address;

        @FXML
        private ComboBox rating;

        @FXML
        private ComboBox cinema;

        @FXML
        private ComboBox hours;

        @FXML
        private ComboBox minutes;

        @FXML
        private ComboBox daytime;

    public void initialize(){
        rating.getItems().addAll("G","PG","PG-13","R","NC17");
        cinema.getItems().addAll("all cinemas in db");
        hours.getItems().addAll(IntStream.rangeClosed(1,12).boxed().collect(Collectors.toList()));
        minutes.getItems().addAll(IntStream.rangeClosed(0,59).boxed().collect(Collectors.toList()));
        daytime.getItems().addAll("am","pm");

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
                AlertBox("movietitle", "movie title field is empty.");
            }

            else if(rating.getSelectionModel().getSelectedItem() == null){
                AlertBox("rating", "no rating selected");
            }

            else if(cinema.getSelectionModel().getSelectedItem() == null){
                AlertBox("cinema", "no cinema selected");
            }

            else if(hours.getSelectionModel().getSelectedItem() == null){
                AlertBox("hours", "no hours selected");
            }

            else if(minutes.getSelectionModel().getSelectedItem() == null){
                AlertBox("minutes", "no minutes selected");
            }

            else if(daytime.getSelectionModel().getSelectedItem() == null){
                AlertBox("daytime", "no daytime selected");
            }

            else
                AlertBox("Movie added", "Movie added");
        }

        public void cinemaApply(ActionEvent e){
            if(cinemaname.getText().isEmpty()){
                AlertBox("cinema", "cinema field is empty.");
            }

            else if(x_address.getText().isEmpty() || y_address.getText().isEmpty()){
                AlertBox("address", "address field is incomplete.");
            }

            else
                AlertBox("Cinema added", "Cinema added");
        }

        public void movieDelete(ActionEvent e){
            AlertBox("delete", "Movie deleted");
        }

        public void cinemaDelete(ActionEvent e){
            AlertBox("delete", "Movie deleted");
        }

        public void moviedisplay(ActionEvent e){
            AlertBox("display", "displays list of movies");
        }

        public void cinemadisplay(ActionEvent e){
            AlertBox("display", "displays list of cinemas");
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
            window.setScene(scene);
            window.showAndWait();

        }
}
