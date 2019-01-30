package sample;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Duration;
import sample.entities.QuitBox;
import javafx.scene.control.*;

public class AddCategoryWindowController {

    @FXML
    private TextField categoryTextField;

    @FXML
    private Label addedCategoryLabel;

    @FXML
    void okButtonClicked(ActionEvent event) throws Exception {
        String newCategory = categoryTextField.getText();
        boolean insertedToDB;
        if(newCategory.length() != 0){

            insertedToDB = GlobalData.insertNewCategory(newCategory);
            if(insertedToDB) {
                categoryTextField.clear();
                addedCategoryLabel.setOpacity(1.0);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(3000), addedCategoryLabel);
                fadeIn.setFromValue(1.0);
                fadeIn.setToValue(0.0);
                fadeIn.play();
            }
            else{
                Alert duplicatedName = new Alert(Alert.AlertType.WARNING);
                duplicatedName.setTitle("Zduplikowana nazwa");
                duplicatedName.setHeaderText(null);
                duplicatedName.setContentText("Taka kategoria juz istnieje.\nProszę wprowadzić inną nazwę kategorii.");
                duplicatedName.showAndWait();
            }
        }
    }

    @FXML
    void retrunButtonClicked(ActionEvent event) throws Exception {
        boolean exit = true;
        if(categoryTextField.getText().length() !=  0 ){
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
