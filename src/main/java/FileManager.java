import javax.swing.*;
import java.io.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class FileManager {
    private static final String PROPERTIES_FILE_BASE_NAME = "config";
    private static final String LAST_OPENED_FILE_KEY = "lastOpenedFile";
    private static final String MAIN_TEX_FILE_KEY = "mainTexFile";





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
            ex.printStackTrace();
        }
        // Set the new property
        properties.setProperty(MAIN_TEX_FILE_KEY, path);
        // Save properties back to the file
        try (OutputStream output = new FileOutputStream(new File(FileManager.class.getClassLoader().getResource(PROPERTIES_FILE_BASE_NAME + ".properties").toURI()))) {
            properties.store(output, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    public static void saveLastOpenedFilePath(String path) {

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
        System.out.println(path);
        properties.setProperty(LAST_OPENED_FILE_KEY, path);
        // Save properties back to the file
        try (OutputStream output = new FileOutputStream(new File(FileManager.class.getClassLoader().getResource(PROPERTIES_FILE_BASE_NAME + ".properties").toURI()))) {
            properties.store(output, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("last opened file" + path);
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