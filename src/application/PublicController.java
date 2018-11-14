package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PublicController {

    public void Admin(ActionEvent event) throws IOException {
        Parent account = FXMLLoader.load(getClass().getResource("Admin.fxml"));
        Scene accountscene = new Scene(account);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(accountscene);
        window.show();

    }
}
