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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;

public class PublicController {
    DatabaseHelper dbHelper;

    @FXML
    private TextField x_address;

    @FXML
    private TextField y_address;

    @FXML
    private ComboBox filter;

    @FXML
    private TextField miles;

    @FXML
    private ListView<String> tab1ListView;

    @FXML
    private ListView<String> tab2ListView;

    public void initialize(){
        filter.getItems().addAll("All","G","PG","PG-13","R","NC17");
        miles.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    miles.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        } );

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

        dbHelper=new DatabaseHelper();

        List list = dbHelper.getMovies("All");
        if(list!=null){
            ObservableList<String> observableList = FXCollections.observableList(list);
            tab1ListView.setItems(observableList);
            tab1ListView.setCellFactory(lv -> new ListCell<String>() {
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
            tab1ListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2) {
                        if(tab1ListView.getSelectionModel().getSelectedItem()==null){
                            return;
                        }
                        tab1ListView.getSelectionModel().getSelectedItem();

                        Stage window = new Stage();
                        window.initModality(Modality.APPLICATION_MODAL);
                        window.setTitle("Cinema List");
                        window.setMinWidth(400);
                        window.setMinHeight(500);

                        javafx.scene.control.Label label = new javafx.scene.control.Label();
                        label.setText("Playing at these Cinemas: ");
                        javafx.scene.control.Button closeButton = new javafx.scene.control.Button("OK");
                        closeButton.setOnAction(e -> window.close());
                        ListView<String> cinemalist = new ListView<String>();
                        String[] content=tab1ListView.getSelectionModel().getSelectedItem().split("\n");
                        List list = dbHelper.getCinemas(dbHelper.getMovieId(content[0]));
                        if (list != null) {
                            ObservableList<String> observableList = FXCollections.observableList(list);
                            cinemalist.setItems(observableList);
                        }
                        else {
                            ObservableList<String> observableList = FXCollections.observableList(new ArrayList<>());
                            cinemalist.setItems(observableList);
                        }
                        VBox layout = new VBox(10);
                        HBox bottom = new HBox();
                        bottom.getChildren().addAll(closeButton);
                        bottom.setAlignment(Pos.CENTER);
                        layout.setPadding(new Insets(10,10,10,10));
                        layout.getChildren().addAll(label, cinemalist, bottom);
                        layout.setAlignment(Pos.TOP_LEFT);

                        Scene scene = new Scene(layout);
                        scene.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
                        window.setScene(scene);
                        window.showAndWait();
                }
                }
            });
        }else{
            ObservableList<String> observableList = FXCollections.observableList(new ArrayList<>());
            tab1ListView.setItems(observableList);
        }

        list = dbHelper.getCinemas();
        if(list!=null){
            ObservableList<String> observableList = FXCollections.observableList(list);
            tab2ListView.setItems(observableList);
            tab2ListView.setCellFactory(lv -> new ListCell<String>() {
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
            tab2ListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(event.getClickCount()==2){
                        if(tab2ListView.getSelectionModel().getSelectedItem()==null){
                            return;
                        }
                        tab2ListView.getSelectionModel().getSelectedItem();


                        Stage window = new Stage();
                        window.initModality(Modality.APPLICATION_MODAL);
                        window.setTitle("Movie List");
                        window.setMinWidth(400);
                        window.setMinHeight(500);

                        javafx.scene.control.Label label = new javafx.scene.control.Label();
                        label.setText("Movies Playing: ");
                        javafx.scene.control.Button closeButton = new javafx.scene.control.Button("OK");
                        closeButton.setOnAction(e -> window.close());
                        ListView<String> movielist = new ListView<String>();
                        String[] content=tab2ListView.getSelectionModel().getSelectedItem().split("\n");
                        List list = dbHelper.getMovies(dbHelper.getCinemaId(content[0]));
                        if (list != null) {
                            ObservableList<String> observableList = FXCollections.observableList(list);
                            movielist.setItems(observableList);
                        }
                        else {
                            ObservableList<String> observableList = FXCollections.observableList(new ArrayList<>());
                            movielist.setItems(observableList);
                        }
                        VBox layout = new VBox(10);
                        HBox bottom = new HBox();
                        bottom.getChildren().addAll(closeButton);
                        bottom.setAlignment(Pos.CENTER);
                        layout.setPadding(new Insets(10,10,10,10));
                        layout.getChildren().addAll(label, movielist,bottom);
                        layout.setAlignment(Pos.TOP_LEFT);

                        Scene scene = new Scene(layout);
                        scene.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
                        window.setScene(scene);
                        window.showAndWait();
                    }
                }
            });
        }else{
            ObservableList<String> observableList = FXCollections.observableList(new ArrayList<>());
            tab2ListView.setItems(observableList);
        }

    }

    public void Admin(ActionEvent event) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Administrator Access");
        window.setMinWidth(250);

        javafx.scene.control.Label label = new javafx.scene.control.Label();
        label.setText("Password");
        javafx.scene.control.PasswordField password = new javafx.scene.control.PasswordField();
        password.setOnAction(e -> {
            try {
                AdminPass(password.getText(),event, window);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, password);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        layout.setPadding(new Insets(10,0,0,0));
        scene.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
        window.setScene(scene);
        window.showAndWait();

    }


    public void movieApply(ActionEvent e){
        dbHelper=new DatabaseHelper();

        if(filter.getSelectionModel().getSelectedItem() == "All"){
            ObservableList<String> observableList = FXCollections.observableList(dbHelper.getMovies("All"));
            tab1ListView.setItems(observableList);
            tab1ListView.setCellFactory(lv -> new ListCell<String>() {
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

        else if(filter.getSelectionModel().getSelectedItem() == "G"){
            if(dbHelper.getMovies("G") == null){
                ObservableList<String> observableList = FXCollections.observableList(new ArrayList<>());
                tab1ListView.setItems(observableList);
            }
            else {
                ObservableList<String> observableList = FXCollections.observableList(dbHelper.getMovies("G"));
                tab1ListView.setItems(observableList);
                tab1ListView.setCellFactory(lv -> new ListCell<String>() {
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
        }

        else if(filter.getSelectionModel().getSelectedItem() == "PG"){
            if(dbHelper.getMovies("PG") == null){
                ObservableList<String> observableList = FXCollections.observableList(new ArrayList<>());
                tab1ListView.setItems(observableList);
            }
            else {
                ObservableList<String> observableList = FXCollections.observableList(dbHelper.getMovies("PG"));
                tab1ListView.setItems(observableList);
                tab1ListView.setCellFactory(lv -> new ListCell<String>() {
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
        }

        else if(filter.getSelectionModel().getSelectedItem() == "PG-13"){
            if(dbHelper.getMovies("PG-13") == null){
                ObservableList<String> observableList = FXCollections.observableList(new ArrayList<>());
                tab1ListView.setItems(observableList);
            }
            else {
                ObservableList<String> observableList = FXCollections.observableList(dbHelper.getMovies("PG-13"));
                tab1ListView.setItems(observableList);
                tab1ListView.setCellFactory(lv -> new ListCell<String>() {
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
        }

        else if(filter.getSelectionModel().getSelectedItem() == "R"){
            if(dbHelper.getMovies("R") == null){
                ObservableList<String> observableList = FXCollections.observableList(new ArrayList<>());
                tab1ListView.setItems(observableList);
            }
            else {
                ObservableList<String> observableList = FXCollections.observableList(dbHelper.getMovies("R"));
                tab1ListView.setItems(observableList);
                tab1ListView.setCellFactory(lv -> new ListCell<String>() {
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
        }

        else if(filter.getSelectionModel().getSelectedItem() == "NC17"){
            if(dbHelper.getMovies("NC17") == null){
                ObservableList<String> observableList = FXCollections.observableList(new ArrayList<>());
                tab1ListView.setItems(observableList);
            }
            else {
                ObservableList<String> observableList = FXCollections.observableList(dbHelper.getMovies("NC17"));
                tab1ListView.setItems(observableList);
                tab1ListView.setCellFactory(lv -> new ListCell<String>() {
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
        }

        else{
            ObservableList<String> observableList = FXCollections.observableList(dbHelper.getMovies("All"));
            tab1ListView.setItems(observableList);
            tab1ListView.setCellFactory(lv -> new ListCell<String>() {
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
    }

    public void cinemaApply(ActionEvent e){
        if(!x_address.getText().isEmpty() && y_address.getText().isEmpty()){
            AlertBox("address", "address field is incomplete.");
            return;
        }

        else if(x_address.getText().isEmpty() && !y_address.getText().isEmpty()){
            AlertBox("address", "address field is incomplete.");
            return;
        }

        else if(x_address.getText().isEmpty() && y_address.getText().isEmpty() && !miles.getText().isEmpty()){
            AlertBox("address", "address field is incomplete");
        }

        else if(x_address.getText().isEmpty() && y_address.getText().isEmpty() && miles.getText().isEmpty()){
            List list = dbHelper.getCinemas();
            if(list!=null){
                ObservableList<String> observableList = FXCollections.observableList(list);
                tab2ListView.setItems(observableList);
                tab2ListView.setCellFactory(lv -> new ListCell<String>() {
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
                tab2ListView.setItems(observableList);
            }
        }

        else if(!x_address.getText().isEmpty() && !y_address.getText().isEmpty() && miles.getText().isEmpty()){
            List list = dbHelper.getCinemas();
            if(list!=null){
                ObservableList<String> observableList = FXCollections.observableList(list);
                tab2ListView.setItems(observableList);
                tab2ListView.setCellFactory(lv -> new ListCell<String>() {
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
                tab2ListView.setItems(observableList);
            }
        }

        else {
            List list = dbHelper.getCinemas(Integer.parseInt(x_address.getText()), Integer.parseInt(y_address.getText()), Integer.parseInt(miles.getText()));
            if(list != null){
                ObservableList<String> observableList = FXCollections.observableList(list);
                tab2ListView.setItems(observableList);
                tab2ListView.setCellFactory(lv -> new ListCell<String>() {
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
                tab2ListView.setItems(observableList);
            }
        }
    }

    private void AdminPass(String password, ActionEvent event, Stage w) throws IOException{
        if(password.equals("admin")){
            w.close();
            Parent account = FXMLLoader.load(getClass().getResource("Admin.fxml"));
            Scene accountscene = new Scene(account);

            Stage window2 = (Stage)((Node)event.getSource()).getScene().getWindow();

            window2.setScene(accountscene);
            window2.show();
        }

        else
            w.close();

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
