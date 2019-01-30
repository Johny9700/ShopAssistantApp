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
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import sample.entities.QuitBox;

import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.util.ArrayList;
import java.util.List;

public class InventoryWindowController {
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

    private void showProductsOnVBox(List<com.sample.Produkty> products) {
        vBox.getChildren().clear();
        HBox namesBox = new HBox();
        namesBox.setSpacing(60);
        namesBox.setAlignment(Pos.CENTER);

        Label nameDescriptionLabel = new Label("NAZWA");
        nameDescriptionLabel.setFont(Font.font(null, FontWeight.BOLD,12));
        Label amountDescriptionLabel = new Label("ILOŚĆ");
        amountDescriptionLabel.setFont(Font.font(null, FontWeight.BOLD,12));
        Label priceDescriptionLabel = new Label("CENA");
        priceDescriptionLabel.setFont(Font.font(null, FontWeight.BOLD,12));
        priceDescriptionLabel.setAlignment(Pos.CENTER_RIGHT);
        Label categoryDescriptionLabel = new Label("KATEGORIA");
        categoryDescriptionLabel.setFont(Font.font(null, FontWeight.BOLD,12));
        Label producerDescriptionLabel = new Label("PRODUCENT");
        producerDescriptionLabel.setFont(Font.font(null, FontWeight.BOLD,12));
        amountDescriptionLabel.setPrefWidth(40);
        nameDescriptionLabel.setPrefWidth(230);
        priceDescriptionLabel.setPrefWidth(100);
        categoryDescriptionLabel.setPrefWidth(150);
        producerDescriptionLabel.setPrefWidth(200);

        namesBox.getChildren().addAll(nameDescriptionLabel, amountDescriptionLabel, priceDescriptionLabel, categoryDescriptionLabel, producerDescriptionLabel);
        vBox.getChildren().add(namesBox);

        for(com.sample.Produkty oneProduct : products) {
            HBox hBox = new HBox();
            hBox.setSpacing(60);
            hBox.setAlignment(Pos.CENTER);


            Label nameLabel = new Label(oneProduct.getNazwa());
            Label amountLabel = new Label(Integer.toString(oneProduct.getIloscWSklepie()));
            Label priceLabel = new Label(oneProduct.getCena() + " zł");
            priceLabel.setAlignment(Pos.CENTER_RIGHT);
            Label categoryLabel = new Label(oneProduct.getKategorieNazwa());
            Label producerLabel = new Label(oneProduct.getNazwaFirmy());
            amountLabel.setPrefWidth(20);
            nameLabel.setPrefWidth(250);
            priceLabel.setPrefWidth(100);
            categoryLabel.setPrefWidth(150);
            producerLabel.setPrefWidth(200);
            hBox.getChildren().addAll(nameLabel, amountLabel, priceLabel, categoryLabel, producerLabel);

            vBox.getChildren().add(hBox);
        }
    }

    private void filterAction() {
        filterButton.setDisable(true);
        String category = categoryChoiceBox.getValue();
        String pattern = nameTextField.getText();

        List<com.sample.Produkty> products = GlobalData.selectProductsInShopFiltered(category, pattern);
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

        List<com.sample.Produkty> products = GlobalData.selectAllProductsToShow();
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
