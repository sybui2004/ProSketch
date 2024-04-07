package app.model.shapes;

import app.model.StatusCanvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import java.util.Stack;

public class Square {
    public void setEventMouseDragged(GraphicsContext gc, double preX, double preY, MouseEvent e, Stack<StatusCanvas> undoStack) {
        gc.drawImage(undoStack.peek().getImage(), 0, 0);
        gc.strokeRect(preX, preY, Math.abs(e.getX() - preX), Math.abs(e.getX() - preX));
    }
}
