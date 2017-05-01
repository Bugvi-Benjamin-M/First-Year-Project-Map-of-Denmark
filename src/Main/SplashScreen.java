package Main;

import Helpers.ThemeHelper;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;

public class SplashScreen extends JWindow {
    BorderLayout borderLayout1 = new BorderLayout();
    JLabel imageLabel = new JLabel();
    JPanel southPanel = new JPanel();
    FlowLayout southPanelFlowLayout = new FlowLayout();
    JProgressBar progressBar = new JProgressBar();
    ImageIcon imageIcon;

    public SplashScreen(ImageIcon imageIcon)
    {
        this.imageIcon = imageIcon;
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // note - this class created with JBuilder
    void jbInit() throws Exception
    {
        progressBar.setUI(new BasicProgressBarUI());
        progressBar.setOpaque(true);
        imageLabel.setIcon(imageIcon);
        this.getContentPane().setLayout(borderLayout1);
        southPanel.setLayout(southPanelFlowLayout);
        southPanel.setBackground(ThemeHelper.color("toolbar"));
        this.getContentPane().add(imageLabel, BorderLayout.CENTER);
        this.getContentPane().add(southPanel, BorderLayout.SOUTH);
        southPanel.add(progressBar, null);
        this.pack();

        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);

    }

    public void setScreenVisible(boolean b)
    {
        final boolean boo = b;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { setVisible(boo); }
        });
    }
}
