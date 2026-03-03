/*
 * Course: CSC-1120
 * Lab Assignment 4 - Image Manipulator 3000 Pt.2
 * ImageDisplayer
 * Name: Tavion Ball
 * Last Updated: 02/12/2026
 */
package ballt;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;


import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * class that handles the methods for the Controller class
 */
public class ImageIO {
    private static final int RED_OFFSET = 16;
    private static final int GREEN_OFFSET = 8;
    private static final int ALPHA_OFFSET = 24;
    private static final int MASK = 0x000000FF;
    private static final double SCALAR = 255.0;

    /**
     * A private constructor to forbid instantiating of the utility class
     * @throws AssertionError thrown if the constructor is called
     */
    private ImageIO() throws AssertionError {
        throw new AssertionError("ImageIO should not be instantiated");
    }

    /**
     * Method to read in jpg and png files and return an Image object
     * @param path of file
     * @return Image object of the file
     * @throws IllegalArgumentException if files type is not supported
     * @throws IOException if file is corrupt or path is bad
     */
    public static Image read(Path path) throws IllegalArgumentException, IOException {
        Image image;
        if(path==null){
            throw new IllegalArgumentException("Path of file cannot be null");
        }
        String ext = path.toString();
        ext = ext.substring(ext.lastIndexOf(".") + 1);
        if (!ext.equalsIgnoreCase("jpg") &&
                !ext.equalsIgnoreCase("png")) {
            if(ext.equalsIgnoreCase("msoe")){
                image = readMSOE(path);
            } else if(ext.equalsIgnoreCase("bmsoe")) {
                image = readBMSOE(path);
            } else{
                throw new IllegalArgumentException("File extension is not supported");
            }
        } else {
            image = ImageUtil.readImage(path);
        }
        return image;
    }

    /**
     * method to read in msoe files and return an Image object
     * @param path of file
     * @return Image object of the file
     * @throws IllegalArgumentException if files type is not supported
     * @throws IOException if file is corrupt or path is bad
     */
    private static Image readMSOE(Path path) throws IllegalArgumentException, IOException {
        if(path==null){
            throw new IllegalArgumentException("Path of file cannot be null");
        }
        String ext = path.toString();
        ext = ext.substring(ext.lastIndexOf(".") + 1);
        if (!ext.equalsIgnoreCase("msoe")) {
            throw new IllegalArgumentException("File extension is not supported");
        }
        Scanner in = new Scanner(Files.newInputStream(path));
        String currentLine = in.nextLine();
        if(!currentLine.equalsIgnoreCase("MSOE")){
            throw new IllegalArgumentException("File is invalid");
        }
        ArrayList<ArrayList<String>> lines = new ArrayList<>();
        int width = in.nextInt();
        int height = in.nextInt();
        in.nextLine();
        for(int i = 0; i<height; i++){
            if(!in.hasNextLine()){
                throw new IllegalArgumentException("Not correct height");
            }
            currentLine = in.nextLine();
            Scanner inner = new Scanner(currentLine);
            ArrayList<String> line = new ArrayList<>();
            for(int j = 0; j<width; j++){
                if(!inner.hasNext()){
                    throw new IllegalArgumentException("Too short width");
                }
                line.add(inner.next());
            }
            if(inner.hasNext()){
                throw new IllegalArgumentException("Too long width");
            }
            lines.add(line);
        }
        WritableImage image = new WritableImage(width, height);
        PixelWriter pw = image.getPixelWriter();
        int y = 0;
        for(ArrayList<String> line : lines){
            for(int x = 0; x <width; x++){
                pw.setColor(x, y, Color.web(line.get(x)));
            }
            y++;
        }
        return image;
    }

    private static Image readBMSOE(Path path) throws IOException, IllegalArgumentException{
        try(DataInputStream in = new DataInputStream(Files.newInputStream(path))) {
            final int bmsoeLength = 5;
            StringBuilder bmsoe = new StringBuilder();
            for (int i = 0; i < bmsoeLength; i++) {
                try {
                    bmsoe.append((char) in.readByte());
                } catch (EOFException e){
                    throw new IllegalArgumentException("Bad Header!");
                }
            }
            if(!String.valueOf(bmsoe).equalsIgnoreCase("bmsoe")){
                throw new IllegalArgumentException("Not correct format");
            }
            int width = in.readInt();
            int height = in.readInt();
            WritableImage wi = new WritableImage(width, height);
            PixelWriter pw = wi.getPixelWriter();
            for(int y = 0; y <height; y++){
                for(int x = 0; x <width; x++){
                    if(in.available()==0){
                        throw new IllegalArgumentException("Wrong file size!");
                    }
                    pw.setColor(x, y, intToColor(in.readInt()));
                }
            }
            if(in.available()>0){
                throw new IllegalArgumentException("File is not formatted " +
                        "correctly (Height && Width");
            }
            return wi;
        }
    }

