package app.controller;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Stack;

import app.model.StatusCanvas;
import app.model.FileHandling;
import app.model.Pixel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class DrawingController {

    @FXML
    Slider ScaleSlider;
    @FXML
    Label pixel;
    @FXML
    Label fileName;
    @FXML
    Label canvasWidthHeight;
    @FXML
    Label zoomLabel;
    @FXML
    AnchorPane BigAnchor, CanvasAnchor;
    @FXML
    Canvas canvas;
    @FXML
    ScrollPane scrollPane;
    @FXML
    Group group;
    @FXML
    Button pencilBtn;
    @FXML
    Button brushBtn;
    @FXML
    Button eraserBtn;
    @FXML
    Button fillBtn;
    @FXML
    Button searchBtn;
    @FXML
    Button rectBtn;
    @FXML
    Button squareBtn;
    @FXML
    Button roundedRectBtn;
    @FXML
    Button ovalBtn;
    @FXML
    Button circleBtn;
    @FXML
    Button lineBtn;
    @FXML
    Button undoBtn;
    @FXML
    Button redoBtn;
    @FXML
    ComboBox<String> sizeCombo;
    @FXML
    ColorPicker colorPicker;
    @FXML
    TextField heightTextField, widthTextField;
    boolean zoomLocked = true, statusFile = false;
    Button[] tools;
    Cursor cursor;
    String selectedTool = "";
    GraphicsContext gc;
    double prevX, prevY;
    Stack<StatusCanvas> undoStack;
    Stack<StatusCanvas> redoStack;
    int SizeVal = 1;
    Color color;
    File file;

    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        this.tools = new Button[]{pencilBtn, brushBtn, eraserBtn, fillBtn, searchBtn, rectBtn, squareBtn, roundedRectBtn, ovalBtn, circleBtn, lineBtn};
        linkedSize();
        linkedZoom();
        linkedMouseXY();
        getCanvasHeightWidth();
        initializePinchZoom();
        initializeColors();
        initializeSizes();
        SetUpDrawEvents();
        initializeStatus();
        disableRedoUndo();
    }

    private void initializeStatus() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        undoStack.add(new StatusCanvas(canvas.snapshot(null, null)));
    }


    private void drawCircle(double startX, double startY, double endX, double endY) {
        double radius = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(undoStack.peek().getImage(), 0, 0);
        gc.strokeOval(startX - radius, startY - radius, radius * 2, radius * 2);
    }

    private void SetUpDrawEvents() {
        canvas.setOnMousePressed((e) -> {
            if (selectedTool.isEmpty()) return;
            if (e.isPrimaryButtonDown()) {
                gc.setFill(color);
                gc.setStroke(color);
            }
            switch (selectedTool) {
                case "pencil":
                    prevX = e.getX();
                    prevY = e.getY();
                    gc.strokeLine(prevX, prevY, prevX, prevY);
                    break;
                case "brush":
                    prevX = e.getX();
                    prevY = e.getY();
                    gc.fillArc(e.getX() - SizeVal, e.getY() - SizeVal, SizeVal, SizeVal, 0, 360, ArcType.ROUND);
                    break;
                case "eraser":
                    prevX = e.getX();
                    prevY = e.getY();
                    gc.setFill(Color.WHITE);
                    gc.setStroke(Color.WHITE);
                    gc.fillRect(e.getX(), e.getY(), SizeVal, SizeVal);
                    break;
                case "fill":
                    if (e.isPrimaryButtonDown()) bucketFill((int) e.getX(), (int) e.getY(), color);
                    break;
                case "search":
                    colorPicker.setValue(canvas.snapshot(null, null).getPixelReader().getColor((int) e.getX(), (int) e.getY()));
                    changeColor();
                    break;
                case "square", "roundrect", "oval", "line", "circle", "rect":
                    prevX = e.getX();
                    prevY = e.getY();
                    break;
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (e) -> {
            if (selectedTool.isEmpty()) return;
            if (e.isPrimaryButtonDown()) {
                gc.setFill(color);
                gc.setStroke(color);
            }

            switch (selectedTool) {
                case "pencil":
                    gc.strokeLine(prevX, prevY, e.getX(), e.getY());
                    prevX = e.getX();
                    prevY = e.getY();
                    break;
                case "brush":
                    if (Math.abs(e.getX() - prevX) > Math.max(SizeVal / 2, 1) || Math.abs(e.getY() - prevY) > Math.max(SizeVal / 2, 1)) {
                        drawExtraPoints(prevX, prevY, e.getX(), e.getY());
                    }
                    gc.fillArc(e.getX() - SizeVal, e.getY() - SizeVal, SizeVal, SizeVal, 0, 360, ArcType.ROUND);
                    prevX = e.getX();
                    prevY = e.getY();
                    break;
                case "eraser":
                    gc.setFill(Color.WHITE);
                    gc.setStroke(Color.WHITE);
                    gc.fillRect(e.getX(), e.getY(), SizeVal, SizeVal);
                    if (Math.abs(e.getX() - prevX) > Math.max(SizeVal / 2, 1) || Math.abs(e.getY() - prevY) > Math.max(SizeVal / 2, 1)) {
                        deleteExtraPoints(prevX, prevY, e.getX(), e.getY());
                    }
                    prevX = e.getX();
                    prevY = e.getY();
                    break;
                case "rect":
                    gc.drawImage(undoStack.peek().getImage(), 0, 0);
                    gc.strokeRect(prevX, prevY, Math.abs(e.getX() - prevX), Math.abs(e.getY() - prevY));
                    break;
                case "roundrect":
                    gc.drawImage(undoStack.peek().getImage(), 0, 0);
                    gc.strokeRoundRect(prevX, prevY, Math.abs(e.getX() - prevX), Math.abs(e.getY() - prevY), Math.min(Math.abs(e.getX() - prevX) / 5, 50), Math.min(Math.abs(e.getX() - prevX) / 5, 50));
                    break;
                case "oval":
                    gc.drawImage(undoStack.peek().getImage(), 0, 0);
                    gc.strokeOval(prevX, prevY, Math.abs(e.getX() - prevX), Math.abs(e.getY() - prevY));
                    break;
                case "circle":
                    gc.drawImage(undoStack.peek().getImage(), 0, 0);
                    drawCircle(prevX, prevY, e.getX(), e.getY());
                    break;
                case "line":
                    gc.drawImage(undoStack.peek().getImage(), 0, 0);
                    gc.strokeLine(prevX, prevY, e.getX(), e.getY());
                    break;
                case "square":
                    gc.drawImage(undoStack.peek().getImage(), 0, 0);
                    gc.strokeRect(prevX, prevY, Math.abs(e.getX() - prevX), Math.abs(e.getX() - prevX));

                default:
                    break;
            }
        });
        canvas.setOnMouseReleased((e) -> {
            if (!compareImages(undoStack.peek().getImage(), (canvas.snapshot(null, null)))) {
                undoStack.add(new StatusCanvas(canvas.snapshot(null, null)));
                redoStack = new Stack<>();
                disableRedoUndo();
            }

        });
    }

    private void bucketFill(int x, int y, Color fillColor) {
        WritableImage snapshot = canvas.snapshot(null, null);
        PixelReader pixelReader = snapshot.getPixelReader();
        LinkedList<Pixel> queue = new LinkedList<>();
        queue.addLast(new Pixel(x, y));
        while (!queue.isEmpty()) {
            Pixel currentPixel = queue.pop();
            Color pixelColor = pixelReader.getColor(currentPixel.getX(), currentPixel.getY());
            if (!pixelColor.equals(fillColor)) {
                snapshot.getPixelWriter().setColor(currentPixel.getX(), currentPixel.getY(), fillColor);
                ArrayList<Pixel> neighbors = getPixelNeighbors(currentPixel.getX(), currentPixel.getY(), snapshot);
                for (var neighbor : neighbors) {
                    if (pixelReader.getColor(neighbor.getX(), neighbor.getY()).equals(pixelColor))
                        queue.addLast(neighbor);
                }
            }
        }
        gc.drawImage(snapshot, 0, 0);
    }

    private ArrayList<Pixel> getPixelNeighbors(int x, int y, WritableImage image) {
        ArrayList<Pixel> neighbors = new ArrayList<>();
        if (x > 0) neighbors.add(new Pixel(x - 1, y));
        if (x < image.getWidth() - 1) neighbors.add(new Pixel(x + 1, y));
        if (y > 0) neighbors.add(new Pixel(x, y - 1));
        if (y < image.getHeight() - 1) neighbors.add(new Pixel(x, y + 1));

        return neighbors;
    }

    private void drawExtraPoints(double prevX, double prevY, double x, double y) {
        if (Math.abs(x - prevX) > Math.max(SizeVal / 2, 1) || Math.abs(y - prevY) > Math.max(SizeVal / 2, 1)) {
            double newX = (int) ((x + prevX) / 2);
            double newY = (int) ((y + prevY) / 2);

            gc.fillArc(newX - SizeVal, newY - SizeVal, SizeVal, SizeVal, 0, 360, ArcType.ROUND);

            drawExtraPoints(prevX, prevY, newX, newY);
            drawExtraPoints(newX, newY, x, y);
        }
    }

    private void deleteExtraPoints(double prevX, double prevY, double x, double y) {
        if (Math.abs(x - prevX) > Math.max(SizeVal / 2, 1) || Math.abs(y - prevY) > Math.max(SizeVal / 2, 1)) {

            double newX = (int) ((x + prevX) / 2);
            double newY = (int) ((y + prevY) / 2);

            gc.setFill(Color.WHITE);
            gc.setStroke(Color.WHITE);
            gc.fillRect(newX, newY, SizeVal, SizeVal);

            deleteExtraPoints(prevX, prevY, newX, newY);
            deleteExtraPoints(newX, newY, x, y);
        }
    }

    private void initializeSizes() {
        ObservableList<String> options = FXCollections.observableArrayList("1 px", "3 px", "5 px", "8 px", "15 px");
        sizeCombo.setItems(options);
        sizeCombo.getSelectionModel().select(2);
        changeSize();

    }

    private void initializeColors() {
        colorPicker.setValue(Color.BLACK);
        color = colorPicker.getValue();
    }

    private void initializePinchZoom() {
        BigAnchor.setOnScroll((e) -> {
            if (e.isControlDown()) {
                if (e.getDeltaY() < 0) ScaleSlider.adjustValue(ScaleSlider.getValue() - 25);

                if (e.getDeltaY() > 0) ScaleSlider.adjustValue(ScaleSlider.getValue() + 25);
            }
        });
        CanvasAnchor.setOnScroll((e) -> {
            if (e.isControlDown()) {
                e.consume(); // prevent scrolling while zooming
                if (e.getDeltaY() < 0) ScaleSlider.adjustValue(ScaleSlider.getValue() - 25);
                if (e.getDeltaY() > 0) ScaleSlider.adjustValue(ScaleSlider.getValue() + 25);
            }
        });
    }

    private void getCanvasHeightWidth() {
        canvasWidthHeight.setText((int) canvas.getWidth() + " x " + (int) canvas.getHeight() + "px");
    }

    private void linkedMouseXY() {
        canvas.setOnMouseMoved((e) -> {
            pixel.setText((int) e.getX() + ", " + (int) e.getY());
        });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (e) -> {
            pixel.setText((int) e.getX() + ", " + (int) e.getY());
        });
    }

    private void linkedSize() {
        scrollPane.maxHeightProperty().bind(BigAnchor.heightProperty());
        scrollPane.maxWidthProperty().bind(BigAnchor.widthProperty());
        widthTextField.setText((int) canvas.getWidth() + "");
        heightTextField.setText((int) canvas.getHeight() + "");
    }

    private void linkedZoom() {
        ScaleSlider.valueProperty().addListener((e) -> {
            if (!zoomLocked || ScaleSlider.getValue() % 25 == 0) {
                zoomLabel.setText((int) (ScaleSlider.getValue()) + "%");
                double zoom = ScaleSlider.getValue() / 100.0;
                group.setScaleX(zoom);
                group.setScaleY(zoom);
            }
        });

        canvas.setOnMouseEntered((e) -> BigAnchor.setCursor(cursor));
        canvas.setOnMouseExited((e) -> BigAnchor.setCursor(Cursor.DEFAULT));
    }

    @FXML
    public void openFile() throws Exception {
        FileHandling loader = new FileHandling((Stage) BigAnchor.getScene().getWindow());
        Object[] result = loader.openFile();
        if (result == null) return;
        Image image = (Image) result[0];
        File f = (File) result[1];
        int w = (int) image.getWidth();
        int h = (int) image.getHeight();
        canvas.setHeight(h);
        canvas.setWidth(w);
        CanvasAnchor.setPrefHeight(h);
        CanvasAnchor.setPrefWidth(w);
        getCanvasHeightWidth();

        undoStack = new Stack<>();
        redoStack = new Stack<>();
        disableRedoUndo();
        initializeColors();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(image, 0, 0);
        file = f;
        fileName.setText(file.getName());
    }

    @FXML
    public void saveFile() throws IOException {
        FileHandling saver = new FileHandling((Stage) BigAnchor.getScene().getWindow());
        Image image = canvas.snapshot(null, null);
        if (file == null) {
            File f = saver.saveAsFile(image);
            if (f != null) {
                file = f;
                fileName.setText(file.getName());
            }
        } else {
            saver.saveFile(image, file.getName().substring(file.getName().lastIndexOf(".") + 1).toUpperCase(), file);
        }
    }

    @FXML
    public void saveAsFile() throws IOException {
        FileHandling saver = new FileHandling((Stage) BigAnchor.getScene().getWindow());
        Image image = canvas.snapshot(null, null);
        File f = saver.saveAsFile(image);
        if (f != null) {
            fileName.setText(f.getName());
            file = f;
        }

    }

    @FXML
    public void closeApp() {
        System.exit(0);
    }

    @FXML
    public void undo() {
        if (undoStack.size() > 1) {
            redoStack.add(undoStack.pop());
            if (undoStack.peek().DimensionsChanged()) {
                changeDimensions(undoStack.peek().getWidth(), undoStack.peek().getHeight());
            }
            gc.drawImage(undoStack.peek().getImage(), 0, 0);
        }
        disableRedoUndo();
    }

    @FXML
    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.add(redoStack.peek());
            StatusCanvas statusCanvas = redoStack.pop();
            if (statusCanvas.DimensionsChanged()) {
                changeDimensions(statusCanvas.getWidth(), statusCanvas.getHeight());
            }
            gc.drawImage(statusCanvas.getImage(), 0, 0);
        }
        disableRedoUndo();
    }

    public void disableRedoUndo() {
        redoBtn.setDisable(redoStack.isEmpty());
        undoBtn.setDisable(undoStack.size() < 2);
    }

    @FXML
    public void openAbout() {
        try {
            Desktop.getDesktop().browse(new URL("https://github.com/sybui2004/ProSketch").toURI());
        } catch (Exception ignored) {
        }
    }

    @FXML
    public void newFile() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        fileName.setText("Not Saved yet");
        file = null;
        initializeStatus();
        disableRedoUndo();
    }

    @FXML
    public void selectTool(MouseEvent event) {
        Button b = (Button) event.getSource();
        b.getStyleClass().add("selected-tool");
        String id = b.getId();
        selectedTool = id;
        for (Button btn : tools) {
            if (btn != b) {
                btn.getStyleClass().remove("selected-tool");
            }
        }
        cursor = new ImageCursor();
        switch (id) {
            case "pencil":
                cursor = new ImageCursor(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/app/view/images/pencilcursor.png"))), 0, 32);
                break;
            case "brush":
                cursor = new ImageCursor(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/app/view/images/brush.png"))), 6, 27);
                break;
            case "eraser":
                cursor = new ImageCursor(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/app/view/images/eraser.png"))), 10, 30);
                break;
            case "fill":
                cursor = new ImageCursor(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/app/view/images/fill.png"))), 3, 28);
                break;
            case "search":
                cursor = new ImageCursor(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/app/view/images/search.png"))), 4, 29);
                break;
            default:
                cursor = Cursor.CROSSHAIR;
                break;
        }

        BigAnchor.setCursor(cursor);
        CanvasAnchor.setCursor(cursor);
    }

    @FXML
    public void changeSize() {
        SizeVal = Integer.parseInt(sizeCombo.getValue().substring(0, sizeCombo.getValue().indexOf(" ")));
        gc.setLineWidth(SizeVal);
    }

    @FXML
    public void changeColor() {
        color = colorPicker.getValue();
    }

    static public boolean compareImages(Image img1, Image img2) {
        for (int i = 0; i < img1.getWidth(); i++) {
            for (int j = 0; j < img1.getHeight(); j++) {
                if (!img1.getPixelReader().getColor(i, j).equals(img2.getPixelReader().getColor(i, j))) return false;
            }
        }
        return true;
    }

    @FXML
    public void discardDimensions() {
        widthTextField.setText((int) canvas.getWidth() + "");
        heightTextField.setText((int) canvas.getHeight() + "");
    }

    @FXML
    public void applyDimensions() {
        int w = Integer.parseInt(widthTextField.getText());
        int h = Integer.parseInt(heightTextField.getText());
        if (w < 1 || h < 1) {
            widthTextField.setText((int) canvas.getWidth() + "");
            heightTextField.setText((int) canvas.getHeight() + "");
            return;
        }

        undoStack.add(new StatusCanvas((int) canvas.getWidth(), (int) canvas.getHeight(), undoStack.pop().getImage()));

        changeDimensions(w, h);

        undoStack.add(new StatusCanvas((int) canvas.getWidth(), (int) canvas.getHeight(), canvas.snapshot(null, null)));
        disableRedoUndo();
    }

    private void changeDimensions(int w, int h) {
        try {
            canvas.setHeight(h);
            canvas.setWidth(w);
            CanvasAnchor.setPrefHeight(h);
            CanvasAnchor.setPrefWidth(w);
            getCanvasHeightWidth();
            double zoom = ScaleSlider.getValue() / 100.0;
            group.setScaleX(zoom + 0.01);
            group.setScaleY(zoom + 0.01);
            Thread.sleep(1);
            group.setScaleX(zoom);
            group.setScaleY(zoom);
        } catch (InterruptedException e) {
            widthTextField.setText((int) canvas.getWidth() + "");
            heightTextField.setText((int) canvas.getHeight() + "");
        }
    }

    @FXML
    public void exitedCanvasTab() {
        discardDimensions();
    }

    @FXML
    public void checkEscape(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            gc.drawImage(undoStack.peek().getImage(), 0, 0);
        }
    }
}
