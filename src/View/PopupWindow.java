package View;

import Helpers.ThemeHelper;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;

/**
 * Class details:
 * The PopupWindow class contains a selection of different popup windows that
 * can be shown during the execution of the program.
 *
 */
public class PopupWindow {

    /**
   * Opens a FileChooser purposed to opening a file with some specific
   * extension.
   * @param allFilesFilter Whether the FileChooser should display files that
   * does not
   *                       have any of the required extensions.
   * @param filters A collection of filters that the file to be opened has a
   * extension of.
   * @return The FileChooser either containing the user-specified file or being
   * null if cancelled.
   */
    public static JFileChooser fileLoader(boolean allFilesFilter,
        FileNameExtensionFilter[] filters)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(allFilesFilter);
        if (filters != null)
            for (FileNameExtensionFilter filter : filters) {
                fileChooser.addChoosableFileFilter(filter);
            }
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return fileChooser;
        } else {
            return null;
        }
    }

    /**
     * Opens a FileChooser purposed to saving to a file with some specific
     * extension.
     * @param allFilesFilter Whether the FileChooser should display files that
     *                       does not have any of the required extensions.
     * @param filters A collection of filters that the file to be saved to should have
     *                an extension of.
     * @return The FileChooser either containing the user-specified file or being
     * null if cancelled.
     */
    public static JFileChooser fileSaver(boolean allFilesFilter,
        FileNameExtensionFilter[] filters)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(allFilesFilter);
        if (filters != null)
            for (FileNameExtensionFilter filter : filters) {
                fileChooser.addChoosableFileFilter(filter);
            }
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            return fileChooser;
        } else {
            return null;
        }
    }

    /**
     * Opens a information popup message
     * @param relativeTo The position on the screen, e.g. relative to some frame or
     *                   null if in the middle of the screen
     * @param message The message of the popup
     * @param title The title of the information popup
     */
    public static void infoBox(JFrame relativeTo, String message, String title)
    {
        JOptionPane.showMessageDialog(relativeTo, message, title,
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Opens a error message popup
     * @param relativeTo The position on the screen, e.g. relative to some frame or
     *                   null if in the middle of the screen
     * @param message The message of the popup
     */
    public static void errorBox(JFrame relativeTo, String message)
    {
        JOptionPane.showMessageDialog(relativeTo, message, "Error occured!",
            JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Opens a warning popup box
     * @param relativeTo The position on the screen, e.g. relative to some frame or
     *                   null if in the middle of the screen
     * @param message The message of the popup
     */
    public static void warningBox(JFrame relativeTo, String message)
    {
        JOptionPane.showMessageDialog(relativeTo, message, "Warning",
            JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Opens up a confirmation popup message
     * @param relativeTo The position on the screen, e.g. relative to some frame or
     *                   null if in the middle of the screen
     * @param message The message of the popup
     * @param title The title of the confirmation box
     * @param options The number of options available
     * @return the selected option
     */
    public static int confirmBox(JFrame relativeTo, String message, String title,
        int options)
    {
        return JOptionPane.showConfirmDialog(relativeTo, message, title, options);
    }

    /**
     *
     * Opens up a confirmation popup message
     * @param relativeTo The position on the screen, e.g. relative to some frame or
     *                   null if in the middle of the screen
     * @param message The message of the popup
     * @param title The title of the confirmation box
     * @param options A collection of options to choose from
     * @param initialValue The initially marked option
     * @return the chosen option
     */
    public static int confirmBox(JFrame relativeTo, String message, String title,
        Object[] options, Object initialValue) {
        return JOptionPane.showOptionDialog(
                relativeTo, message, title, JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, initialValue);
    }

    /**
     * Opens up a confirmation popup message
     * @param relativeTo The position on the screen, e.g. relative to some frame or
     *                   null if in the middle of the screen
     * @param message The message of the popup
     * @param title The title of the confirmation box
     * @param options A collection of options to choose from
     * @return the selected option
     */
    public static String confirmBox(JFrame relativeTo, String message, String title, String[] options) {
        return (String) JOptionPane.showInputDialog(relativeTo, message,title, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    /**
     * Opens a popup box where the user can input some text
     * Opens up a confirmation popup message
     * @param relativeTo The position on the screen, e.g. relative to some frame or
     *                   null if in the middle of the screen
     * @param message The message of the popup
     * @param title The title of the input box
     * @return the user entered text
     */
    public static String textInputBox(JFrame relativeTo, String title, String message){
        return JOptionPane.showInputDialog(relativeTo, message, title, JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Opens a loading screen
     * @param description optional description on the loading screen
     * @return the JWindow opened
     */
    public static JWindow LoadingScreen(String description) {
        JWindow loadWindow = new JWindow();
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(true);
        progressBar.setBackground(ThemeHelper.color("toolbar"));
        progressBar.setOpaque(true);
        progressBar.setUI(new BasicProgressBarUI());
        JLabel icon = new JLabel();
        icon.setIcon(new ImageIcon(PopupWindow.class.getResource("/Copenhagen.jpg")));
        icon.setVisible(true);
        icon.setPreferredSize(new Dimension(200,200));
        icon.setBackground(ThemeHelper.color("toolbar"));
        icon.setOpaque(true);
        JLabel text = new JLabel(description);
        text.setFont(new Font(text.getFont().getName(), text.getFont().getStyle(), 12));
        text.setVisible(true);
        text.setBackground(ThemeHelper.color("toolbar"));
        text.setForeground(ThemeHelper.color("icon"));
        text.setOpaque(true);
        loadWindow = new JWindow();
        loadWindow.setLayout(new BorderLayout());
        //loadWindow.setLocation(new Point(x,y));
        loadWindow.add(BorderLayout.NORTH, icon);
        loadWindow.add(BorderLayout.CENTER, text);
        loadWindow.add(BorderLayout.SOUTH, progressBar);
        loadWindow.pack();
        loadWindow.setVisible(true);
        loadWindow.setAlwaysOnTop(true);
        return loadWindow;
    }
}
