package sample;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import sample.entities.QuitBox;

public class AddClientWindowController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label addedCustomerLabel;

    @FXML
    void okButtonClicked(ActionEvent event) throws Exception {
        String name = nameTextField.getText();
        String surname = surnameTextField.getText();
        String email = emailTextField.getText();

        if (email.contains("@") || email.length() == 0){
            if(name.length() == 0 || surname.length() == 0){
                Alert inapropiateNameOrSurname = new Alert(Alert.AlertType.WARNING);
                inapropiateNameOrSurname.setTitle("Błędne dane");
                inapropiateNameOrSurname.setHeaderText(null);
                inapropiateNameOrSurname.setContentText("Niepoprawne dane osobowe.\nProszę wprowadzić imię i nazwisko.");
                System.out.println("Niepoprawne dane osobowe");
                inapropiateNameOrSurname.showAndWait();
            }
            else {
                System.out.println("Dobre dane nowego klienta");
                GlobalData.insertNewCustomer(name, surname, email);
                nameTextField.clear();
                surnameTextField.clear();
                emailTextField.clear();
                addedCustomerLabel.setOpacity(1.0);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(3000), addedCustomerLabel);
                fadeIn.setFromValue(1.0);
                fadeIn.setToValue(0.0);
                fadeIn.play();
            }
        }
        else {
            Alert inapropiateEmailAlert = new Alert(Alert.AlertType.WARNING);
            inapropiateEmailAlert.setTitle("Błędne dane");
            inapropiateEmailAlert.setHeaderText(null);
            inapropiateEmailAlert.setContentText("Niepoprawny adres email.\nWprowadż poprawny adres email.");
            System.out.println("Niepoprawny email");
            inapropiateEmailAlert.showAndWait();
        }
    }

    @FXML
    void returnButtonClicked(ActionEvent event) throws Exception{
        boolean exit = true;
        if(nameTextField.getText().length() !=  0 || surnameTextField.getText().length() != 0 || emailTextField.getText().length() != 0){
            QuitBox quitbox = new QuitBox();
            exit = quitbox.checkIfQuit();
        }
        if(exit){
            Parent root = FXMLLoader.load(getClass().getResource("chooseActionWindow.fxml"));
            Scene scene = new Scene(root, 1280, 720);
            Main.setWindow(scene);
        }

    }

}
