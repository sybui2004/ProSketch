package app.model.shapes;

import app.model.typeEnum.ShapeType;
import app.model.ShapeFX;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Oval extends ShapeFX {

    public Oval(double startX, double startY, Color color) {
        super(startX, startY, color, ShapeType.OVAL);
    }

    @Override
    public void drawShape(GraphicsContext gc, double drawX, double drawY, double drawWidth, double drawHeight, boolean isSelected) {

        gc.fillOval(drawX, drawY, drawWidth, drawHeight);
        gc.strokeOval(drawX, drawY, drawWidth, drawHeight);

    }


    @Override
    public boolean contains(double hitX, double hitY) {
        //y = m(x-x1) + y1;
        //y = mx-mx1 + y1
        //y = mx + c where c = -mx1 + y1 = -mh + k

        double h = getStartingPointX() + getWidth() / 2; //centerX of ellipse
        double k = getStartingPointY() + getHeight() / 2; //centerY of ellipse
        double a = getWidth() / 2;
        double b = getHeight() / 2;
        double m = (k - hitY) / (h - hitX); //slope of intersecting line containing points (h,k) and (hitX,hitY)
        double PHI = -m * h;
        //x1 is h and y1 is k
        //double C= -1*(Math.pow(a*b,2)-Math.pow(h*b,2)-Math.pow(m*h*a,2));
        //double B= 2*(-h*m*m*a*a-h*b*b);
        //double A= m*m*a*a+b*b;
        //double intersectionX=(-B+Math.sqrt(B*B-4*A*C))/2*A;
        //double intersectionY=m*(intersectionX-h)+k;

        double intersectionX = (b * b * h - a * a * m * PHI + a * b * Math.sqrt(b * b + a * a * m * m - 2 * m * PHI * h - PHI * PHI - m * m * h * h)) / (b * b + a * a * m * m);
        double intersectionY = m * (intersectionX - h) + k;
        double distanceFromCenterToEllipseEdge = ShapeFX.getDistanceBetweenTwoPoints(h, k, intersectionX, intersectionY);
        double distanceFromCenterToHitPoints = ShapeFX.getDistanceBetweenTwoPoints(h, k, hitX, hitY);

        return distanceFromCenterToHitPoints <= (distanceFromCenterToEllipseEdge + 0.001f);
    }
}
