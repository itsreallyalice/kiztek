import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.scilab.forge.jlatexmath.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Main extends JFrame {
    private JTextArea textField;
    private JButton submitButton;
    private JPanel pdfPanel;

    public Main () {
        setTitle("KiZTeK: The best LaTeX compiler!");

        ImageIcon logo = new ImageIcon(getClass().getResource("images/logo.png"));
        setIconImage(logo.getImage());


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JTextArea textField = new JTextArea();
        JButton submitButton = new JButton("Refresh");
        pdfPanel = new JPanel();

        // Wrap the text area and PDF panel in JScrollPane
        JScrollPane textScrollPane = new JScrollPane(textField);
        JScrollPane pdfScrollPane = new JScrollPane(pdfPanel);
        pdfScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        JPanel bottomPanel = new JPanel(new FlowLayout());


        topPanel.add(textScrollPane);
        topPanel.add(pdfScrollPane);
        bottomPanel.add(submitButton);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String latexExpression = textField.getText();
                compileAndShowPdf(latexExpression);
                JOptionPane.showMessageDialog( Main.this, "Refreshed!");
            }
        });
        setVisible(true);
    }

    private void compileAndShowPdf(String latexDocument) {
        try {
            // Create a temporary LaTeX file
            File tempFile = File.createTempFile("tempLatex", ".tex");
            tempFile.deleteOnExit();

            // Write the LaTeX document to the temporary file
            java.nio.file.Files.write(tempFile.toPath(), latexDocument.getBytes());

            // Compile the LaTeX document to a PDF using pdflatex
            ProcessBuilder processBuilder = new ProcessBuilder("pdflatex", "-output-directory=" + tempFile.getParent(), tempFile.getName())
                    .directory(tempFile.getParentFile());

            processBuilder.redirectErrorStream(true); // Redirect stderr to stdout
            Process process = processBuilder.start();

            // Capture and print the output (including errors)
            try (InputStream inputStream = process.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            process.waitFor();

            // Check if the compilation was successful
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                // Show the PDF using an external PDF viewer (e.g., Adobe Acrobat Reader)
                Path pdfPath = FileSystems.getDefault().getPath(tempFile.getParent(), tempFile.getName().replace(".tex", ".pdf"));
                Desktop.getDesktop().open(pdfPath.toFile());
            } else {
                System.err.println("Compilation failed with exit code: " + exitCode);
            }
            File pdfFile = new File(tempFile.getParent(), tempFile.getName().replace(".tex", ".pdf"));
            displayPdf(pdfFile);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void displayPdf(File pdfFile) {
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            int pageCount = document.getNumberOfPages();
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // Calculate the size based on the page size of the first page
            PDPage firstPage = document.getPage(0);
            float pageWidth = firstPage.getMediaBox().getWidth();
            float pageHeight = firstPage.getMediaBox().getHeight();

            float scaleFactor = 3f;  // Adjust this value to make the PDF smaller

            // Combine pages into a single image
            BufferedImage combinedImage = new BufferedImage(
                    (int) (pageWidth * scaleFactor),
                    (int) (pageHeight * pageCount * scaleFactor),
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics g = combinedImage.getGraphics();
            for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                BufferedImage pageImage = pdfRenderer.renderImageWithDPI(pageIndex, 300, ImageType.RGB);
                combinedImage = combineImagesHorizontally(combinedImage, pageImage);
            }

            // Display the combined image in the pdfPanel
            ImageIcon pdfIcon = new ImageIcon(combinedImage);
            JLabel pdfLabel = new JLabel(pdfIcon);
            pdfPanel.removeAll();
            pdfPanel.add(pdfLabel);
            pdfPanel.revalidate();
            pdfPanel.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage combineImagesHorizontally(BufferedImage image1, BufferedImage image2) {
        int combinedWidth = image1.getWidth() + image2.getWidth();
        int combinedHeight = Math.max(image1.getHeight(), image2.getHeight());

        BufferedImage combinedImage = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = combinedImage.getGraphics();

        g.drawImage(image1, 0, 0, null);
        g.drawImage(image2, image1.getWidth(), 0, null);

        return combinedImage;
    }

    private BufferedImage generateLatexImage(String latexExpression) {
        TeXFormula formula = new TeXFormula(latexExpression);
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.white);
        g2.fillRect(0, 0, image.getWidth(), image.getHeight());
        icon.paintIcon(null, g2, 0, 0);
        g2.dispose();
        return image;
    }



    public static void main(String[] args) {
        new Main();
    }
}
