package app.model;

public class Pixel {
    private int x, y;

    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Pixel{" + "x=" + x + ", y=" + y + "}";
    }
}
