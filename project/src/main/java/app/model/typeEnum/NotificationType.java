package app.model.typeEnum;

public enum NotificationType {
    SHAPE_CHANGE("shape_change"),
    COLOR_CHANGE("color_change"),
    SHAPE_DELETED("shape_deleted");

    private String string;

    NotificationType(String name) {
        string = name;
    }

    @Override
    public String toString() {
        return string;
    }

}
