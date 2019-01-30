package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sample.entities.QuitBox;

import java.util.ArrayList;
import java.util.List;

public class FireSellerWindowController {

    private VBox vBox;

    @FXML
    private ChoiceBox<KeyValuePair> shopsChoiceBox;

    @FXML
    private TextField surnameTextField;

    @FXML
    private Button filterButton;

    @FXML
    private Button clearFiltersButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button returnButtton;

    private void showSellersOnVBox(List<com.sample.Sprzedawcy> sellers) {
        vBox.getChildren().clear();
        for(com.sample.Sprzedawcy oneSeller : sellers) {
            HBox hBox = new HBox();
            hBox.setSpacing(20);
            hBox.setAlignment(Pos.CENTER);


            Label idLabel = new Label(Long.toString(oneSeller.getIdSprzedawcy()));
            Label nameLabel = new Label(oneSeller.getImie());
            Label surnameLabel = new Label(oneSeller.getNazwisko());
            String phoneNumber = "--";
            if(oneSeller.getNrTelefonu() != 0) //to jest pusty numer telefonu
                phoneNumber = Long.toString(oneSeller.getNrTelefonu());
            Label phoneNumberLabel = new Label(phoneNumber);
            Label addressLabel = new Label(oneSeller.getAdres_sklepu());
            idLabel.setPrefWidth(20);
            nameLabel.setPrefWidth(150);
            surnameLabel.setPrefWidth(200);
            phoneNumberLabel.setPrefWidth(100);
            addressLabel.setPrefWidth(250);
            hBox.getChildren().addAll(idLabel, nameLabel, surnameLabel, phoneNumberLabel, addressLabel);

            Button fireSellerButton = new Button("Zwolnij");
            fireSellerButton.setOnAction(event1 -> {
                boolean removeSeller = true;
                QuitBox quitbox = new QuitBox();
                removeSeller = quitbox.removeSellerConfirmation();
                if(removeSeller == true) {
                    System.out.println("Zwalniam sprzedawcę o id = " + oneSeller.getIdSprzedawcy());
                    GlobalData.fireSeller(oneSeller.getIdSprzedawcy());
                    filterAction();
                }
            });
            hBox.getChildren().add(fireSellerButton);
            vBox.getChildren().add(hBox);
        }
    }

    private void filterAction() {
        filterButton.setDisable(true);
        Long shopID = null;
        if(shopsChoiceBox.getValue() != null)
            shopID = shopsChoiceBox.getValue().getKey();
        String pattern = surnameTextField.getText();

        List<com.sample.Sprzedawcy> sellers = GlobalData.selectSellersToFire(shopID, pattern);
        showSellersOnVBox(sellers);
        clearFiltersButton.setDisable(false);
    }

    @FXML
    private void initialize() {
        surnameTextField.clear();
        ObservableList<KeyValuePair> shops = FXCollections.observableArrayList(GlobalData.selectShops());
        shopsChoiceBox.setItems(shops);

        shopsChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<KeyValuePair>() {
            @Override
            public void changed(ObservableValue<? extends KeyValuePair> observable, KeyValuePair oldValue, KeyValuePair newValue) {
                filterButton.setDisable(false);
            }
        });

        filterButton.setDisable(true);
        clearFiltersButton.setDisable(true);

        vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(0, 10, 0, 20));
        scrollPane.setContent(vBox);

        List<com.sample.Sprzedawcy> sellers = GlobalData.selectSellersToFire(null, "");
        showSellersOnVBox(sellers);
    }

    @FXML
    void filterButtonClicked(ActionEvent actionEvent) {
        filterAction();
    }

    @FXML
    void clearFiltersButtonClicked(ActionEvent actionEvent) throws Exception {
        initialize();
    }

    @FXML
    void returnButttonClicked(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("managerActionsWindow.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);
    }

    @FXML
    void surnameTextFieldKeyPressed(KeyEvent keyEvent) {
        filterButton.setDisable(false);
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            filterAction();
        }
    }
}
