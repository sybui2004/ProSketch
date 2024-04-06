package app.model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class CanvasFX extends StackPane {
    private final Canvas canvas;
    private final GraphicsContext gc;
    private Image background;
    private double startX, startY;
    private double shapeX, shapeY, shapeWidth, shapeHeight;

    public CanvasFX(double width, double height) {
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        getChildren().add(new ImageView(String.valueOf(canvas)));

        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);
    }

    public void save(File file) {
        try {
            Image snapshot = canvas.snapshot(null, null);
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "PNG", file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void load(File file) {
        try {
            background = new Image(file.toURI().toString());
            gc.drawImage(background, 0, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setBackground(Image img) {
        background = img;
        gc.drawImage(background, 0, 0);
    }

    public void clear() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (background != null)
            gc.drawImage(background, 0, 0);
    }

    private void handleMousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
        shapeX = startX;
        shapeY = startY;
    }

    private void handleMouseDragged(MouseEvent e) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (background != null)
            gc.drawImage(background, 0, 0);
        gc.strokeRect(shapeX, shapeY, shapeWidth, shapeHeight);
        shapeWidth = e.getX() - startX;
        shapeHeight = e.getY() - startY;
        gc.strokeRect(shapeX, shapeY, shapeWidth, shapeHeight);
    }

    private void handleMouseReleased(MouseEvent e) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        if (background != null)
            gc.drawImage(background, 0, 0);
        gc.strokeRect(shapeX, shapeY, shapeWidth, shapeHeight);
    }
}
