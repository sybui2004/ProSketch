package app.model.tools;

import javafx.scene.canvas.GraphicsContext;

public class Pencil {
    public void setEventMousePressed(double preX, double preY, GraphicsContext gc)
    {
        gc.strokeLine(preX, preY, preX, preY);
    }
    public void setEventMouseDragged(double preX, double preY, double aftX, double aftY, GraphicsContext gc)
    {
        gc.strokeLine(preX, preY, aftX, aftY);
    }
}
