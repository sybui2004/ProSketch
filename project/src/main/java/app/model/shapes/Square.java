package app.model.shapes;

import app.model.typeEnum.ShapeType;
import javafx.scene.paint.Color;

public class Square extends Rectangle {

    public Square(double startX, double startY, Color color) {
        super(startX,startY,color, ShapeType.SQUARE);
    }

    @Override
    public double getWidth() {
        return Math.min(super.getWidth(),super.getHeight());
    }
    @Override
    public double getHeight() {
        return Math.min(super.getWidth(),super.getHeight());
    }
}
