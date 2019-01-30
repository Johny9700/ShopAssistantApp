package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class ChooseActionWindowController {

    @FXML
    private Button addCategoryButton;

    @FXML
    private Button clientBillsButton;

    @FXML
    private Button inventoryButton;

    @FXML
    private Button addClientButton;

    @FXML
    private Button addProductButton;

    @FXML
    private Button supplyButton;

    @FXML
    private Button billButton;

    @FXML
    private Button returnButton;

    @FXML
    private Button removeProductButton;

    @FXML
    void supplyButtonClicked(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("supplyWindow.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);
    }

    @FXML
    void inventoryButtonClicked(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("inventoryWindow.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);

    }

    @FXML
    void addProductButtonClicked(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("addProductWindow.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);
    }

    @FXML
    void removeProductButtonClicked(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("removeProductWindow.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);
    }

    @FXML
    void addClientButtonClicked(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("addClientWindow.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);
    }

    @FXML
    void billButtonClicked(ActionEvent event) throws Exception {
        GlobalData.customerID = null;
        Parent root = FXMLLoader.load(getClass().getResource("chooseBillTypeWindow.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);
    }

    @FXML
    void addCategoryButtonClicked(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("addCategoryWindow.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);
    }

    @FXML
    void clientBillsButtonClicked(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("clientBillsWindow.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);
    }

    @FXML
    void returnButtonClicked(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);
    }
}