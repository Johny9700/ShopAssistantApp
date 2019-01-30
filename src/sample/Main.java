package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLOutput;
import java.sql.Statement;

public class Main extends Application {

    private static Stage window;

    public static void setWindow(Scene scene) {
        window.setScene(scene);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        primaryStage.setTitle("Aplikacja sklepowa");
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        GlobalData.connectWithDatabase();
        launch(args);
        GlobalData.closeDatabeseConnection();

//        try {
//            Connection db = ConnectionConfiguration.getConnection();
//            Statement st = db.createStatement();
//            ResultSet rs = st.executeQuery("SELECT * FROM kategorie");
//            while (rs.next()) {
//                System.out.println(rs.getString("nazwa"));
//            }
//            rs.close();
//            st.close();
//            db.close();
//        } catch (java.sql.SQLException e) {
//            System.out.println(e.getMessage());
//        }

    }
}
