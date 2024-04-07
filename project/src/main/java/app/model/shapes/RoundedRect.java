package app.model.shapes;

import app.model.StatusCanvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import java.util.Stack;

public class RoundedRect {
    public void setEventMouseDragged(GraphicsContext gc, double preX, double preY, MouseEvent e, Stack<StatusCanvas> undoStack) {
        gc.drawImage(undoStack.peek().getImage(), 0, 0);
        gc.strokeRoundRect(preX, preY, Math.abs(e.getX() - preX), Math.abs(e.getY() - preY), Math.min(Math.abs(e.getX() - preX) / 5, 50), Math.min(Math.abs(e.getX() - preX) / 5, 50));
    }
}
