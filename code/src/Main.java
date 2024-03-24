import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class Main{
    public int width;
    public int height;
    Draw draw = new Draw();

    Main() {
        showInput();
    }

    private void showInput() {
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException
                         | IllegalAccessException
                         | UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        JPanel p = new JPanel(new BorderLayout(5, 5));
        JPanel labels = new JPanel(new GridLayout(0,1,2,2));
        JPanel labels1 = new JPanel(new FlowLayout());
        labels.add(new JLabel("Width", SwingConstants.RIGHT));
        labels.add(new JLabel("Height", SwingConstants.RIGHT));
        labels1.add(new JLabel("Minimum Width:900, Height: 800"));
        p.add(labels, BorderLayout.WEST);
        p.add(labels1, BorderLayout.SOUTH);

        JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField widthField = new JTextField();
        controls.add(widthField);
        JTextField heightField = new JTextField();
        heightField.addAncestorListener(new RequestFocusListener(false));
        controls.add(heightField);
        p.add(controls, BorderLayout.CENTER);
        JOptionPane.showMessageDialog(null, p, "Enter Canvas Width and Height",
                JOptionPane.QUESTION_MESSAGE);
        try {
            width = Integer.parseInt(widthField.getText());
            height = Integer.parseInt(heightField.getText());
            if(width<900 || height <800){
                JOptionPane.showMessageDialog(null, p,
                        "W:900,H:800 Minimum required", JOptionPane.ERROR_MESSAGE);
            }
            draw.setWH(width, height);
            draw.openPaint();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, p,
                    "Please enter valid number!", JOptionPane.ERROR_MESSAGE);
        }
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

