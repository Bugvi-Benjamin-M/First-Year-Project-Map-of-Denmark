package View;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 09/03/2017
 */
public class PopupWindow {

    public static JFileChooser fileChooser(boolean allFilesFilter, FileNameExtensionFilter[] filters) {
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
