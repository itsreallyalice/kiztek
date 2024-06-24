import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.Files;

public class PDFCompiler {
    public static File compile(File latexDocument) {
        try {
            File tempFile = File.createTempFile("tempLatex", ".tex");
            tempFile.deleteOnExit();
//            Files.write(tempFile.toPath(), latexDocument.getBytes());

            ProcessBuilder latex1ProcessBuilder = new ProcessBuilder("pdflatex", latexDocument.getName()).directory(latexDocument.getParentFile());
            latex1ProcessBuilder.redirectErrorStream(true);
            Process latex1Process = latex1ProcessBuilder.start();
            try (InputStream inputStream = latex1Process.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            int latex1ExitCode = latex1Process.waitFor();
            System.out.println("latex[1] exited with code: " + latex1ExitCode);

            //CHANGE SO NO .TEX EXTENSION
            String latexName =  FilenameUtils.removeExtension(latexDocument.getName());
            ProcessBuilder bibtexProcessBuilder = new ProcessBuilder("bibtex", latexName).directory(latexDocument.getParentFile());
            bibtexProcessBuilder.redirectErrorStream(true);
            Process bibtexProcess = bibtexProcessBuilder.start();
            try (InputStream inputStream = bibtexProcess.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            int bibtexExitCode = bibtexProcess.waitFor();
            System.out.println("bibtex exited with code: " + bibtexExitCode);

            ProcessBuilder latex2ProcessBuilder = new ProcessBuilder("pdflatex", latexDocument.getName()).directory(latexDocument.getParentFile());
            latex2ProcessBuilder.redirectErrorStream(true);
            Process latex2Process = latex2ProcessBuilder.start();
            try (InputStream inputStream = latex2Process.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            int latex2ExitCode = latex2Process.waitFor();
            System.out.println("latex[1] exited with code: " + latex2ExitCode);

            ProcessBuilder processBuilder = new ProcessBuilder("pdflatex", "-output-directory=" + tempFile.getParent(), latexDocument.getName())
                    .directory(latexDocument.getParentFile());
            //run bibtex as well
            //run this on a different thread so no freezing.
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
