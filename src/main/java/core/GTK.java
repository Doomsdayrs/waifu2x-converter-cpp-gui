package core;

import org.gnome.gdk.EventKey;
import org.gnome.gtk.*;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

/**
 * This file is part of Waifu2xConverterCppGui.
 * Waifu2xConverterCppGui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Waifu2xConverterCppGui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Waifu2xConverterCppGui.  If not, see <https://www.gnu.org/licenses/>.
 * ====================================================================
 * Waifu2xConverterCppGui
 * 06 / May / 2019
 *
 * @author github.com/doomsdayrs
 */
class GTK {
    static String[] fileTypes = {"png", "dib", "exr", "hdr", "jp2", "jpe", "jpeg", "jpg", "pbm", "pgm", "pic", "bmp", "pnm", "ppm", "pxm", "ras", "sr", "tif", "tiff", "webp"};
    private static File inputFile;
    private static final String fileExtension = "png";
    static boolean outputToText = false;

    public static String StoredString = "";

    private static Entry InputFile;
    private static Entry OutputFile;
    private static Conversion conversion;

    public static void main(String[] args) throws FileNotFoundException, ParseException {
        Gtk.init(args);
        Builder builder = new Builder();
        builder.addFromFile("gui.glade");
        Window window = (Window) builder.getObject("Main");

        conversion = new Conversion();

        //Input setup
        InputFile = (Entry) builder.getObject("InputFile");
        InputFile.connect((Widget.KeyPressEvent) (widget, eventKey) -> {
            String key = eventKey.getKeyval().toString();
            key = key.substring(key.indexOf(".") + 1, key.length());
            System.out.println("Key pressed: " + key);
            if (key.equals("Return")) {
                String inputString = InputFile.getText().trim();
                if (!inputString.equals("")) {
                    if (inputString.contains("file://"))
                        inputString = inputString.replace("file://", "");
                    if (inputString.contains("%20"))
                        inputString = inputString.replaceAll("%20", " ");

                    int lastIndexOfSeparator = inputString.lastIndexOf("/") + 1;
                    String ParentDir = inputString.substring(0, lastIndexOfSeparator);
                    String ChildDir = inputString.substring(lastIndexOfSeparator);
                    inputFile = new File(ParentDir, ChildDir);
                    if (inputFile.getAbsoluteFile().exists()) {
                        if (inputFile.isFile()) {
                            updateInOut(inputFile);
                        } else if (inputFile.isDirectory()) {
                            updateInOut(inputFile);
                            conversion.recursive_directory = true;
                        } else {
                            updateInOut(inputFile);
                            conversion.recursive_directory = false;
                        }
                        InputFile.setText(inputString);
                        OutputFile.setText(inputString + conversion.getOutputHeaders());
                    } else {
                        JOptionPane.showMessageDialog(null, "That is not a valid file!");
                        return false;
                    }
                } else OutputFile.setText("");
            }
            return true;
        });

        //Output setup
        OutputFile = (Entry) builder.getObject("OutputFile");
        OutputFile.connect(new Widget.KeyPressEvent() {
            @Override
            public boolean onKeyPressEvent(Widget widget, EventKey eventKey) {
                String key = eventKey.getKeyval().toString();
                key = key.substring(key.indexOf(".") + 1, key.length());
                System.out.println("Key pressed: " + key);
                if (key.equals("Return")) {
                    String outputText = OutputFile.getText();
                    if (!outputText.equals("")) {
                        conversion.output = OutputFile.getText() + conversion.getOutputHeaders();
                    } else return false;
                }
                return true;
            }
        });
        OutputFile.connect((Entry.Changed) entry -> conversion.output = entry.getText() + conversion.getOutputHeaders());


        window.showAll();
        Gtk.main();
    }


    private static void updateInOut(File file) {
        String absolutePATH = file.getAbsolutePath();
        conversion.input = absolutePATH;
        absolutePATH = absolutePATH.substring(0, absolutePATH.lastIndexOf("."));
        OutputFile.setText(absolutePATH + conversion.getOutputHeaders() + "." + fileExtension);
    }


    /**
     * Returns the execution directory.
     * When the application is run from a .jar file this will remove .jar from its path
     *
     * @return the execution directory
     */
    public static String getExecDir() {
        String decodedPath = null;
        String runtimePath = GTK.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath();
        System.out.println("\nGetting Execution direction...");
        System.out.println("Execution directory raw: " + runtimePath);

        try {
            decodedPath = URLDecoder.decode(runtimePath, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assert decodedPath != null;
        if (decodedPath.toLowerCase().endsWith(".jar")) {
            System.out.println("Application was called from jar file");
            decodedPath = decodedPath.substring(0, decodedPath.lastIndexOf("/") + 1);
        }
        String output = "";

        if (System.getProperty("os.name").equals("Windows 10")) {
            output = decodedPath.substring(1);
        } else if (System.getProperty("os.name").equals("Linux")) {
            output = decodedPath;
        }
        System.out.println("Final Execution Directory: " + output);
        return output;
    }
}
