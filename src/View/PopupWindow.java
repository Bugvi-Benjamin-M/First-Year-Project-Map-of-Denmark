package View;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Class details:
 * The PopupWindow class contains a selection of different popup windows that can be
 * shown during the execution of the program.
 *
 * @author Búgvi Magnussen, buma@itu.dk
 * @version 09/03/2017
 */
public class PopupWindow {

    /**
     * Opens a FileChooser purposed to opening a file with some specific extension.
     * @param allFilesFilter Whether the FileChooser should display files that does not
     *                       have any of the required extensions.
     * @param filters A collection of filters that the file to be opened has a extension of.
     * @return The FileChooser either containing the user-specified file or being null if cancelled.
     */
    public static JFileChooser fileLoader(boolean allFilesFilter, FileNameExtensionFilter[] filters) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(allFilesFilter);
        for (FileNameExtensionFilter filter : filters) {
            fileChooser.addChoosableFileFilter(filter);
        }
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return fileChooser;
        } else {
            return null;
        }
    }


}
