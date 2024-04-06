package app.model.typeEnum;

public enum ShapeType {
    CIRCLE("CIRCLE"), SQUARE("SQUARE"), RECT("RECT"), OVAL("OVAL"), LINE("LINE");

    private String string;

    ShapeType(String name) {
        string = name;
    }

    @Override
    public String toString() {
        return string;
    }
}
