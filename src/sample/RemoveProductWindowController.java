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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RemoveProductWindowController {

    private VBox vBox;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button filterButton;

    @FXML
    private Button clearFiltersButton;

    @FXML
    private Button returnButtton;

    private void showProductsOnVBox(List<com.sample.Produkty> produkts) {
        vBox.getChildren().clear();
        for(com.sample.Produkty oneProduct : produkts) {
            HBox hBox = new HBox();
            hBox.setSpacing(20);
            hBox.setAlignment(Pos.CENTER);

            //TODO wywalić, to jest tylko ramka żeby było łatwiej rozmieścić elementy
            String cssLayout = "-fx-border-color: green;\n" +
                    "-fx-border-insets: 4;\n" +
                    "-fx-border-width: 2;\n" +
                    "-fx-border-style: dashed;\n";

            hBox.setStyle(cssLayout);

            Label idLabel = new Label(Long.toString(oneProduct.getIdProduktu()));
            Label nameLabel = new Label(oneProduct.getNazwa());
            Label priceLabel = new Label(oneProduct.getCena() + " zł");
            priceLabel.setAlignment(Pos.CENTER_RIGHT);
            Label categoryLabel = new Label(oneProduct.getKategorieNazwa());
            Label producerLabel = new Label(oneProduct.getNazwaFirmy());
            idLabel.setPrefWidth(20);
            nameLabel.setPrefWidth(250);
            priceLabel.setPrefWidth(100);
            categoryLabel.setPrefWidth(150);
            producerLabel.setPrefWidth(200);
            hBox.getChildren().addAll(idLabel, nameLabel, priceLabel, categoryLabel, producerLabel);

            Button removeProductButton = new Button("Usuń z oferty");
            removeProductButton.setOnAction(event1 -> {
                boolean removeItem = true;
                QuitBox quitbox = new QuitBox();
                removeItem = quitbox.removeProductConfirmation();
                if(removeItem == true) {
                    System.out.println("Usuwanie produktu o id = " + oneProduct.getIdProduktu());
                    GlobalData.removeProduct(oneProduct.getIdProduktu());
                    filterAction(); //akurat to możemy zawsze wykonać i tu pasuje
                }
            });
            hBox.getChildren().add(removeProductButton);
            vBox.getChildren().add(hBox);
        }
    }

    private void filterAction() {
        filterButton.setDisable(true);
        String category = categoryChoiceBox.getValue();
        String pattern = nameTextField.getText();

        List<com.sample.Produkty> products = GlobalData.selectProductsFiltered(category, pattern);
        showProductsOnVBox(products);
        clearFiltersButton.setDisable(false);
    }

    @FXML
    private void initialize() {
        nameTextField.clear();
        ObservableList<String> category = FXCollections.observableArrayList(GlobalData.selectCategory());
        categoryChoiceBox.setItems(category);

        categoryChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterButton.setDisable(false);
            }
        });

        filterButton.setDisable(true);
        clearFiltersButton.setDisable(true);

        vBox = new VBox();

        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(0, 10, 0, 20));
        scrollPane.setContent(vBox);

        List<com.sample.Produkty> products = GlobalData.selectAllProducts();
        showProductsOnVBox(products);
    }

    @FXML
    void filterButtonClicked(ActionEvent actionEvent) {
        filterAction();
    }

    @FXML
    void clearFiltersButtonClicked(ActionEvent actionEvent) {
        initialize();
    }

    @FXML
    void returnButttonClicked(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("chooseActionWindow.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        Main.setWindow(scene);
    }

    @FXML
    void nameTextFieldKeyPressed(KeyEvent keyEvent) {
        filterButton.setDisable(false);
        if(keyEvent.getCode().equals(KeyCode.ENTER)) {
            filterAction();
        }
    }
}
