package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PublicController {
    DatabaseHelper dbHelper;

    @FXML
    private TextField address;

    @FXML
    private ComboBox filter;

    @FXML
    private TextField miles;

    @FXML
    private ListView<String> tab1ListView;

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
        }else{
            ObservableList<String> observableList = FXCollections.observableList(new ArrayList<>());
            tab1ListView.setItems(observableList);
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
        window.setScene(scene);
        window.showAndWait();

    }


    public void movieApply(ActionEvent e){
        System.out.println("Movies are listed");
    }

    public void cinemaApply(ActionEvent e){
        System.out.println("cinema list filtered to address");
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
        window.setScene(scene);
        window.showAndWait();

    }
}
