/*
 * Course: CSC-1120
 * Lab Assignment 3 - Image Manipulator 3000
 * ImageDisplayer
 * Name: Tavion Ball
 * Last Updated: 02/04/2026
 */
package ballt;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

/**
 * class that controls the actionEvents passed in the imagemanipulator3000.fxml
 */
public class Controller {
    @FXML
    private Button filter;
    @FXML
    private ImageView imageView;

    private Image imageLoaded;
    private Stage secondStage;
    private final FileChooser chooser = new FileChooser();

    public void setSecondStage(Stage stage){
        secondStage = stage;
    }

    @FXML
    private void open() {
        try {
            chooser.setTitle("Open Resource File");
            Path path = chooser.showOpenDialog(null).toPath();
            imageLoaded = ImageIO.read(path);
            imageView.setImage(imageLoaded);
        } catch(IllegalArgumentException e){
            alerting(e.getMessage());
        } catch(IOException e){
            alerting("File cannot exist or does not exist for some reason");
        } catch (NullPointerException e){
            alerting("No file selected!");
        }
    }

    @FXML
    private void save() {
        if(imageLoaded!=null) {
            try {
                chooser.setTitle("Open Resource File");
                Path path = chooser.showSaveDialog(null).toPath();
                ImageIO.write(path, imageView.getImage());
            } catch (IllegalArgumentException e) {
                alerting(e.getMessage());
            } catch (IOException e) {
                alerting("File cannot exist or does not exist for some reason.");
            } catch (NullPointerException e) {
                alerting("No file selected!");
            }
        } else{
            alerting("No file loaded!");
        }
    }

    @FXML
    private void reload() {
        if(imageLoaded!=null) {
            imageView.setImage(imageLoaded);
        } else {
            alerting("No image loaded yet!");
        }
    }

    private Image transformImage(Image image, Transformable transform) {
        WritableImage wi = new WritableImage((int) image.getWidth(),
                (int) image.getHeight());
        PixelReader pr = image.getPixelReader();
        PixelWriter pw = wi.getPixelWriter();
        for (int i = 0; i < (int)image.getHeight(); i++) {
            for (int j = 0; j < (int)image.getWidth(); j++) {
                pw.setColor(j, i, transform.apply(i, pr.getColor(j, i)));
            }
        }
        return wi;
    }

    @FXML
    private void grayscale() {
        if(imageLoaded!=null) {
            imageView.setImage(transformImage(imageView.getImage(), (y, color) -> {
                final double opacity = 1.0;
                final double redGray = 0.21;
                final double greenGray = 0.71;
                final double blueGray = 0.07;
                double gray = redGray * color.getRed()
                        + greenGray * color.getGreen()
                        + blueGray * color.getBlue();
                return new Color(gray, gray, gray, opacity);
            }));
        } else {
            alerting("No image loaded yet!");
        }
    }

    @FXML
    private void negative() {
        if(imageLoaded!=null){
            imageView.setImage(transformImage(imageView.getImage(), (y, color) ->{
                final double opacity = 1.0;
                return new Color(
                        1.0 - color.getRed(),
                        1.0 - color.getGreen(),
                        1.0 - color.getBlue(),
                        opacity);
            }));
        } else {
            alerting("No image loaded yet!");
        }
    }
    @FXML
    private void red() {
        if(imageLoaded!=null){
            imageView.setImage(transformImage(imageView.getImage(), (y, color) ->{
                final double opacity = 1.0;
                return new Color(color.getRed(), 0.0, 0.0, opacity); }));
        } else {
            alerting("No image loaded yet!");
        }
    }

    @FXML
    private void redGray() {
        if(imageLoaded!=null){
            imageView.setImage(transformImage(imageView.getImage(), (y, color) -> {
                final double opacity = 1.0;
                final double redGray = 0.21;
                final double greenGray = 0.71;
                final double blueGray = 0.07;
                double gray = redGray * color.getRed()
                        + greenGray * color.getGreen()
                        + blueGray * color.getBlue();
                if(y%2==0) {
                    return new Color(gray, gray, gray, opacity);
                } else {
                    return new Color(color.getRed(), 0.0, 0.0, opacity);
                }
            }));
        } else {
            alerting("No image loaded yet!");
        }
    }

    private static void alerting(String s){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, s);
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(_ -> alert.close());
    }
    @FXML
    private void filterVisibility() {
        if(imageLoaded!=null) {
            if (secondStage.isShowing()) {
                secondStage.hide();
                filter.setText("Show Filter");
            } else {
                secondStage.show();
                filter.setText("Hide Filter");
            }
        } else{
            alerting("No image loaded yet!");
        }
    }

    /**
     * Takes in an int[][] and applies a filter using that array
     * using the ImageFilter class
     * @param kernel filter to apply
     * @throws IllegalArgumentException if sum of kernel is 0
     */
    public void applyFilter(int[][] kernel) throws IllegalArgumentException{
        imageView.setImage(ImageFilter.applyKernel(imageView.getImage(), kernel));
    }
}
