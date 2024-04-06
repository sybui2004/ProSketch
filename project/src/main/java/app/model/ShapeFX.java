package app.model;

import app.model.typeEnum.ShapeType;
import app.model.shapes.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class ShapeFX {
    protected double startX, startY, endX, endY;
    protected Color shapeColor;
    protected final Color selectedShapeColor = Color.RED;
    protected final double RESIZE_CIRCLE_RADIUS = 0.002f;
    protected final Color RESIZE_CIRCLE_COLOR = Color.YELLOW;

    private final ShapeType shapeType;
    //private ResizeCircle resizeCircle;

    protected double width;
    protected double height;

    public ShapeFX(double startX, double startY, Color color, ShapeType shapeType) {
        this.startX = startX;
        this.startY = startY;
        this.endX = startX;
        this.endY = startY;
        this.shapeColor = color;
        this.shapeType = shapeType;
    }


    public double getWidth() {
        return Math.abs(width);
    }

    public double getHeight() {
        return Math.abs(height);
    }

    public double getStartingPointX() {
        if (endX < startX) {
            return startX - getWidth();
        }
        return startX;
    }

    public double getStartingPointY() {
        if (endY < startY) {
            return startY - getHeight();
        }
        return startY;
    }
    public ShapeType getShapeType() {
        return shapeType;
    }


    public void updateEndPoints(double dX, double dY) {
        width += dX;
        height += dY;
        endX = startX + width;
        endY = startY + height;
//        if (resizeCircle == null) {
//            resizeCircle = new ResizeCircle(startX, startY, RESIZE_CIRCLE_COLOR);
//        }
        updateResizeCircle();
    }


    private void updateResizeCircle() {

        //resizeCircle.setAllPoints(getStartingPointX() + getWidth() - RESIZE_CIRCLE_RADIUS, getStartingPointY() + getHeight() - RESIZE_CIRCLE_RADIUS, getStartingPointX() + getWidth() + RESIZE_CIRCLE_RADIUS, getStartingPointY() + getHeight() + RESIZE_CIRCLE_RADIUS);
    }

    public void draw(GraphicsContext gc, double canvasWidth, double canvasHeight, double viewPortX, double viewPortY, boolean isSelected, boolean isCanvas) {
        gc.save();
        if (isCanvas) {
            gc.setLineWidth(3f);
        } else {
            gc.setLineWidth(0.5f);
            gc.setStroke(Color.BLACK);
            viewPortX = 0;
            viewPortY = 0;

        }
        double drawStartX = getStartingPointX() * canvasWidth - viewPortX;
        double drawStartY = getStartingPointY() * canvasHeight - viewPortY;
        double drawWidth = getWidth() * canvasWidth;
        double drawHeight = getHeight() * canvasHeight;
        if (shapeType == ShapeType.LINE) {
            gc.setStroke(getShapeColor());
            drawShape(gc, startX * canvasWidth - viewPortX, startY * canvasHeight - viewPortY, endX * canvasWidth - viewPortX, endY * canvasHeight - viewPortY, isSelected);
        } else {
            gc.setFill(getShapeColor());
            drawShape(gc, drawStartX, drawStartY, drawWidth, drawHeight, isSelected);
        }
        if (isCanvas) {
            if (isSelected) {
                gc.save();
                gc.setLineDashes(8);
                strokeSelected(gc, drawStartX, drawStartY, drawWidth, drawHeight);
                gc.restore();
//                if (resizeCircle != null)
//                    resizeCircle.draw(gc, canvasWidth, canvasHeight, viewPortX, viewPortY, false, true);
            }
        } else {
            if (isSelected) {
                strokeSelected(gc, drawStartX, drawStartY, drawWidth, drawHeight);
            }
        }

        gc.restore();
    }

    protected void strokeSelected(GraphicsContext gc, double startX, double startY, double width, double height) {
        gc.save();
        gc.setStroke(selectedShapeColor);
        gc.strokeRect(startX, startY, width, height);
        gc.setStroke(Color.BLACK);
        gc.restore();
    }


    public Color getShapeColor() {
        return shapeColor;
    }

    /**
     * Move the shape by dx,dy
     *
     * @param dX the margin by which to change the coordinates of the shape
     * @param dY the margin by which to change the coordinates of the shape
     */
    public void updateStartPoints(double dX, double dY) {
        startX += dX;
        endX += dX;
        startY += dY;
        endY += dY;
        updateResizeCircle();
    }


    public boolean checkResizeHit(double hitX, double hitY) {
//        if (resizeCircle != null)
//            return resizeCircle.contains(hitX, hitY);

        return false;

    }

    /**
     * ABSTRACT METHODS for XShape
     **/

    protected abstract void drawShape(GraphicsContext gc, double startX, double startY, double endX, double endY, boolean isSelected);

    public abstract boolean contains(double hitX, double hitY);

    /**
     * STATIC METHODS for XShape
     **/

    public static double getDistanceBetweenTwoPoints(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }


    public static ShapeFX initShape(ShapeType xShapeType, double startX, double startY, Color color) {
        ShapeFX shapeSelected = null;


        switch (xShapeType) {
            case CIRCLE -> {
                shapeSelected = new Circle(startX, startY, color);
            }
            case RECT -> {
                shapeSelected = new Rectangle(startX, startY, color);
            }
            case OVAL -> {
                shapeSelected = new Oval(startX, startY, color);
            }
            case LINE -> {
                shapeSelected = new Line(startX, startY, color);
            }
            case SQUARE -> {
                shapeSelected = new Square(startX, startY, color);
            }

        }
        return shapeSelected;

    }

}