import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PDFCompiler {

    public static ErrorListener errorListener;
    public void setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }









    public static File compilepdflatexbibtexpdflatex(File latexDocument) throws TransformerException {
        String line;
        StringBuilder outputMsg = new StringBuilder();
        try {
            File tempFile = File.createTempFile("tempLatex", ".tex");
            tempFile.deleteOnExit();
//            Files.write(tempFile.toPath(), latexDocument.getBytes());

            ProcessBuilder latex1ProcessBuilder = new ProcessBuilder("pdflatex", "-interaction=nonstopmode", latexDocument.getName()).directory(latexDocument.getParentFile());
            latex1ProcessBuilder.redirectErrorStream(true);
            Process latex1Process = latex1ProcessBuilder.start();
            try (InputStream inputStream = latex1Process.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);

                }
            }
            int latex1ExitCode = latex1Process.waitFor();
            System.out.println("latex[1] exited with code: " + latex1ExitCode);

            //CHANGE SO NO .TEX EXTENSION
            String latexName =  FilenameUtils.removeExtension(latexDocument.getName());
            ProcessBuilder bibtexProcessBuilder = new ProcessBuilder("bibtex", "-interaction=nonstopmode",latexName).directory(latexDocument.getParentFile());
            bibtexProcessBuilder.redirectErrorStream(true);
            Process bibtexProcess = bibtexProcessBuilder.start();
            try (InputStream inputStream = bibtexProcess.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {


                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);

                }
            }
            int bibtexExitCode = bibtexProcess.waitFor();
            System.out.println("bibtex exited with code: " + bibtexExitCode);

            ProcessBuilder latex2ProcessBuilder = new ProcessBuilder("pdflatex", "-interaction=nonstopmode",latexDocument.getName()).directory(latexDocument.getParentFile());
            latex2ProcessBuilder.redirectErrorStream(true);
            Process latex2Process = latex2ProcessBuilder.start();
            try (InputStream inputStream = latex2Process.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {


                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);

                }
            }
            int latex2ExitCode = latex2Process.waitFor();
            System.out.println("latex[1] exited with code: " + latex2ExitCode);

            ProcessBuilder processBuilder = new ProcessBuilder("pdflatex", "-interaction=nonstopmode", "-output-directory=" + tempFile.getParent(), latexDocument.getName())
                    .directory(latexDocument.getParentFile());
            //run bibtex as well
            //run this on a different thread so no freezing.
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            try (InputStream inputStream = process.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {


                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                    outputMsg.append(line).append(System.lineSeparator());
                }
            }

            process.waitFor();
            errorGenerator(outputMsg.toString());
            int exitCode = process.exitValue();
            File pdfFile = new File(latexDocument.getParent(), latexDocument.getName().replace(".tex", ".pdf"));
            System.err.println("Compilation failed with exit code: " + exitCode);
            return pdfFile;




        } catch (IOException | InterruptedException e) {

            if (errorListener != null) {
                errorListener.error((TransformerException) e);
            }
//            showErrorDialog(e.getMessage())
        }
        return null;
    }
    public static File compilepdflatex(File latexDocument) throws TransformerException {
        String line;
        StringBuilder outputMsg = new StringBuilder();
        try {
            File tempFile = File.createTempFile("tempLatex", ".tex");
            tempFile.deleteOnExit();

            ProcessBuilder latexProcessBuilder = new ProcessBuilder("pdflatex", "-interaction=nonstopmode", latexDocument.getName()).directory(latexDocument.getParentFile());
            latexProcessBuilder.redirectErrorStream(true);
            Process latexProcess = latexProcessBuilder.start();
            try (InputStream inputStream = latexProcess.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {



                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                    outputMsg.append(line).append(System.lineSeparator());

                }

            }
            int latex1ExitCode = latexProcess.waitFor();
            System.out.println("latex[1] exited with code: " + latex1ExitCode);
            latexProcess.waitFor();
            errorGenerator(outputMsg.toString());
            int exitCode = latexProcess.exitValue();






            File pdfFile = new File(latexDocument.getParent(), latexDocument.getName().replace(".tex", ".pdf"));
            System.err.println("Compilation failed with exit code: " + exitCode);
            return pdfFile;




        }catch (IOException | InterruptedException e) {

            if (errorListener != null) {
                errorListener.error((TransformerException) e);
            }
        }
        return null;}

    public static File compilebibtex(File latexDocument) throws TransformerException {
        String line;
        StringBuilder outputMsg = new StringBuilder();
        try {


            String latexName =  FilenameUtils.removeExtension(latexDocument.getName());
            System.out.println(latexName);
            ProcessBuilder bibtexProcessBuilder = new ProcessBuilder("bibtex", "-interaction=nonstopmode",latexName).directory(latexDocument.getParentFile());
            bibtexProcessBuilder.redirectErrorStream(true);
            Process latexProcess = bibtexProcessBuilder.start();
            try (InputStream inputStream = latexProcess.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {


                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                    outputMsg.append(line).append(System.lineSeparator());
                }
            }
            int latex1ExitCode = latexProcess.waitFor();
            System.out.println("bibtex exited with code: " + latex1ExitCode);
            latexProcess.waitFor();
            errorGenerator(outputMsg.toString());
            int exitCode = latexProcess.exitValue();

            System.err.println("Compilation failed with exit code: " + exitCode);
                File pdfFile = new File(latexDocument.getParent(), latexDocument.getName().replace(".tex", ".pdf"));
                return pdfFile;



        }catch (IOException | InterruptedException e) {

            if (errorListener != null) {
                errorListener.error((TransformerException) e);
            }
        }
        return null;}

    public static File compilexelatex(File latexDocument) throws TransformerException {
        String line;
        StringBuilder outputMsg = new StringBuilder();
        try {
            File tempFile = File.createTempFile("tempLatex", ".tex");
            tempFile.deleteOnExit();

            String latexName =  FilenameUtils.removeExtension(latexDocument.getName());
            ProcessBuilder xetexProcessBuilder = new ProcessBuilder("xelatex", "-interaction=nonstopmode",latexName).directory(latexDocument.getParentFile());
            xetexProcessBuilder.redirectErrorStream(true);
            Process latexProcess = xetexProcessBuilder.start();
            try (InputStream inputStream = latexProcess.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {


                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                    outputMsg.append(line).append(System.lineSeparator());
                }
            }
            int latex1ExitCode = latexProcess.waitFor();
            System.out.println("latex[1] exited with code: " + latex1ExitCode);
            latexProcess.waitFor();
            errorGenerator(outputMsg.toString());
            int exitCode = latexProcess.exitValue();

            System.err.println("Compilation failed with exit code: " + exitCode);
                File pdfFile = new File(latexDocument.getParent(), latexDocument.getName().replace(".tex", ".pdf"));
                return pdfFile;



        }catch (IOException | InterruptedException e) {

            if (errorListener != null) {
                errorListener.error((TransformerException) e);
            }
        }
        return null;}

    public static File compilelualatex(File latexDocument) throws TransformerException {
        String line;
        StringBuilder outputMsg = new StringBuilder();
        try {
            File tempFile = File.createTempFile("temp", ".tex");
            tempFile.deleteOnExit();

            String latexName =  FilenameUtils.removeExtension(latexDocument.getName());
            ProcessBuilder xetexProcessBuilder = new ProcessBuilder("lualatex", "-interaction=nonstopmode",latexName).directory(latexDocument.getParentFile());
            xetexProcessBuilder.redirectErrorStream(true);
            Process latexProcess = xetexProcessBuilder.start();
            try (InputStream inputStream = latexProcess.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {


                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                    outputMsg.append(line).append(System.lineSeparator());
                }
            }
            int latex1ExitCode = latexProcess.waitFor();
            System.out.println("latex[1] exited with code: " + latex1ExitCode);
            latexProcess.waitFor();
            errorGenerator(outputMsg.toString());
            int exitCode = latexProcess.exitValue();

            System.err.println("Compilation failed with exit code: " + exitCode);
                File pdfFile = new File(latexDocument.getParent(), latexDocument.getName().replace(".tex", ".pdf"));
                return pdfFile;



        }catch (IOException | InterruptedException e) {

            if (errorListener != null) {
                errorListener.error((TransformerException) e);
            }
        }
        return null;}

    public static void errorGenerator(String output) {

        List<String> errorLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new StringReader(output))) {
            String line;
            while ((line = br.readLine()) != null) {
                //LaTeX errors can only start with these characters
                if (line.startsWith("?") || line.startsWith("!") || line.startsWith("LaTeX Warning") || line.startsWith("l.")) {
                    errorLines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error message invalid");
        }
        // Display the lines in a dialog box
        if (!errorLines.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (String line : errorLines) {
                message.append(line).append("\n");
            }
            JOptionPane.showMessageDialog(null, message.toString(), "Errors/Warnings", JOptionPane.ERROR_MESSAGE);
        } else {
            //No errors
        }
    }


}
