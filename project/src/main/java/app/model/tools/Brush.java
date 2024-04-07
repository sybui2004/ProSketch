package app.model.tools;

import app.model.Drawing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.ArcType;

public class Brush {

    private final Drawing drawing = new Drawing();
    public void setEventMousePressed(int sizeVal, GraphicsContext gc, MouseEvent e) {
        gc.fillArc(e.getX() - sizeVal, e.getY() - sizeVal, sizeVal, sizeVal, 0, 360, ArcType.ROUND);
    }
    public void setEventMouseDragged(double preX, double preY, int sizeVal, GraphicsContext gc,  MouseEvent e)
    {
        if (Math.abs(e.getX() - preX) > Math.max(sizeVal / 2, 1) || Math.abs(e.getY() - preY) > Math.max(sizeVal / 2, 1)) {
            drawing.drawExtraPoints(preX, preY, e.getX(), e.getY(), gc, sizeVal);
        }
        gc.fillArc(e.getX() - sizeVal, e.getY() - sizeVal, sizeVal, sizeVal, 0, 360, ArcType.ROUND);
    }

}
