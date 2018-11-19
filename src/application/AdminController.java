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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class AdminController {
        DatabaseHelper dbHelper;

        @FXML
        private TextField movieTitleTextField;

        @FXML
        private ComboBox moviesComboBox;

        @FXML
        private TextField cinemaNameTextField;

        @FXML
        private ComboBox cinemasComboBox;

        @FXML
        private TextField xCoordinateTextField;

        @FXML
        private TextField yCoordinateTextField;

        @FXML
        private ComboBox ratingComboBox;

        @FXML
        private ComboBox releaseTypeComboBox;

        @FXML
        private ComboBox hoursComboBox;

        @FXML
        private ComboBox minutesComboBox;

        @FXML
        private ComboBox timePeriodComboBox;

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
        private RadioButton GeneralBttn;

        @FXML
        private RadioButton AllBttn;

        @FXML
        private RadioButton LimitedBttn;

        @FXML
        private ListView<String> movieListView;

        @FXML
        private ListView<String> cinemaListView;

        @FXML
        private ListView<String> showtimesListView;

    public void initialize(){
        dbHelper=new DatabaseHelper();

        ratingComboBox.getItems().addAll("Unrated","G","PG","PG-13","R","NC17");
        releaseTypeComboBox.getItems().addAll("General","Limited");
        hoursComboBox.getItems().addAll(IntStream.rangeClosed(1,12).boxed().collect(Collectors.toList()));
        minutesComboBox.getItems().addAll(IntStream.rangeClosed(0,59).boxed().collect(Collectors.toList()));
        timePeriodComboBox.getItems().addAll("AM","PM");
        moviesComboBox.getItems().clear();
        if(dbHelper.getMovies("All")==null){
            moviesComboBox.getItems().add("No Movies in Database");
        }else{
            moviesComboBox.getItems().addAll(dbHelper.getMovies("All"));
        }


        cinemasComboBox.getItems().clear();
        if(dbHelper.getCinemas()==null){
            cinemasComboBox.getItems().add("No Cinemas in Database");
        }else{
            cinemasComboBox.getItems().addAll(dbHelper.getCinemas());
        }

        xCoordinateTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    xCoordinateTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        } );

        yCoordinateTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    yCoordinateTextField.setText(newValue.replaceAll("[^\\d]", ""));
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

        AllBttn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AllBttn.setSelected(true);
                if (GeneralBttn.isSelected()) {
                    GeneralBttn.setSelected(false);
                }
                if (LimitedBttn.isSelected()) {
                    LimitedBttn.setSelected(false);
                }
            }
        });

        GeneralBttn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!GeneralBttn.isSelected() && !LimitedBttn.isSelected()){
                    AllBttn.setSelected(true);
                }
                else
                    AllBttn.setSelected(false);
            }
        });

        LimitedBttn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!GeneralBttn.isSelected() && !LimitedBttn.isSelected()){
                    AllBttn.setSelected(true);
                }
                else
                    AllBttn.setSelected(false);
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

        list = dbHelper.getMoviesShowtimes();
        if(list!=null) {
            ObservableList<String> observableList = FXCollections.observableList(list);
            showtimesListView.setItems(observableList);
            showtimesListView.setCellFactory(lv -> new ListCell<String>() {
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
            showtimesListView.setItems(observableList);
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
            if(movieTitleTextField.getText().isEmpty()){
                AlertBox("Movietitle", "Movie title field is empty.");
                return;
            }

            else if(ratingComboBox.getSelectionModel().getSelectedItem() == null){
                AlertBox("Rating", "No rating selected");
                return;
            }

            else if(releaseTypeComboBox.getSelectionModel().getSelectedItem() == null){
                AlertBox("Release Type", "Please choose General or Limited Release");
                return;
            }

            else
                dbHelper.addMovie(movieTitleTextField.getText(), ratingComboBox.getSelectionModel().getSelectedItem().toString(), releaseTypeComboBox.getSelectionModel().getSelectedItem().toString());
                refreshAll();
                System.out.println("movie added " + movieTitleTextField.getText() + " ratingComboBox: " + ratingComboBox.getSelectionModel().getSelectedItem().toString());
        }

        public void cinemaApply(ActionEvent e) {
            String releaseTypeRestriction = "";

            if(GeneralBttn.isSelected()){
                releaseTypeRestriction="General";
            }else if(LimitedBttn.isSelected()){
                releaseTypeRestriction="Limited";
            }else{
                releaseTypeRestriction="All";
            }
            if (cinemaNameTextField.getText().isEmpty()) {
                AlertBox("Cinema", "cinema field is empty.");
                return;
            }
            else if (xCoordinateTextField.getText().isEmpty() || yCoordinateTextField.getText().isEmpty()) {
                AlertBox("Address", "address field is incomplete.");
                return;
            }

            else if (G.isSelected() || PG.isSelected() || PG13.isSelected() || R.isSelected() || NC17.isSelected()) {
                ArrayList<String> ratingList = new ArrayList<String>();
                if (G.isSelected()) {
                    ratingList.add("G");
                }
                if (PG.isSelected()) {
                    ratingList.add("PG");
                }
                if (PG13.isSelected()) {
                    ratingList.add("PG-13");
                }
                if (R.isSelected()) {
                    ratingList.add("R");
                }
                if (NC17.isSelected()) {
                    ratingList.add("NC17");
                }

                dbHelper.addCinema(cinemaNameTextField.getText(), Integer.parseInt(xCoordinateTextField.getText()), Integer.parseInt(yCoordinateTextField.getText()), ratingList,releaseTypeRestriction);
                AlertBox("Cinema Added", "Cinema added to Database with " + ratingList + " restrictions");
            }

            else {
                dbHelper.addCinema(cinemaNameTextField.getText(), Integer.parseInt(xCoordinateTextField.getText()), Integer.parseInt(yCoordinateTextField.getText()),releaseTypeRestriction);
                AlertBox("Cinema Added", "Cinema added");
            }
            refreshAll();
        }

        public void showtimeApply(ActionEvent e){
            if(moviesComboBox.getSelectionModel().getSelectedItem() == null){
                AlertBox("Movie Title", "No movie selected");
                return;
            }

            else if(cinemasComboBox.getSelectionModel().getSelectedItem() == null){
                AlertBox("Cinema", "No cinema selected");
                return;
            }

            else if(hoursComboBox.getSelectionModel().getSelectedItem() == null){
                AlertBox("hoursComboBox", "No hour selected");
                return;
            }

            else if(minutesComboBox.getSelectionModel().getSelectedItem() == null){
                AlertBox("minutesComboBox", "No minutes selected");
                return;
            }

            else if(timePeriodComboBox.getSelectionModel().getSelectedItem() == null){
                AlertBox("timePeriodComboBox", "Please select AM or PM");
                return;
            }

            else {
                String[] content= moviesComboBox.getSelectionModel().getSelectedItem().toString().split("\n");
                String[] content2= cinemasComboBox.getSelectionModel().getSelectedItem().toString().split("\n");
                if(dbHelper.addShowtime(dbHelper.getMovieId(content[0]),
                        Integer.parseInt(hoursComboBox.getSelectionModel().getSelectedItem().toString()),
                        Integer.parseInt(minutesComboBox.getSelectionModel().getSelectedItem().toString()),
                        timePeriodComboBox.getSelectionModel().getSelectedItem().toString().toUpperCase(),
                        dbHelper.getCinemaId(content2[0]))==false){
                    AlertBox("Denied", "Movies with this Rating or Release Type are not allowed in this cinema");
                    return;
                }
                AlertBox("Showtime", "Showtime added");
            }
            refreshAll();
        }

        public void movieDelete(ActionEvent e){
            if(movieListView.getSelectionModel().getSelectedItem()==null){
                return;
            }
            String[] content=movieListView.getSelectionModel().getSelectedItem().split("\n");
            dbHelper.removeMovie(dbHelper.getMovieId(content[0]));
            refreshAll();
        }

        public void cinemaDelete(ActionEvent e){
            if(cinemaListView.getSelectionModel().getSelectedItem()==null){
                return;
            }
            String[] content=cinemaListView.getSelectionModel().getSelectedItem().split("\n");
            dbHelper.removeCinema(dbHelper.getCinemaId(content[0]));
            refreshAll();
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

        public void refreshAll(){
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

            list = dbHelper.getMoviesShowtimes();
            if(list!=null) {
                ObservableList<String> observableList = FXCollections.observableList(list);
                showtimesListView.setItems(observableList);
                showtimesListView.setCellFactory(lv -> new ListCell<String>() {
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
                showtimesListView.setItems(observableList);
            }
            moviesComboBox.getItems().clear();
            if(dbHelper.getMovies("All")==null){
                    moviesComboBox.getItems().add("No Movies in Database");
                }else{
                    moviesComboBox.getItems().addAll(dbHelper.getMovies("All"));
                }


            cinemasComboBox.getItems().clear();
            if(dbHelper.getCinemas()==null){
                    cinemasComboBox.getItems().add("No Cinemas in Database");
                }else{
                    cinemasComboBox.getItems().addAll(dbHelper.getCinemas());
                }


        }
}
