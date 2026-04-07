package org.model;

import java.awt.Image;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;

/** MODEL: Strumenti di supporto utili per l'I/O dei file e il caricamento delle immagini. */
public class Utils {

    public static Image makeImage(File file) {
        try {
            return ImageIO.read(file);
        }
        catch (IOException e) {
            System.out.println("Could not locate image file: " + file);
            return null;
        }
    }

    public static Scanner makeReader(File file) {
        try {
            Scanner s = new Scanner(file);
            s.hasNextLine();
            return s;
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file: " + file.getAbsolutePath());
            return null;
        }
    }

    public static PrintWriter makeWriter(File file, boolean append) {
        try {
            return new PrintWriter(new FileWriter(file, append), true); }
        catch (IOException e) {
            System.err.println("IOException making PrintWriter for: " + file.getAbsolutePath());
            return null;
        }
    }

    public static String[] split(String string, String regex) {
        ArrayList<String> words = new ArrayList<>();
        int first = 0, last = string.indexOf(regex);
        if (last != -1) {
            words.add(string.substring(first, last));
            while (last != -1 && string.substring(last).contains(regex)) {
                first = last + 1;
                last = string.indexOf(regex, first);
                if (last != -1) {
                    String w = string.substring(first, last).trim();
                    if (!w.equals(regex) && !w.isEmpty()) words.add(w);
                }
            }
            words.add(string.substring(first).trim());
        }
        return words.toArray(new String[0]);
    }
}
