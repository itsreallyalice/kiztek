import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import org.apache.pdfbox.debugger.ui.ZoomMenu;

public class Main extends JFrame {
    private JTextArea textField;
    private JButton submitButton;
    private JPanel pdfPanel;
    private JPanel sidebar; // Added sidebar panel
    private boolean isSidebarVisible = true; // Track sidebar visibility

    public Main () {
        setTitle("KiZTeK: The best LaTeX compiler!");
        ImageIcon logo = new ImageIcon(getClass().getResource("images/logo.png"));
        setIconImage(logo.getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JToolBar toolbar = new JToolBar();
        JButton newButton = new JButton("New");
        JButton uploadButton = new JButton("Open");
        JButton exportButton = new JButton("Export");


        // Define action listener for the toggle button
        JButton toggleSidebarButton = new JButton("Toggle Sidebar");
        toggleSidebarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isSidebarVisible) {
                    sidebar.setVisible(false);
                    isSidebarVisible = false;
                    toggleSidebarButton.setText("Show Sidebar");
                } else {
                    sidebar.setVisible(true);
                    isSidebarVisible = true;
                    toggleSidebarButton.setText("Hide Sidebar");
                }
            }
        });
        toolbar.add(toggleSidebarButton);


        toolbar.add(newButton);
        toolbar.add(uploadButton);
        toolbar.add(exportButton);


        JTextArea textField = new JTextArea();
        JButton submitButton = new JButton("Refresh");
        pdfPanel = new JPanel();
        sidebar = new JPanel(); // Initialize the sidebar panel
        sidebar.setPreferredSize(new Dimension(200, 0)); // Set preferred size of sidebar


        // Wrap the text area and PDF panel in JScrollPane
        JScrollPane textScrollPane = new JScrollPane(textField);
        JScrollPane pdfScrollPane = new JScrollPane(pdfPanel);
        pdfScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        JPanel bottomPanel = new JPanel(new FlowLayout());

        //topPanel.add(toolbar);
        centerPanel.add(textScrollPane);
        centerPanel.add(pdfScrollPane);
        bottomPanel.add(submitButton);

        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        add(sidebar, BorderLayout.WEST); // Add sidebar to the west

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String latexExpression = textField.getText();
                compileAndShowPdf(latexExpression);
                JOptionPane.showMessageDialog( Main.this, "Refreshed!");
            }
        });


        // Display files in the current directory
        File currentDir = new File(".");
        String[] files = currentDir.list();
        JList<String> fileList = new JList<>(files);
        sidebar.add(new JScrollPane(fileList), BorderLayout.CENTER);

        ZoomMenu zoom;
        

        setVisible(true);


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
                //Path pdfPath = FileSystems.getDefault().getPath(tempFile.getParent(), tempFile.getName().replace(".tex", ".pdf"));
                //Desktop.getDesktop().open(pdfPath.toFile());
            } else {
                System.err.println("Compilation failed with exit code: " + exitCode);
            }
            File pdfFile = new File(tempFile.getParent(), tempFile.getName().replace(".tex", ".pdf"));
            currentPdfFile = pdfFile;
            displayPdf(pdfFile);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private float scaleFactor = 1.5f; // Initial scale factor
    private File currentPdfFile;

    private void displayPdf(File pdfFile) {
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            int pageCount = document.getNumberOfPages();
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // Calculate the size based on the page size of the first page
            PDPage firstPage = document.getPage(0);
            float pageWidth = firstPage.getMediaBox().getWidth();
            float pageHeight = firstPage.getMediaBox().getHeight();

            // Adjust the image size based on the scale factor
            int scaledWidth = (int) (pageWidth * scaleFactor);
            int scaledHeight = (int) (pageHeight * scaleFactor);

            // Combine pages into a single image
            BufferedImage combinedImage = new BufferedImage(
                    scaledWidth,
                    scaledHeight * pageCount,
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics g = combinedImage.getGraphics();
            for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                BufferedImage pageImage = pdfRenderer.renderImageWithDPI(pageIndex, 300 * scaleFactor, ImageType.RGB);
                g.drawImage(pageImage, 0, pageIndex * scaledHeight, scaledWidth, scaledHeight, null);
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

    // Zoom in by increasing the scale factor
    private void zoomIn() {
        scaleFactor += 0.1f; // You can adjust the zoom level
        displayPdf(currentPdfFile);
    }

    // Zoom out by decreasing the scale factor
    private void zoomOut() {
        scaleFactor -= 0.1f; // You can adjust the zoom level
        if (scaleFactor < 0.1f) scaleFactor = 0.1f; // Limit minimum scale
        displayPdf(currentPdfFile);
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

    public static void main(String[] args) {
        new Main();
    }
}