package sample;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.Duration;
import sample.entities.QuitBox;

public class HireSellerWindowController {

    @FXML
    private ChoiceBox<KeyValuePair> shopsChoiceBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private Label sellerHiredLabel;

    @FXML
    private Button okButton;

    @FXML
    private Button retrunButton;

    @FXML
    private void initialize() {
        ObservableList<KeyValuePair> shops = FXCollections.observableArrayList(GlobalData.selectShops());
        shopsChoiceBox.setItems(shops);
    }


    @FXML
    void okButtonClicked(ActionEvent actionEvent) {
        String fullShopAddres;
        if(nameTextField.getText().length() == 0 || surnameTextField.getText().length() == 0 || shopsChoiceBox.getValue() == null){
            Alert notEnoughData = new Alert(Alert.AlertType.WARNING);
            notEnoughData.setTitle("Brak danych");
            notEnoughData.setHeaderText(null);
            notEnoughData.setContentText("Niewystarczająca ilość danych o sprzedawcy.\nProszę wprowadzić wszystkie dane.");
            System.out.println("Brak danych sprzedawcy");
            notEnoughData.showAndWait();
        }
        else{
            Long phoneNumber = null;
            try {
                if (phoneNumberTextField.getText().isEmpty() == false && phoneNumberTextField.getText().length() == 9) {
                    phoneNumber = Long.valueOf(phoneNumberTextField.getText());
                }
                else if(phoneNumberTextField.getText().isEmpty() == true){

                }
                else {
                    System.out.println("TU");
                    throw new NumberFormatException("bad number");
                }
                GlobalData.insertNewSeller(nameTextField.getText(), surnameTextField.getText(), phoneNumber, shopsChoiceBox.getValue().getKey());

                nameTextField.clear();
                surnameTextField.clear();
                phoneNumberTextField.clear();
                shopsChoiceBox.getSelectionModel().clearSelection();
                sellerHiredLabel.setOpacity(1.0);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(3000), sellerHiredLabel);
                fadeIn.setFromValue(1.0);
                fadeIn.setToValue(0.0);
                fadeIn.play();
            }catch(NumberFormatException e){
                Alert wrongTelephoneNumberFormat = new Alert(Alert.AlertType.WARNING);
                wrongTelephoneNumberFormat.setTitle("Niepoprawny numer telefonu");
                wrongTelephoneNumberFormat.setHeaderText(null);
                wrongTelephoneNumberFormat.setContentText("Niepoprawny numer telefonu.\nProszę wprowadzić poprawny numer.");
                wrongTelephoneNumberFormat.showAndWait();
            }
        }
    }

    @FXML
    void returnButtonClicked(ActionEvent actionEvent) throws Exception {
        boolean exit = true;
        if(nameTextField.getText().length() !=  0 || surnameTextField.getText().length() != 0 || phoneNumberTextField.getText().length() != 0){
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
