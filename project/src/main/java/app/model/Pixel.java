package app.model;

import javafx.scene.image.WritableImage;

import java.util.ArrayList;

public class Pixel {
    private int x, y;

    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pixel(){

    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public ArrayList<Pixel> getPixelNeighbors(int x, int y, WritableImage image) {
        ArrayList<Pixel> neighbors = new ArrayList<>();
        if (x > 0) neighbors.add(new Pixel(x - 1, y));
        if (x < image.getWidth() - 1) neighbors.add(new Pixel(x + 1, y));
        if (y > 0) neighbors.add(new Pixel(x, y - 1));
        if (y < image.getHeight() - 1) neighbors.add(new Pixel(x, y + 1));

        return neighbors;
    }

    @Override
    public String toString() {
        return "Pixel{" + "x=" + x + ", y=" + y + "}";
    }
}
