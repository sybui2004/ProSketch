package app.model.tools;

import app.model.Drawing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Eraser {
    private final Drawing drawing = new Drawing();
    public void setEventMousePressed(int sizeVal, GraphicsContext gc, MouseEvent e)
    {
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.fillRect(e.getX(), e.getY(), sizeVal, sizeVal);
    }
    public void setEventMouseDragged(int sizeVal, double preX, double preY, GraphicsContext gc, MouseEvent e)
    {
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.fillRect(e.getX(), e.getY(), sizeVal, sizeVal);
        if (Math.abs(e.getX() - preX) > Math.max(sizeVal / 2, 1) || Math.abs(e.getY() - preY) > Math.max(sizeVal / 2, 1)) {
            drawing.deleteExtraPoints(preX, preY, e.getX(), e.getY(), gc, sizeVal);
        }
    }
}
