package Helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 27-03-2017.
 * @project BFST
 */
public class FileSorter {

    public static void main(String[] args) {
        if (args.length != 1) throw new RuntimeException("Requires file name as parameter.");
        try {
            ArrayList<Integer> input = new ArrayList<>();
            Path file = Paths.get("./resources/" + args[0]);
            try (InputStream in = Files.newInputStream(file);
                 BufferedReader reader =
                         new BufferedReader(new InputStreamReader(in))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    input.add(Integer.parseInt(line));
                }
                Collections.sort(input);
                for (Integer i : input) {
                    System.out.println(i.toString());
                }
            } catch (IOException x) {
                System.err.println(x);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }


}
