package sample;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import sample.entities.QuitBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class AddProductWindowController {

    @FXML
    private ChoiceBox<String> producerChoiceBox;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    @FXML
    private TextField enterName;

    @FXML
    private TextField enterPrice;

    @FXML
    private Label addedNewProductLabel;

    @FXML
    private void initialize() {
        ObservableList<String> producers = FXCollections.observableArrayList(GlobalData.selectProducers());
        producerChoiceBox.setItems(producers);
        ObservableList<String> category = FXCollections.observableArrayList(GlobalData.selectCategory());
        categoryChoiceBox.setItems(category);

    }

    @FXML
    void okButtonClicked(ActionEvent event) throws Exception {
        String productName = enterName.getText();
        String productPrice = enterPrice.getText();
        Float castedPrice;
        String productProducer;
        String productCategory;

        if(productName.length() == 0 || productPrice.length() == 0
                || producerChoiceBox.getValue() == null || categoryChoiceBox.getValue() == null){
            Alert inapropiateNameOrPrice = new Alert(Alert.AlertType.WARNING);
            inapropiateNameOrPrice.setTitle("Brak danych");
            inapropiateNameOrPrice.setHeaderText(null);
            inapropiateNameOrPrice.setContentText("Brak danych produktu.\nProszę wprowadzić wszystkie dane.");
            System.out.println("Brak danych produktu");
            inapropiateNameOrPrice.showAndWait();
        }
        else{
            productProducer = producerChoiceBox.getValue().toString();
            productCategory = categoryChoiceBox.getValue().toString();
            System.out.println(producerChoiceBox.getValue().toString());
            try{
                productPrice = productPrice.replace(",",".");
                if(!checkIfPriceIsOK(productPrice)){
                    throw new NumberFormatException();
                }
                castedPrice = Float.parseFloat(productPrice);
            }
            catch(NumberFormatException e){
                castedPrice = -1.0f;
                Alert quitAlert = new Alert(Alert.AlertType.WARNING);
                quitAlert.setTitle("Niepoprawna cena");
                quitAlert.setHeaderText(null);
                quitAlert.setContentText("Cena, która została wprowadzona jest niepoprawna\nNależy wprowadzić poprawną cenę.");
                quitAlert.showAndWait();
            }
            if(castedPrice > 0) {
                GlobalData.insertNewProduct(productName, castedPrice, productProducer, productCategory);
                initialize();
                enterName.clear();
                enterPrice.clear();
                addedNewProductLabel.setOpacity(1.0);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(3000), addedNewProductLabel);
                fadeIn.setFromValue(1.0);
                fadeIn.setToValue(0.0);
                fadeIn.play();
            }
            else{
                Alert quitAlert = new Alert(Alert.AlertType.WARNING);
                quitAlert.setTitle("Niepoprawna cena");
                quitAlert.setHeaderText("Cena, która została wprowadzona jest niepoprawna, nie może być ujemna.");
                quitAlert.setContentText("Należy wprowadzić cenę, która ma dodatnią wartość.");
                quitAlert.showAndWait();
                enterPrice.clear();
            }
        }
    }

    @FXML
    void retrunButtonClicked(ActionEvent event) throws Exception {
        boolean exit = true;
        if(enterName.getText().length() !=  0 || enterPrice.getText().length() != 0
                || producerChoiceBox.getValue() != null || categoryChoiceBox.getValue() != null){
            QuitBox quitbox = new QuitBox();
            exit = quitbox.checkIfQuit();
        }
        if(exit){
            Parent root = FXMLLoader.load(getClass().getResource("chooseActionWindow.fxml"));
            Scene scene = new Scene(root, 1280, 720);
            Main.setWindow(scene);
        }
    }

    private boolean checkIfPriceIsOK(String price){
        boolean priceFormatOk = false;
        String[] properPrice = price.split(Pattern.quote("."));
        if(properPrice.length == 1){
            if(properPrice[0].length() < 5){
                priceFormatOk = true;
            }
        }
        else if(properPrice.length == 2){
            if(properPrice[0].length() < 5 && properPrice[1].length() < 3){
                priceFormatOk = true;
            }
        }
        return priceFormatOk;
    }

}
