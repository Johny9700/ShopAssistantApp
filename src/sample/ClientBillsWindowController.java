package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.event.ActionEvent;
import sample.entities.QuitBox;

import java.util.List;

public class ClientBillsWindowController {

    private VBox vBox;
    private VBox vBoxForClientBills;
    private Long chosenClientId;

    @FXML
    private TextField surnameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button clientFilterButton;

    @FXML
    private Button clientClearFiltersButton;

    @FXML
    private VBox billsVBox;


    @FXML
    private CheckBox receiptCheckBox;

    @FXML
    private CheckBox invoiceCheckBox;

    @FXML
    private Button billsFilterButton;

    @FXML
    private Button billsClearFiltersButton;

    @FXML
    private ScrollPane clientsScrollPane;

    @FXML
    private ScrollPane billsScrollPane;

    @FXML
    private Button returnButton;


    private void showClientData(){
        String typeOfBill;
        if(receiptCheckBox.isSelected() && invoiceCheckBox.isSelected()) {
            System.out.println("wszystko");
            typeOfBill = "both";
        }
        else if(receiptCheckBox.isSelected()) {
            System.out.println("paragony");
            typeOfBill = "receipt";
        }
        else if(invoiceCheckBox.isSelected()) {
            System.out.println("faktury");
            typeOfBill = "invoice";
        }
        else {
            System.out.println("nic");
            typeOfBill = "none";
        }
        List<com.sample.DowodySprzedazy> clientBills = GlobalData.selectClientBills(chosenClientId, typeOfBill);
        vBoxForClientBills.getChildren().clear();
        for(com.sample.DowodySprzedazy oneBill : clientBills) {
            HBox hBox = new HBox();
            hBox.setSpacing(20);
            hBox.setAlignment(Pos.CENTER);

            String billNumberText = "";
            if(oneBill.getNrFaktury() != null) billNumberText = oneBill.getNrFaktury();
            if(oneBill.getNrParagonu() != null) billNumberText = oneBill.getNrParagonu();

            Label idBill = new Label(Long.toString(oneBill.getIdDowoduSprzedazy()));
            Label billNumber = new Label(billNumberText);
            Label date = new Label(oneBill.getData().toString());
            Label kwota = new Label(oneBill.getKwota());

            idBill.setPrefWidth(40);
            date.setPrefWidth(120);
            billNumber.setPrefWidth(120);
            kwota.setPrefWidth(100);

            hBox.getChildren().addAll(idBill, billNumber, date, kwota);
            vBoxForClientBills.getChildren().add(hBox);
        }

    }

    private void showClientsOnVBox(List<com.sample.Klienci> clients) {
        vBox.getChildren().clear();
        for(com.sample.Klienci oneClient : clients) {
            HBox hBox = new HBox();
            hBox.setSpacing(20);
            hBox.setAlignment(Pos.CENTER);


            Label idLabel = new Label(Long.toString(oneClient.getIdKlienta()));
            Label nameLabel = new Label(oneClient.getImie());
            Label surnameLabel = new Label(oneClient.getNazwisko());
            Label emailLabel = new Label(oneClient.getEmail());
            idLabel.setPrefWidth(20);
            nameLabel.setPrefWidth(75);
            surnameLabel.setPrefWidth(115);
            emailLabel.setPrefWidth(125);
            hBox.getChildren().addAll(idLabel, nameLabel, surnameLabel, emailLabel);

            Button chooseClientButton = new Button("Wybierz");
            chooseClientButton.setOnAction(event1 -> {
                chosenClientId = oneClient.getIdKlienta(); // w tym polu trzymam ID
                System.out.println(chosenClientId);
                billsVBox.setDisable(false);
                showClientData();

            });
            hBox.getChildren().add(chooseClientButton);
            vBox.getChildren().add(hBox);
        }
    }





    private void filterAction() {
        String surnamePattern = surnameTextField.getText();
        String emailPattern = emailTextField.getText();

        List<com.sample.Klienci> clients = GlobalData.selectClients(emailPattern, surnamePattern);
        showClientsOnVBox(clients);
        clientClearFiltersButton.setDisable(false);
    }

    @FXML
    private void initialize() {
        surnameTextField.clear();
        emailTextField.clear();

        clientClearFiltersButton.setDisable(true);

        vBox = new VBox();

        vBox.setSpacing(10);
        vBox.setPadding(new Insets(0, 5, 0, 5));
        clientsScrollPane.setContent(vBox);

        vBoxForClientBills =  new VBox();
        vBoxForClientBills.setSpacing(10);
        vBoxForClientBills.setPadding(new Insets(0, 5, 0, 5));
        billsScrollPane.setContent(vBoxForClientBills);

        List<com.sample.Klienci> clients = GlobalData.selectClients("", "");
        showClientsOnVBox(clients);
    }

    @FXML
    void clientFilterButtonClicked(ActionEvent event) {
        filterAction();
    }

    @FXML
    void clientClearFiltersButtonClicked(ActionEvent event) {
        initialize();
    }

    @FXML
    void billsFilterButtonClicked(ActionEvent event) {
        billsClearFiltersButton.setDisable(false);
        showClientData();
    }

    @FXML
    void billsClearFiltersButtonClicked(ActionEvent event) {
        billsClearFiltersButton.setDisable(true);
        receiptCheckBox.setSelected(true);
        invoiceCheckBox.setSelected(true);
        showClientData();
    }

    @FXML
    void returnButtonClicked(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("chooseActionWindow.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);
    }

    @FXML
    void surnameTextFieldKeyPressed(KeyEvent keyEvent) {
        clientFilterButton.setDisable(false);
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            filterAction();
        }
    }

    @FXML
    void emailTextFieldKeyPressed(KeyEvent keyEvent) {
        clientFilterButton.setDisable(false);
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            filterAction();
        }
    }
}