    /**
     * Method to write an Image object to a png or jpg files
     * @param path of file to write to
     * @param image that we are going to write to the file
     * @throws IllegalArgumentException if extension is not a png or jpg
     * @throws IOException if file path is bad or cannot exist
     */
    public static void write(Path path, Image image) throws IllegalArgumentException, IOException{
        if(path==null || image==null){
            throw new IllegalArgumentException("Path of file cannot be null");
        }
        String ext = path.toString();
        ext = ext.substring(ext.lastIndexOf(".") + 1);
        if (!ext.equalsIgnoreCase("jpg") &&
                !ext.equalsIgnoreCase("png")) {
            if(ext.equalsIgnoreCase("msoe")){
                writeMSOE(path, image);
            } else if(ext.equalsIgnoreCase("bmsoe")) {
                writeBMSOE(path, image);
            } else{
                throw new IllegalArgumentException("File extension is not supported");
            }
        } else {
            ImageUtil.writeImage(path, image);
        }
    }

    /**
     * Method to write an Image object to a msoe file
     * @param path of file to write to
     * @param image that we are going to write to the file
     * @throws IllegalArgumentException if extension is not msoe
     * @throws IOException if file path is bad or cannot exist
     */
    private static void writeMSOE(Path path, Image image)
            throws IllegalArgumentException, IOException{
        if(path==null || image==null){
            throw new IllegalArgumentException("Path of file cannot be null");
        }
        final int hexNum = 255;
        String ext = path.toString();
        ext = ext.substring(ext.lastIndexOf(".") + 1);
        if (!ext.equalsIgnoreCase("msoe")) {
            throw new IllegalArgumentException("File extension is not supported");
        }
        try(PrintWriter pw = new PrintWriter(Files.newOutputStream(path))) {
            ArrayList<ArrayList<String>> lines = new ArrayList<>();
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            PixelReader pr = image.getPixelReader();
            for (int y = 0; y < height; y++) {
                ArrayList<String> colors = new ArrayList<>();
                for (int x = 0; x < width; x++) {
                    String hex = String.format("#%02X%02X%02X",
                            (int) (pr.getColor(x, y).getRed() * hexNum),
                            (int) (pr.getColor(x, y).getGreen() * hexNum),
                            (int) (pr.getColor(x, y).getBlue() * hexNum));
                    colors.add(hex);
                }
                lines.add(colors);
            }
            pw.println("MSOE");
            pw.println(width + " " + height);
            for (ArrayList<String> line : lines) {
                for (int i = 0; i < width; i++) {
                    if (i != (width - 1)) {
                        pw.print(line.get(i) + " ");
                    } else {
                        pw.print(line.get(i));
                    }
                }
                pw.print("\n");
            }
        }
    }

    private static void writeBMSOE(Path path, Image image)
            throws IOException, IllegalArgumentException{
        try(DataOutputStream dos = new DataOutputStream(Files.newOutputStream(path))) {
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            PixelReader pr = image.getPixelReader();
            dos.writeBytes("BMSOE");
            dos.writeInt(width);
            dos.writeInt(height);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    dos.writeInt(colorToInt(pr.getColor(x, y)));
                }
            }
        }
    }

    private static Color intToColor(int color) {
        double red = ((color >> RED_OFFSET) & MASK)/SCALAR;
        double green = ((color >> GREEN_OFFSET) & MASK)/SCALAR;
        double blue = (color & MASK)/SCALAR;
        double alpha = ((color >> ALPHA_OFFSET) & MASK)/SCALAR;
        return new Color(red, green, blue, alpha);
    }

    private static int colorToInt(Color color) {
        int red = ((int)(color.getRed() * SCALAR)) & MASK;
        int green = ((int)(color.getGreen() * SCALAR)) & MASK;
        int blue = ((int)(color.getBlue() * SCALAR)) & MASK;
        int alpha = ((int)(color.getOpacity() * SCALAR)) & MASK;
        return (alpha << ALPHA_OFFSET) + (red << RED_OFFSET) + (green << GREEN_OFFSET) + blue;
    }
}
