/*
 * Course: CSC-1120
 * Lab Assignment 3 - Image Manipulator 3000
 * ImageDisplayer
 * Name: Tavion Ball
 * Last Updated: 02/04/2026
 */
package ballt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class to handle the events of filter.fxml
 */
public class FilterController implements Initializable {
    private final TextField[][] kernel = new TextField[3][3];
    @FXML
    private TextField t1;
    @FXML
    private TextField t2;
    @FXML
    private TextField t3;
    @FXML
    private TextField t4;
    @FXML
    private TextField t5;
    @FXML
    private TextField t6;
    @FXML
    private TextField t7;
    @FXML
    private TextField t8;
    @FXML
    private TextField t9;
    @FXML
    private Controller controller;

    @FXML
    private void updateFilterValues(ActionEvent event){
        Button pressed = (Button) event.getSource();
        if(pressed.getText().equals("Blur")){
            kernel[0][0].setText("0");
            kernel[0][1].setText("1");
            kernel[0][2].setText("0");
            kernel[1][0].setText("1");
            kernel[1][1].setText("5");
            kernel[1][2].setText("1");
            kernel[2][0].setText("0");
            kernel[2][1].setText("1");
            kernel[2][2].setText("0");
        } else if(pressed.getText().equals("Sharpen")){
            kernel[0][0].setText("0");
            kernel[0][1].setText("-1");
            kernel[0][2].setText("0");
            kernel[1][0].setText("-1");
            kernel[1][1].setText("5");
            kernel[1][2].setText("-1");
            kernel[2][0].setText("0");
            kernel[2][1].setText("-1");
            kernel[2][2].setText("0");
        }
    }

    @FXML
    private void apply(){
        try {
            int[][] kernel = new int[3][3];
            kernel[0][0] = Integer.parseInt(t1.getText());
            kernel[0][1] = Integer.parseInt(t2.getText());
            kernel[0][2] = Integer.parseInt(t3.getText());
            kernel[1][0] = Integer.parseInt(t4.getText());
            kernel[1][1] = Integer.parseInt(t5.getText());
            kernel[1][2] = Integer.parseInt(t6.getText());
            kernel[2][0] = Integer.parseInt(t7.getText());
            kernel[2][1] = Integer.parseInt(t8.getText());
            kernel[2][2] = Integer.parseInt(t9.getText());
            controller.applyFilter(kernel);
        } catch (NumberFormatException e){
            alerting("Must be integer values!!!");
        } catch (IllegalArgumentException e){
            alerting(e.getMessage());
        }

    }

    public void setPrimaryController(Controller controller) {
        this.controller = controller;
    }

    private static void alerting(String s){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, s);
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(_ -> alert.close());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        kernel[0][0] = t1;
        kernel[0][1] = t2;
        kernel[0][2] = t3;
        kernel[1][0] = t4;
        kernel[1][1] = t5;
        kernel[1][2] = t6;
        kernel[2][0] = t7;
        kernel[2][1] = t8;
        kernel[2][2] = t9;
    }
}
