package app.model;

import javafx.scene.image.Image;

public class StatusCanvas {
    private final boolean dimensionsChanged;
    private int width, height;
    private final Image image;

    public StatusCanvas(Image image) {
        this.image = image;
        dimensionsChanged = false;
    }

    public StatusCanvas(int width, int height, Image image) {
        this.width = width;
        this.height = height;
        this.image = image;
        dimensionsChanged = true;
    }

    public boolean DimensionsChanged() {
        return dimensionsChanged;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "CanvasHistory { " + "dimensionsChanged = " + dimensionsChanged + ", width = " + width + ", height = " + height + ", image = " + image + "}";
    }
}
