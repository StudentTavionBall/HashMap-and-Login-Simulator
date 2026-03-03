/*
 * Course: CSC-1120
 * Lab Assignment 3 - Image Manipulator 3000
 * ImageDisplayer
 * Name: Tavion Ball
 * Last Updated: 02/04/2026
 */
package ballt;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Class that holds the GUI for calculator.fxml
 */
public class ImageManipulator3000 extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loaderLogin = new FXMLLoader(
                getClass().getResource("login.fxml"));
        Pane root = loaderLogin.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Login to Image Manipulator 3000");
        stage.show();

        FXMLLoader loaderPrimary = new FXMLLoader(
                getClass().getResource("imagemanipulator3000.fxml"));
        Pane root1 = loaderPrimary.load();
        Stage stage1 = new Stage();
        stage1.setScene(new Scene(root1));
        stage1.setTitle("Welcome to Image Manipulator 3000");

        FXMLLoader loaderSecondary = new FXMLLoader(
                getClass().getResource("filter.fxml"));
        Pane root2 = loaderSecondary.load();
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(root2));
        stage2.setTitle("Filter Kernel");

        LoginController controller = loaderLogin.getController();
        controller.setSecondStage(stage1);
        controller.setPrimaryStage(stage);
        Controller firstcontroller = loaderPrimary.getController();
        firstcontroller.setSecondStage(stage2);
        FilterController secondController = loaderSecondary.getController();
        secondController.setPrimaryController(firstcontroller);
        stage2.setOnCloseRequest(_->stage2.hide());
    }
}