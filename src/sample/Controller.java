package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;

import java.util.List;

public class Controller {

    @FXML
    private Button okButton;

    @FXML
    private Button managerPanelButton;

    @FXML
    private ChoiceBox<KeyValuePair> shopsChoiceBox;

    @FXML
    private ChoiceBox<KeyValuePair> sellersChoiceBox;

    @FXML
    private void initialize() {
        System.out.println("first window initialize() called");
        GlobalData.shopID = null;
        GlobalData.sellerID = null;

        List<KeyValuePair> shops = GlobalData.selectShops();

        ObservableList<KeyValuePair> shopsAddresses = FXCollections.observableArrayList(shops);
        shopsChoiceBox.setItems(shopsAddresses);
        shopsChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<KeyValuePair>() {
            @Override
            public void changed(ObservableValue<? extends KeyValuePair> observable, KeyValuePair oldValue, KeyValuePair newValue) {
                GlobalData.shopID = newValue.getKey();
                GlobalData.sellerID = null;
                ObservableList<KeyValuePair> sellers = FXCollections.observableArrayList(GlobalData.selectSellers());
                sellersChoiceBox.setItems(sellers);
                sellersChoiceBox.setDisable(false); // prawie jak ' no shutdown ' żeby włączyć
                okButton.setDisable(true);
            }
        });
        sellersChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<KeyValuePair>() {
            @Override
            public void changed(ObservableValue<? extends KeyValuePair> observableValue, KeyValuePair s, KeyValuePair t1) {
                okButton.setDisable(false);
            }
        });
    }

    @FXML
    void okButtonClicked(ActionEvent event) throws Exception {
        Long shopID = shopsChoiceBox.getValue().getKey();
        Long sellerID = sellersChoiceBox.getValue().getKey();
//        System.out.println(shopID + "\t" + sellerID);
        GlobalData.sellerID = sellerID;
//        System.out.println(GlobalData.shopID);
//        System.out.println(GlobalData.sellerID);

        Parent root = FXMLLoader.load(getClass().getResource("chooseActionWindow.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);
    }

    @FXML
    void managerPanelButtonClicked(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("managerActionsWindow.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);
    }
}
