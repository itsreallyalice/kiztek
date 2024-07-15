import javax.swing.*;
import java.io.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class FileManager {
    private static final String PROPERTIES_FILE_BASE_NAME = "config";
    private static final String LAST_OPENED_FILE_KEY = "lastOpenedFile";
    private static final String MAIN_TEX_FILE_KEY = "mainTexFile";
    private static final String COMPILER_KEY = "currentCompiler";




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
        Properties properties = new Properties();
        // Load existing properties
        try (InputStream input = FileManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_BASE_NAME + ".properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        if (properties.getProperty(MAIN_TEX_FILE_KEY) == null) {
            return new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
        }
        else{

            return properties.getProperty(MAIN_TEX_FILE_KEY);}
    }
//    public static String getLastOpenedFilePath() {
//        if (resourceBundle.getString(LAST_OPENED_FILE_KEY) == null) {
//            return new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
//        }
//        else{
//            return resourceBundle.getString(LAST_OPENED_FILE_KEY);}
//
//    }

    public  static File getLastOpenedFilePathNew() {
        Properties properties = new Properties();
        try (InputStream input = FileManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_BASE_NAME + ".properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (properties.getProperty(LAST_OPENED_FILE_KEY) == null) {
            File tempFile = new File(new JFileChooser().getFileSystemView().getDefaultDirectory().toString());
            return new File(new JFileChooser().getFileSystemView().getDefaultDirectory().toString());
        }
        else{
            return new File( properties.getProperty(LAST_OPENED_FILE_KEY));
        }
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
        Properties properties = new Properties();
        // Load existing properties
        try (InputStream input = FileManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_BASE_NAME + ".properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ex) {
            System.out.println("why?");
            ex.printStackTrace();
        }
        // Set the new property
        properties.setProperty(MAIN_TEX_FILE_KEY, path);
        // Save properties back to the file
        try {
            properties.store(new FileOutputStream(PROPERTIES_FILE_BASE_NAME + ".properties"), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void saveLastOpenedFilePath(String path) throws IOException {

        Properties properties = new Properties();
        // Load existing properties
        try (InputStream input = FileManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_BASE_NAME + ".properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // Set the new property

        properties.setProperty(LAST_OPENED_FILE_KEY, path);
        // Save properties back to the file
        try {
            properties.store(new FileOutputStream(PROPERTIES_FILE_BASE_NAME + ".properties"), null);
        } catch (IOException e) {
            System.out.println("why?");
            throw new RuntimeException(e);
        }




    }

    public static String getCompilerKey() {
        Properties properties = new Properties();
        // Load existing properties
        try (InputStream input = FileManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_BASE_NAME + ".properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (properties.getProperty(COMPILER_KEY) == null) {
            return "pdflatexbibtexpdflatex";
        }
        else{
            return properties.getProperty(COMPILER_KEY);}

    }

    public static void setCompiler(String compiler) {
        Properties properties = new Properties();
        // Load existing properties
        try (InputStream input = FileManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_BASE_NAME + ".properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // Set the new property
        properties.setProperty(COMPILER_KEY, compiler);
        // Save properties back to the file
        try {
            properties.store(new FileOutputStream(PROPERTIES_FILE_BASE_NAME + ".properties"), null);
        } catch (IOException e) {
            System.out.println("why?");
            throw new RuntimeException(e);
        }

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