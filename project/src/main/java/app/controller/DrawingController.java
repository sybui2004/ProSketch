package app.controller;

import app.model.FileHandling;
import app.model.StatusCanvas;
import app.model.shapes.*;
import app.model.shapes.Rectangle;
import app.model.tools.Brush;
import app.model.tools.Eraser;
import app.model.tools.Filler;
import app.model.tools.Pencil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Stack;

public class DrawingController {

    @FXML
    private Slider scaleSlider;
    @FXML
    private Label pixel, fileName,canvasWidthHeight, zoomLabel;
    @FXML
    private AnchorPane BigAnchor, CanvasAnchor;
    @FXML
    private Canvas canvas;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Group group;
    @FXML
    private Button pencilBtn, brushBtn, eraserBtn,fillBtn, searchBtn, rectBtn, squareBtn, roundedRectBtn, ovalBtn, circleBtn, lineBtn, undoBtn, redoBtn;
    @FXML
    private ComboBox<String> sizeCombo;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private TextField heightTextField, widthTextField;
    private Button[] tools;
    private Cursor cursor;
    private String selectedTool = "";
    public GraphicsContext gc;
    public double preX, preY;
    public Stack<StatusCanvas> undoStack;
    public Stack<StatusCanvas> redoStack;
    private Brush brush;
    private Pencil pencil;
    private Eraser eraser;
    private Filler filler;
    private Rectangle rect;
    private Square square;
    private RoundedRect roundedRect;
    private Oval oval;
    private Circle circle;
    private Line line;
    public int sizeVal = 1;
    public Color color;
    private File file;

    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        this.tools = new Button[]{pencilBtn, brushBtn, eraserBtn, fillBtn, searchBtn, rectBtn, squareBtn, roundedRectBtn, ovalBtn, circleBtn, lineBtn};

        initializeStatus();
        initializeTools();
        initializeShapes();
        initializePinchZoom();
        initializeColors();
        initializeSizes();
        setUpDrawEvents();
        linkedSize();
        linkedZoom();
        linkedMouseXY();
        getCanvasHeightWidth();
        disableRedoUndo();
    }

    private void initializeStatus() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        undoStack.add(new StatusCanvas(canvas.snapshot(null, null)));
    }

    private void initializeTools() {
        brush = new Brush();
        pencil = new Pencil();
        eraser = new Eraser();
        filler = new Filler();
    }

    private void initializeShapes() {
        rect = new Rectangle();
        square = new Square();
        roundedRect = new RoundedRect();
        oval = new Oval();
        circle = new Circle();
        line = new Line();
    }

    private void setUpDrawEvents() {
        canvas.setOnMousePressed((e) -> {
            if (selectedTool.isEmpty()) return;
            if (e.isPrimaryButtonDown()) {
                gc.setFill(color);
                gc.setStroke(color);
            }
            switch (selectedTool) {
                case "pencil":
                    preX = e.getX();
                    preY = e.getY();
                    pencil.setEventMousePressed(preX, preY, gc);
                    break;
                case "brush":
                    preX = e.getX();
                    preY = e.getY();
                    brush.setEventMousePressed(sizeVal, gc, e);
                    break;
                case "eraser":
                    preX = e.getX();
                    preY = e.getY();
                    eraser.setEventMousePressed(sizeVal, gc, e);
                    break;
                case "fill":
                    filler.setEventMousePressed(e, color, canvas, gc);
                    break;
                case "search":
                    colorPicker.setValue(canvas.snapshot(null, null).getPixelReader().getColor((int) e.getX(), (int) e.getY()));
                    changeColor();
                    break;
                case "square", "roundrect", "oval", "line", "circle", "rect":
                    preX = e.getX();
                    preY = e.getY();
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
                    pencil.setEventMouseDragged(preX, preY, e.getX(), e.getY(), gc);
                    preX = e.getX();
                    preY = e.getY();
                    break;
                case "brush":
                    brush.setEventMouseDragged(preX, preY, sizeVal, gc, e);
                    preX = e.getX();
                    preY = e.getY();
                    break;
                case "eraser":
                    eraser.setEventMouseDragged(sizeVal, preX, preY, gc, e);
                    preX = e.getX();
                    preY = e.getY();
                    break;
                case "rect":
                    rect.setEventMouseDragged(gc, preX, preY, e, undoStack);
                    break;
                case "roundrect":
                    roundedRect.setEventMouseDragged(gc, preX, preY, e, undoStack);
                    break;
                case "oval":
                    oval.setEventMouseDragged(gc, preX, preY, e, undoStack);
                    break;
                case "circle":
                    circle.setEventMouseDragged(gc, preX, preY, e, undoStack, canvas);
                    break;
                case "line":
                    line.setEventMouseDragged(gc, preX, preY, e, undoStack);
                    break;
                case "square":
                    square.setEventMouseDragged(gc, preX, preY, e, undoStack);
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
                if (e.getDeltaY() < 0) scaleSlider.adjustValue(scaleSlider.getValue() - 25);

                if (e.getDeltaY() > 0) scaleSlider.adjustValue(scaleSlider.getValue() + 25);
            }
        });
        CanvasAnchor.setOnScroll((e) -> {
            if (e.isControlDown()) {
                e.consume(); // preent scrolling while zooming
                if (e.getDeltaY() < 0) scaleSlider.adjustValue(scaleSlider.getValue() - 25);
                if (e.getDeltaY() > 0) scaleSlider.adjustValue(scaleSlider.getValue() + 25);
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
        scaleSlider.valueProperty().addListener((e) -> {
                zoomLabel.setText((int) (scaleSlider.getValue()) + "%");
                double zoom = scaleSlider.getValue() / 100.0;
                group.setScaleX(zoom);
                group.setScaleY(zoom);
        });

        canvas.setOnMouseEntered((e) -> BigAnchor.setCursor(cursor));
        canvas.setOnMouseExited((e) -> BigAnchor.setCursor(Cursor.DEFAULT));
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

    private void disableRedoUndo() {
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
        sizeVal = Integer.parseInt(sizeCombo.getValue().substring(0, sizeCombo.getValue().indexOf(" ")));
        gc.setLineWidth(sizeVal);
    }

    @FXML
    public void changeColor() {
        color = colorPicker.getValue();
    }

    public static boolean compareImages(Image img1, Image img2) {
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
            double zoom = scaleSlider.getValue() / 100.0;
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
