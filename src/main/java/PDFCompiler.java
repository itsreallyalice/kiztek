import java.io.*;
import java.nio.file.Files;

public class PDFCompiler {
    public static File compile(File latexDocument) {
        try {
            File tempFile = File.createTempFile("tempLatex", ".tex");
            tempFile.deleteOnExit();
//            Files.write(tempFile.toPath(), latexDocument.getBytes());

            ProcessBuilder processBuilder = new ProcessBuilder("pdflatex", "-output-directory=" + tempFile.getParent(), latexDocument.getName())
                    .directory(latexDocument.getParentFile());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            try (InputStream inputStream = process.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            process.waitFor();
            int exitCode = process.exitValue();
            if (exitCode == 0) {

                File pdfFile = new File(tempFile.getParent(), latexDocument.getName().replace(".tex", ".pdf"));
                return pdfFile;
            } else {
                System.err.println("Compilation failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
