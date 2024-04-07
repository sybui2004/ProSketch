package app.model.tools;

import app.model.Pixel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;

public class Filler {
    private final Pixel pixel = new Pixel();

    private void bucketFill(int x, int y, Color fillColor, Canvas canvas, GraphicsContext gc) {
        WritableImage snapshot = canvas.snapshot(null, null);
        PixelReader pixelReader = snapshot.getPixelReader();
        LinkedList<Pixel> queue = new LinkedList<>();
        queue.addLast(new Pixel(x, y));
        while (!queue.isEmpty()) {
            Pixel currentPixel = queue.pop();
            Color pixelColor = pixelReader.getColor(currentPixel.getX(), currentPixel.getY());
            if (!pixelColor.equals(fillColor)) {
                snapshot.getPixelWriter().setColor(currentPixel.getX(), currentPixel.getY(), fillColor);
                ArrayList<Pixel> neighbors = pixel.getPixelNeighbors(currentPixel.getX(), currentPixel.getY(), snapshot);
                for (var neighbor : neighbors) {
                    if (pixelReader.getColor(neighbor.getX(), neighbor.getY()).equals(pixelColor))
                        queue.addLast(neighbor);
                }
            }
        }
        gc.drawImage(snapshot, 0, 0);
    }
    public void setEventMousePressed(MouseEvent e, Color color, Canvas canvas, GraphicsContext gc)
    {
        if (e.isPrimaryButtonDown()) bucketFill((int) e.getX(), (int) e.getY(), color, canvas, gc);
    }
}
