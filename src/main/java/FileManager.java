import java.io.*;
import java.util.Properties;

public class FileManager {

    private static final String PROPERTIES_FILE = "config.properties";
    private static final String LAST_OPENED_FILE_KEY = "lastOpenedFile";

    public static String getLastOpenedFilePath() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(PROPERTIES_FILE)) {
            properties.load(input);
            return properties.getProperty(LAST_OPENED_FILE_KEY);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
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
    public static void saveLastOpenedFilePath(String path) {
        Properties properties = new Properties();
        properties.setProperty(LAST_OPENED_FILE_KEY, path);
        try (OutputStream output = new FileOutputStream(PROPERTIES_FILE)) {
            properties.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}