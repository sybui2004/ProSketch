package app.model.shapes;

import app.model.typeEnum.ShapeType;
import app.model.ShapeFX;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle extends ShapeFX {

    public Rectangle(double startX, double startY, Color color) {
        super(startX, startY, color, ShapeType.RECT);
    }

    public Rectangle(double startX, double startY, Color color, ShapeType childShapeType) {
        super(startX, startY, color, childShapeType);

    }

    @Override
    public void drawShape(GraphicsContext gc, double drawX, double drawY, double drawWidth, double drawHeight, boolean isSelected) {
        gc.fillRect(drawX, drawY, drawWidth, drawHeight);
        if (!isSelected) {
            gc.strokeRect(drawX, drawY, drawWidth, drawHeight);
        }
    }

    @Override
    public boolean contains(double hitX, double hitY) {
        return (hitX >= getStartingPointX() && hitX <= getStartingPointX() + getWidth() && hitY >= getStartingPointY() && hitY <= getStartingPointY() + getHeight());
    }
}
