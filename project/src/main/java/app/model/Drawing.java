package app.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class Drawing {
    public void drawExtraPoints(double preX, double preY, double x, double y, GraphicsContext gc, int sizeVal) {
        if (Math.abs(x - preX) > Math.max(sizeVal / 2, 1) || Math.abs(y - preY) > Math.max(sizeVal / 2, 1)) {
            double newX = (int) ((x + preX) / 2);
            double newY = (int) ((y + preY) / 2);

            gc.fillArc(newX - sizeVal, newY - sizeVal, sizeVal, sizeVal, 0, 360, ArcType.ROUND);

            drawExtraPoints(preX, preY, newX, newY, gc, sizeVal);
            drawExtraPoints(newX, newY, x, y, gc, sizeVal);
        }
    }

    public void deleteExtraPoints(double preX, double preY, double x, double y, GraphicsContext gc, int sizeVal) {
        if (Math.abs(x - preX) > Math.max(sizeVal / 2, 1) || Math.abs(y - preY) > Math.max(sizeVal / 2, 1)) {

            double newX = (int) ((x + preX) / 2);
            double newY = (int) ((y + preY) / 2);

            gc.setFill(Color.WHITE);
            gc.setStroke(Color.WHITE);
            gc.fillRect(newX, newY, sizeVal, sizeVal);

            deleteExtraPoints(preX, preY, newX, newY, gc, sizeVal);
            deleteExtraPoints(newX, newY, x, y, gc, sizeVal);
        }
    }
}
