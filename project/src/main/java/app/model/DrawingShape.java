package app.model;

import app.model.typeEnum.NotificationType;
import app.model.typeEnum.ShapeType;
import app.model.Interface.IDrawingModelListener;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class DrawingShape {
    private Color selectedColor;
    private ShapeType selectedShape;
    private final ArrayList<ShapeFX> allShapes;

    private final ArrayList<IDrawingModelListener> subscribers;

    public DrawingShape() {
        subscribers = new ArrayList<>();
        allShapes = new ArrayList<>();
        selectedColor = null;
        selectedShape = null;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public ShapeType getSelectedShape() {
        return selectedShape;
    }

    public void addNewShape(ShapeFX shape) {
        allShapes.add(shape);
    }

    public ArrayList<ShapeFX> getAllShapes() {
        return allShapes;
    }

    private void notifySubscribers(NotificationType notificationType) {
        subscribers.forEach(subscriber -> subscriber.onDrawingModelChange(notificationType));
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = (this.selectedColor == selectedColor) ? null : selectedColor;
        notifySubscribers(NotificationType.COLOR_CHANGE);
    }

    public void setSelectedShape(ShapeType selectedShape) {
        this.selectedShape = (this.selectedShape == selectedShape) ? null : selectedShape;
        notifySubscribers(NotificationType.SHAPE_CHANGE);
    }

    public void addSubscriber(IDrawingModelListener subscriber) {
        subscribers.add(subscriber);
    }

    public void bringShapeToTop(ShapeFX shape) {
        if (shape != null) {
            allShapes.remove(shape);
            allShapes.add(shape);
        }
    }

    public ShapeFX contains(double hitX, double hitY) {

        for (int i = allShapes.size() - 1; i >= 0; i--) {
            if (allShapes.get(i).contains(hitX, hitY)) return allShapes.get(i);
        }
        return null;
    }


    public void removeShape(ShapeFX shape) {
        allShapes.remove(shape);
        notifySubscribers(NotificationType.SHAPE_DELETED);
    }
}
