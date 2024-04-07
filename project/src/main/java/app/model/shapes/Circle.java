package app.model.shapes;

import app.model.StatusCanvas;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import java.util.Stack;

public class Circle{

    private void drawCircle(double startX, double startY, double endX, double endY, Canvas canvas, Stack<StatusCanvas> undoStack, GraphicsContext gc) {
        double radius = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(undoStack.peek().getImage(), 0, 0);
        gc.strokeOval(startX - radius, startY - radius, radius * 2, radius * 2);
    }

    public void setEventMouseDragged(GraphicsContext gc, double preX, double preY, MouseEvent e, Stack<StatusCanvas> undoStack, Canvas canvas) {
        gc.drawImage(undoStack.peek().getImage(), 0, 0);
        drawCircle(preX, preY, e.getX(), e.getY(), canvas, undoStack, gc);
    }

}
