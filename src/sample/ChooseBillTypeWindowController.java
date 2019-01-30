package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class ChooseBillTypeWindowController {

    private VBox vBox;
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
    private ScrollPane clientsScrollPane;

    @FXML
    private Button receiptButton;

    @FXML
    private Button invoiceButton;

    @FXML
    private Button returnButton;


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
            idLabel.setPrefWidth(25);
            nameLabel.setPrefWidth(75);
            surnameLabel.setPrefWidth(115);
            emailLabel.setPrefWidth(125);
            hBox.getChildren().addAll(idLabel, nameLabel, surnameLabel, emailLabel);

            Button chooseClientButton = new Button("Wybierz");
            chooseClientButton.setOnAction(event1 -> {
                chosenClientId = oneClient.getIdKlienta(); // w tym polu trzymam ID
                System.out.println(chosenClientId);
                GlobalData.customerID = chosenClientId;
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
    void receiptButtonClicked(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("receiptWindow.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);
    }

    @FXML
    void invoiceButtonClicked(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("invoiceWindow.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);
    }

    @FXML
    void returnButtonClicked(ActionEvent actionEvent) throws Exception {
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
