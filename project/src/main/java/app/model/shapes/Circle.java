package app.model.shapes;

import app.model.typeEnum.ShapeType;
import app.model.ShapeFX;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Circle extends ShapeFX {

    public Circle(double startX, double startY, Color color) {
        super(startX, startY, color, ShapeType.CIRCLE);
    }

    @Override
    public double getWidth() {
        return Math.min(super.getWidth(), super.getHeight());
    }

    @Override
    public double getHeight() {
        return Math.min(super.getWidth(), super.getHeight());
    }

    @Override
    public void drawShape(GraphicsContext gc, double drawX, double drawY, double drawWidth, double drawHeight, boolean isSelected) {
        gc.fillOval(drawX, drawY, drawWidth, drawHeight);
        gc.strokeOval(drawX, drawY, drawWidth, drawHeight);

    }

    @Override
    public boolean contains(double hitX, double hitY) {
        double radius = getWidth() / 2;
        double centreX = getStartingPointX() + getWidth() / 2;
        double centreY = getStartingPointY() + getHeight() / 2;
        double distanceHitFromCenter = ShapeFX.getDistanceBetweenTwoPoints(centreX, centreY, hitX, hitY);
        return distanceHitFromCenter <= (radius + 0.0001f);
    }
}
