package sample;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import sample.entities.QuitBox;

public class OpenShopWindowController {

    @FXML
    private Button okButton;

    @FXML
    private Button returnButton;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField streetTextField;

    @FXML
    private TextField numberTextField;

    @FXML
    private Label addedNewShopLabel;


    @FXML
    void okButtonClicked(ActionEvent actionEvent) {
        String fullShopAddres;
        if(cityTextField.getText().length() == 0 || streetTextField.getText().length() == 0 || numberTextField.getText().length() == 0){
            Alert notEnoughData = new Alert(Alert.AlertType.WARNING);
            notEnoughData.setTitle("Brak danych");
            notEnoughData.setHeaderText(null);
            notEnoughData.setContentText("Niewystarczająca ilość danych o sklepie.\nProszę wprowadzić wszystkie dane.");
            System.out.println("Brak danych sklepu");
            notEnoughData.showAndWait();
        }
        else{
            fullShopAddres = cityTextField.getText() + ", " + streetTextField.getText() + " " + numberTextField.getText();

            GlobalData.insertNewShop(fullShopAddres);
            System.out.println(fullShopAddres);

            cityTextField.clear();
            streetTextField.clear();
            numberTextField.clear();
            addedNewShopLabel.setOpacity(1.0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(3000), addedNewShopLabel);
            fadeIn.setFromValue(1.0);
            fadeIn.setToValue(0.0);
            fadeIn.play();
        }
    }

    @FXML
    void returnButtonClicked(ActionEvent actionEvent) throws Exception {

        boolean exit = true;
        if(cityTextField.getText().length() !=  0 || streetTextField.getText().length() != 0 || numberTextField.getText().length() != 0){
            QuitBox quitbox = new QuitBox();
            exit = quitbox.checkIfQuit();
        }
        if(exit){
            Parent root = FXMLLoader.load(getClass().getResource("managerActionsWindow.fxml"));
            Scene scene = new Scene(root, 1280, 720);
            Main.setWindow(scene);
        }
    }
}
