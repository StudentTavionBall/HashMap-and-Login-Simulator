/*
 * Course: CSC-1120
 * Personal Project 1 - Hashmaps
 * ballt.Hashmap
 * Name: Tavion Ball
 * Last Updated: 03/03/2026
 */
package ballt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * class to control the login screen
 */
public class LoginController {

    @FXML
    private PasswordField password;
    @FXML
    private TextField username;
    @FXML
    private Label incorrect;

    private final Hashmap<String, String> loginInfo = new Hashmap<>();
    private Stage secondStage;
    private Stage primaryStage;

    public void setSecondStage(Stage stage){
        secondStage = stage;
    }
    public void setPrimaryStage(Stage stage){
        primaryStage = stage;
    }

    @FXML
    private void login() {
        try {
            String key = username.getText();
            String value = password.getText();
            if (loginInfo.get(key) != null && loginInfo.get(key).equals(value)) {
                secondStage.show();
                primaryStage.hide();
            } else {
                incorrect.setText("Incorrect username or password please try again");
            }
        } catch (Exception e) {
            System.out.println("Whoops");
        }
    }

    @FXML
    private void register() {
        try {
            loginInfo.put(username.getText(), password.getText());
        } catch (Exception e) {
            System.out.println("Whoops");
        }
    }
}
