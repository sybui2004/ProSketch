package app.model.typeEnum;

public enum FileHandling {
    New("New"), Open("Open"), Save("Save"), SaveAs("Save As");
    private String string;
    FileHandling(String name) {
        string = name;
    }
    @Override
    public String toString() {
        return string;
    }
}
