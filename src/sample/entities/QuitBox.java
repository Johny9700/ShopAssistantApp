package sample.entities;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class QuitBox {

    public static boolean checkIfQuit(){
        boolean exit = true;
        Alert quitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        quitAlert.setTitle("Wyjście");
        quitAlert.setHeaderText(null);
        quitAlert.setContentText("Jeśli wyjdziesz stracisz wszystkie niezapisane dane.\nCzy na pewno chcesz wyjść?");
        ButtonType takBtn = new ButtonType("Tak");
        ButtonType nieBtn = new ButtonType("Nie");
        quitAlert.getButtonTypes().clear();
        quitAlert.getButtonTypes().addAll(takBtn, nieBtn);
        Optional<ButtonType> option = quitAlert.showAndWait();

        if(option.get() == nieBtn){
            exit = false;
        }
        return exit;
    }

    public static boolean removeProductConfirmation(){
        boolean remove = true;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Potwierdzeie usunięcia sprzedawcy");
        alert.setContentText("Czy na pewno chcesz usunąć produkt z oferty?");

        ButtonType takBtn = new ButtonType("Tak");
        ButtonType nieBtn = new ButtonType("Nie");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(takBtn, nieBtn);
        Optional<ButtonType> option = alert.showAndWait();

        if(option.get() == nieBtn){
            remove = false;
        }
        return remove;
    }

    public static boolean removeSellerConfirmation(){
        boolean remove = true;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Potwierdzeie zwolnienia sprzedawcy");
        alert.setContentText("Czy na pewno chcesz zwolnić sprzedawcę?");

        ButtonType takBtn = new ButtonType("Tak");
        ButtonType nieBtn = new ButtonType("Nie");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(takBtn, nieBtn);
        Optional<ButtonType> option = alert.showAndWait();

        if(option.get() == nieBtn){
            remove = false;
        }
        return remove;
    }

}
