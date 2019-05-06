package core;

import org.gnome.gtk.*;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public class Java {
    static String[] fileTypes = {"bmp","dib","exr","hdr","jp2","jpe","jpeg","jpg","pbm","pgm","pic","png","pnm","ppm","pxm","ras","sr","tif","tiff","webp"};
    static File inputFile;
    static String fileExtension = "png";
    static boolean outputToText = false;

    public static String StoredString = "";

    static Entry InputFile;
    static Entry OutputFile;
    static Conversion conversion;

    public static void main(String[] args) throws FileNotFoundException, ParseException {
        Gtk.init(args);
        Builder b = new Builder();
        b.addFromFile("gui.glade");
        Window window = (Window) b.getObject("Main");
        conversion = new Conversion();
        build(b);
        initialize();
        window.showAll();
        Gtk.main();
    }



    public static void build(Builder builder){
        InputFile = (Entry) builder.getObject("InputFile");
        OutputFile = (Entry) builder.getObject("OutputFile");
    }

    public static void initialize(){
        InputFile.connect((Entry.Changed) entry -> {
            String inputString = entry.getText().trim();
            if (!inputString.equals("")){
            if (inputString.contains("file://"))
                inputString = inputString.replace("file://","");
            if (inputString.contains("%20"))
                inputString = inputString.replaceAll("%20"," ");

            int lastIndexOfSeperator = inputString.lastIndexOf("/") + 1;

            entry.setText(inputString);
            String ParentDir = inputString.substring(0, lastIndexOfSeperator);
            String ChildDir = inputString.substring(lastIndexOfSeperator);
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
            } else JOptionPane.showMessageDialog(null, "That is not a valid file!");}
        });
        OutputFile.connect((Entry.Changed) entry -> {
            conversion.output = entry.getText() + conversion.getOutputHeaders();
        });
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
        String runtimePath = Java.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath();
        System.out.println("\nGetting Execution direction...");
        System.out.println("Execution directory raw: " + runtimePath);

        try {
            decodedPath = URLDecoder.decode(runtimePath, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
