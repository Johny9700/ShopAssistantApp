package sample;

import com.sample.Pozycje;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ReceiptWindowController {

    private VBox vBox;
    private VBox productVBox;
    private List<Long> productsOnBillIds;
    private double finalPrice;

    private ObservableList<String> category;

    @FXML
    private Label finalPriceLabel;

    @FXML
    private TextField nameTextField;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    @FXML
    private Button filterButton;

    @FXML
    private Button clearFiltersButton;

    @FXML
    private ScrollPane productsScrollPane;

    @FXML
    private ScrollPane productsOnBillScrollPane;

    @FXML
    private Button okButton;

    @FXML
    private Button returnButton;


    @FXML
    private void initialize() {
        finalPrice = 0;
        productsOnBillIds = new ArrayList<>();
        category = FXCollections.observableArrayList(GlobalData.selectCategory());

        categoryChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterButton.setDisable(false);
            }
        });

        vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(0, 10, 0, 20));
        productsScrollPane.setContent(vBox);

        productVBox = new VBox();
        productVBox.setSpacing(10);
        productVBox.setAlignment(Pos.CENTER);
        productVBox.setPadding(new Insets(0, 10, 0, 10));
        productsOnBillScrollPane.setContent(productVBox);

        clearFilters();
    }

    private void clearFilters() {
        nameTextField.clear();
        categoryChoiceBox.setItems(category);
        filterButton.setDisable(true);
        clearFiltersButton.setDisable(true);
        List<com.sample.Produkty> products = GlobalData.selectAllAvailableProducts(null, "");
        showProductsOnVBox(products);
    }

    @FXML
    void filterButtonClicked(ActionEvent actionEvent) {
        filterAction();
    }

    @FXML
    void clearFiltersButtonClicked(ActionEvent actionEvent) {
        clearFilters();
    }

    @FXML
    void okButtonClicked(ActionEvent actionEvent) {
        if(productVBox.getChildrenUnmodifiable().isEmpty()) {
            Alert notEnoughData = new Alert(Alert.AlertType.WARNING);
            notEnoughData.setTitle("Brak produktów");
            notEnoughData.setHeaderText(null);
            notEnoughData.setContentText("Proszę dodać produkty.");
            notEnoughData.showAndWait();
            return;
        }
        String endInformation = "";
        String endString = "";
        String receiptNumber = GlobalData.getBillNumber("P");
        List<com.sample.Pozycje> boughtProduct= new ArrayList<>();
        int evidenceID;
        for(Node node : productVBox.getChildrenUnmodifiable()) {
            HBox hBox = (HBox)node;
            com.sample.Pozycje oneProduct = new Pozycje();
            boolean nullPosition = false;
            int i = 1;
            for(Node nnnn : hBox.getChildrenUnmodifiable()) {
                if(i == 6 && nnnn instanceof Label){
                    Label idLabel = (Label)nnnn;
                    System.out.println(idLabel.getText());
                    oneProduct.setIdProduktu(Long.parseLong(idLabel.getText()));
                }else if(i == 1 && nnnn instanceof Label){
                    Label nameLabel = (Label)nnnn;
                    endInformation = endInformation + nameLabel.getText() + "...";
                }else if(i == 2 && nnnn instanceof Label){
                    Label priceLabel = (Label)nnnn;
                    endString = priceLabel.getText();
                }else if(nnnn instanceof Spinner) {
                    Spinner sss = (Spinner)nnnn;
                    SpinnerValueFactory valueFactory = sss.getValueFactory();
                    Object rawValue = valueFactory.getValue();
                    Integer IntValue = (Integer) rawValue;
                    oneProduct.setIlosc(IntValue);
                    System.out.println("ilość: " + IntValue);
                    endInformation = endInformation + IntValue + "..." +endString +"\n";
                }
                i++;
            }
            if(!nullPosition) {
                boughtProduct.add(oneProduct);
            }
        }
        String adequatePrice = finalPriceLabel.getText();
        adequatePrice = adequatePrice.replace(",",".");

        GlobalData.insertNewSaleEvidenceP(receiptNumber, null, null, Double.parseDouble(adequatePrice), boughtProduct);

        System.out.println(receiptNumber);
        endInformation = endInformation + "Suma: " + finalPriceLabel.getText() +" zł";
        Alert quitAlert = new Alert(Alert.AlertType.INFORMATION);
        quitAlert.setTitle("Wystawiono paragon");
        quitAlert.setHeaderText("Wystawiono paragon o numerze: " + receiptNumber);
        quitAlert.setContentText(endInformation);
        quitAlert.showAndWait();

        productVBox.getChildren().removeAll(productVBox.getChildren());
        productsOnBillIds.clear();
        finalPriceLabel.setText("0,00");
    }

    @FXML
    void returnButtonClicked(ActionEvent actionEvent) throws Exception {
        GlobalData.customerID = null;
        Parent root = FXMLLoader.load(getClass().getResource("chooseBillTypeWindow.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);
    }

    private void showProductsOnVBox(List<com.sample.Produkty> products) {
        vBox.getChildren().clear();
        HBox namesBox = new HBox();
        namesBox.setSpacing(30);
        namesBox.setAlignment(Pos.CENTER);

        Label nameDescriptionLabel = new Label("NAZWA");
        nameDescriptionLabel.setFont(Font.font(null, FontWeight.BOLD,12));
        Label priceDescriptionLabel = new Label("CENA");
        priceDescriptionLabel.setFont(Font.font(null, FontWeight.BOLD,12));
        priceDescriptionLabel.setAlignment(Pos.CENTER_RIGHT);
        Label producerDescriptionLabel = new Label("PRODUCENT");
        producerDescriptionLabel.setFont(Font.font(null, FontWeight.BOLD,12));
        Label addProductLabel = new Label("DODAJ");
        addProductLabel.setFont(Font.font(null, FontWeight.BOLD,12));
        nameDescriptionLabel.setPrefWidth(140);
        priceDescriptionLabel.setPrefWidth(80);
        producerDescriptionLabel.setPrefWidth(100);
        addProductLabel.setPrefWidth(60);

        namesBox.getChildren().addAll(nameDescriptionLabel, priceDescriptionLabel, producerDescriptionLabel, addProductLabel);
        vBox.getChildren().add(namesBox);

        for(com.sample.Produkty oneProduct : products) {
            HBox hBox = new HBox();
            hBox.setSpacing(30);
            hBox.setAlignment(Pos.CENTER);


            Label nameLabel = new Label(oneProduct.getNazwa());
            Label priceLabel = new Label(oneProduct.getCena() + " zł");
            priceLabel.setAlignment(Pos.CENTER_RIGHT);
            Label producerLabel = new Label(oneProduct.getNazwaFirmy());
            nameLabel.setPrefWidth(150);
            priceLabel.setPrefWidth(80);
            producerLabel.setPrefWidth(100);
            Button chooseProductButton = new Button("Dodaj");
            chooseProductButton.setOnAction(event1 -> {
                if(productsOnBillIds.contains(oneProduct.getIdProduktu())) {
                    System.out.println("TODO okienko z napisem że się nie da dodać tego samego");
                    Alert notEnoughData = new Alert(Alert.AlertType.WARNING);
                    notEnoughData.setTitle("Produkt już dodany");
                    notEnoughData.setHeaderText(null);
                    notEnoughData.setContentText("Nie możesz dodać ponownie tego samego produktu\nZamiast tego zwiększ liczbę sztuk.");
                    notEnoughData.showAndWait();
                    return;
                }
                else {
                    String changedPrice = oneProduct.getCena();
                    changedPrice = changedPrice.replace(",", ".");
                    System.out.println(changedPrice);
                    finalPrice = finalPrice + Double.parseDouble(changedPrice);
                    DecimalFormat df = new DecimalFormat("0.00");
                    finalPriceLabel.setText(String.valueOf(df.format(finalPrice)));
                    System.out.println(finalPrice);
                    addChosenProduct(oneProduct);
                }
            });

            hBox.getChildren().addAll(nameLabel, priceLabel, producerLabel, chooseProductButton);

            vBox.getChildren().add(hBox);
        }
    }

    private void filterAction() {
        filterButton.setDisable(true);
        String category = categoryChoiceBox.getValue();
        String pattern = nameTextField.getText();

        List<com.sample.Produkty> products = GlobalData.selectAllAvailableProducts(category, pattern);
        showProductsOnVBox(products);
        clearFiltersButton.setDisable(false);
    }

    private void addChosenProduct(com.sample.Produkty chosenProduct){
        productsOnBillIds.add(chosenProduct.getIdProduktu());

        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);

        Label idLabel = new Label(String.valueOf(chosenProduct.getIdProduktu()));
        idLabel.setPrefWidth(0);
        Label nameLabel = new Label(chosenProduct.getNazwa());
        Label priceLabel = new Label(chosenProduct.getCena() + " zł");
        priceLabel.setAlignment(Pos.CENTER_RIGHT);
        Label producerLabel = new Label(chosenProduct.getNazwaFirmy());
        nameLabel.setPrefWidth(130);
        priceLabel.setPrefWidth(80);
        producerLabel.setPrefWidth(90);

        Spinner<Integer> amount = new Spinner<>();
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, chosenProduct.getIloscWSklepie(), 1);
        amount.setValueFactory(valueFactory);
        amount.setPrefWidth(60);
        amount.valueProperty().addListener(((observable, oldValue, newValue) -> {
            String unitPrice = chosenProduct.getCena();
            unitPrice = unitPrice.replace(",", ".");
            finalPrice = finalPrice + (newValue-oldValue)*Double.parseDouble(unitPrice);
            DecimalFormat df = new DecimalFormat("0.00");
            finalPriceLabel.setText(String.valueOf(df.format(finalPrice)));
        }));

        Button deleteProductButton = new Button("Usuń");
        deleteProductButton.setOnAction(event1 -> {
            productVBox.getChildren().remove(deleteProductButton.getParent());
            String changedPrice = chosenProduct.getCena();
            changedPrice = changedPrice.replace(",", ".");
            System.out.println(changedPrice);
            finalPrice = finalPrice - Double.parseDouble(changedPrice)*amount.getValue();
            DecimalFormat df = new DecimalFormat("0.00");
            finalPriceLabel.setText(String.valueOf(df.format(finalPrice)));
            productsOnBillIds.remove(chosenProduct.getIdProduktu());
        });
        hBox.getChildren().addAll(nameLabel, priceLabel, producerLabel, amount, deleteProductButton, idLabel);

        productVBox.getChildren().add(hBox);
    }

    @FXML
    void nameTextFieldKeyPressed(KeyEvent keyEvent) {
        filterButton.setDisable(false);
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            filterAction();
        }
    }

}
