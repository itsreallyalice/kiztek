import java.io.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class FileManager {
    private static final String PROPERTIES_FILE_BASE_NAME = "config";
    private static final String LAST_OPENED_FILE_KEY = "lastOpenedFile";
    private static final String MAIN_TEX_FILE_KEY = "mainTexFile";
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(PROPERTIES_FILE_BASE_NAME);




    public static String getMainTexFile() {return resourceBundle.getString(MAIN_TEX_FILE_KEY);}
    public static String getLastOpenedFilePath() {
        return resourceBundle.getString(LAST_OPENED_FILE_KEY);
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
        properties.setProperty(LAST_OPENED_FILE_KEY, path);
        // Save properties back to the file
        try (OutputStream output = new FileOutputStream(new File(FileManager.class.getClassLoader().getResource(PROPERTIES_FILE_BASE_NAME + ".properties").toURI()))) {
            properties.store(output, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}