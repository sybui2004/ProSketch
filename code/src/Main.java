import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

public class Main{
    public int width;
    public int height;
    Draw draw = new Draw();

    Main() {

        showInput();
    }

    private void showInput() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
        JPanel labels1 = new JPanel(new FlowLayout());
        labels.add(new JLabel("Width", SwingConstants.RIGHT));
        labels.add(new JLabel("Height", SwingConstants.RIGHT));
        labels1.add(new JLabel("Minimum Width:900, Height: 800"));
        p.add(labels, BorderLayout.WEST);
        p.add(labels1, BorderLayout.SOUTH);
        draw.setWH(1500, 900);
        draw.openPaint();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }

}

