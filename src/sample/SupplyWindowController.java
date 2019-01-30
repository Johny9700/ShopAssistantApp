package sample;

import com.sample.Pozycje;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sample.entities.QuitBox;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;

public class SupplyWindowController {

    private VBox vBox;
    private ObservableList<KeyValuePair> productsList;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ChoiceBox<String> producerChoiceBox;

    @FXML
    private Button addProductButton;

    @FXML
    private Button okButton;

    @FXML
    private Button returnButton;

    @FXML
    private void initialize() {
        ObservableList<String> producers = FXCollections.observableArrayList(GlobalData.selectProducers());
        producerChoiceBox.setItems(producers);
        producerChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                addProductButton.setDisable(false);
                productsList = FXCollections.observableArrayList(GlobalData.selectProducerProduct(producerChoiceBox.getValue()));
            }
        });

        vBox = new VBox();
        vBox.setSpacing(10);
        scrollPane.setContent(vBox);
    }

    @FXML
    void addProductButtonClicked(ActionEvent event) {
        producerChoiceBox.setDisable(true);

        HBox hBox = new HBox();
        hBox.setSpacing(20);

        ChoiceBox<KeyValuePair> product = new ChoiceBox<>();
        product.setPrefWidth(500);
        System.out.println(producerChoiceBox.getValue().toString());

        product.setItems(productsList);
        hBox.getChildren().add(product);

        Spinner<Integer> amount = new Spinner<>();
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1);
        amount.setValueFactory(valueFactory);
        hBox.getChildren().add(amount);

        Button removeProduct = new Button("Usuń");
        removeProduct.setOnAction(event1 -> {
            vBox.getChildren().remove(removeProduct.getParent());
            if(vBox.getChildren().isEmpty() == true) {
                producerChoiceBox.setDisable(false);
            }
        });
        hBox.getChildren().add(removeProduct);

        vBox.getChildren().add(hBox);
    }

    @FXML
    void okButtonClicked(ActionEvent event) {
        List<com.sample.Pozycje> positionList = new ArrayList<>();

        for(Node node : vBox.getChildrenUnmodifiable()) {
            HBox hBox = (HBox)node;
            com.sample.Pozycje onePosition = new Pozycje();
            boolean nullPosition = false;
            for(Node nnnn : hBox.getChildrenUnmodifiable()) {
                if(nnnn instanceof ChoiceBox) {
                    ChoiceBox ccc = (ChoiceBox)nnnn;
                    KeyValuePair chosenProduct = (KeyValuePair)ccc.getValue();
                    if(chosenProduct == null)
                    {
                        //break inner loop (product, amount HBox pair)
                        nullPosition = true;
                        break;
                    }
                    Long productId = chosenProduct.getKey();
                    String productName = chosenProduct.toString();
                    onePosition.setIdProduktu(productId);

                    System.out.println("produktID: " + productId);
                    System.out.println("produktName: " + productName);
                }
                if(nnnn instanceof Spinner) {
                    Spinner sss = (Spinner)nnnn;
                    SpinnerValueFactory valueFactory = sss.getValueFactory();
                    Object rawValue = valueFactory.getValue();
                    Integer IntValue = (Integer)rawValue;
                    onePosition.setIlosc(IntValue);
                    System.out.println("ilość: " + IntValue);
                }
            }
            if(!nullPosition) {
                positionList.add(onePosition);
            }

        }
        GlobalData.insertNewOrder(producerChoiceBox.getValue(), positionList);

        vBox.getChildren().removeAll(vBox.getChildren());
    }

    @FXML
    void returnButtonClicked(ActionEvent event) throws Exception {
        boolean exit = true;
        if(vBox.getChildren().isEmpty() == false){
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
