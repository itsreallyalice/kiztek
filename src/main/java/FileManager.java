import javax.swing.*;
import java.io.*;

import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class FileManager {

    private static final String LAST_OPENED_FILE_KEY = "lastOpenedFile";
    private static final String MAIN_TEX_FILE_KEY = "mainTexFile";
    private static final String COMPILER_KEY = "currentCompiler";

    public static Preferences preferences;


    public static File createMainTeX(String name, String path){
        String content = "\\documentclass{article}\n\n"
                + "\\begin{document}\n\n"
                + "(Type your content here.)\n\n"
                + "\\end{document}";

        File newFile;
        String baseFilename = JOptionPane.showInputDialog(null,"New File",name);


        try {

            newFile = FileManager.createNewFile(baseFilename,path);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        try (FileWriter fileWriter = new FileWriter(newFile)) {
            fileWriter.write(content);
            System.out.println("File created and content written successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while creating/writing to the file.");
            e.printStackTrace();
        }
        return newFile;
    }
    public static String getMainTexFile() {
        Preferences preferences = Preferences.userRoot().node(FileManager.class.getName());
        return preferences.get(MAIN_TEX_FILE_KEY,"") ;
    }


    public  static File getLastOpenedFilePathNew() {
        Preferences preferences = Preferences.userRoot().node(FileManager.class.getName());
        return new File (preferences.get(LAST_OPENED_FILE_KEY,"")) ;
    }

    public static String readFileToString(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
    public static void setMainTexFile(String path) {
        Preferences preferences = Preferences.userRoot().node(FileManager.class.getName());
        preferences.put(MAIN_TEX_FILE_KEY,path);
    }
    public static void saveLastOpenedFilePath(String path) throws IOException {

        Preferences preferences = Preferences.userRoot().node(FileManager.class.getName());
        preferences.put(LAST_OPENED_FILE_KEY,path);

    }

    public static String getCompilerKey() {
        Preferences preferences = Preferences.userRoot().node(FileManager.class.getName());
        return preferences.get(COMPILER_KEY,"") ;

    }

    public static void setCompiler(String compiler) {
        Preferences preferences = Preferences.userRoot().node(FileManager.class.getName());
        preferences.put(COMPILER_KEY,compiler);

    }

    public static File createNewFile(String baseName, String path) throws IOException {
        File file = new File(path, baseName);
        int counter = 1;

        // Extract the filename without extension and the extension itself
        String fileNameWithoutExtension = baseName.substring(0, baseName.lastIndexOf('.'));
        String extension = baseName.substring(baseName.lastIndexOf('.'));



        // Loop to find a unique file name
        while (file.exists()) {
            file = new File(path,fileNameWithoutExtension + "-" + counter + extension);
            counter++;
        }

        // Create the new file

        if (file.createNewFile()) {
            return file;
        } else {
            throw new IOException("Failed to create new file.");
        }
    }


}