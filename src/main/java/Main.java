import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

public class Main extends JFrame {
    private JTextArea textField;
    private JButton submitButton;
    private JPanel pdfPanel;
    private JPanel sidebar; // Added sidebar panel
    private boolean isSidebarVisible = true; // Track sidebar visibility

    public Main () {
        setTitle("KiZTeK: The best LaTeX compiler!");
        ImageIcon logo = new ImageIcon(getClass().getResource("logo.png"));
        setIconImage(logo.getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        initUI();




    }

    private void initUI() {
        pdfPanel = new JPanel();
        JToolBar toolbar = new JToolBar();
        JButton newButton = new JButton("New");
        JButton uploadButton = new JButton("Open");
        JButton exportButton = new JButton("Export");


        // build a controller
        SwingController controller = new SwingController();

        // Build a SwingViewFactory configured with the controller
        SwingViewBuilder factory = new SwingViewBuilder(controller);

        // Use the factory to build a JPanel that is pre-configured
        //with a complete, active Viewer UI.
        JPanel viewerComponentPanel = factory.buildViewerPanel();


        //COPY SWINGVIEWBUILDER AND CHANGE "addToMenu" lines?

        // add copy keyboard command
        ComponentKeyBinding.install(controller, viewerComponentPanel);

        // add interactive mouse link annotation support via callback
        controller.getDocumentViewController().setAnnotationCallback(
                new org.icepdf.ri.common.MyAnnotationCallback(
                        controller.getDocumentViewController()));

        pdfPanel.add(viewerComponentPanel);

        pdfPanel.setVisible(true);


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

//        pdfPanel.setLayout(new FlowLayout());
//        pdfPanel.setSize(2500, 3500);

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
                controller.openDocument(PDFCompiler.compile(latexExpression).getPath());
                JOptionPane.showMessageDialog( Main.this, "Refreshed!");
            }
        });


        // Display files in the current directory
        File currentDir = new File(".");
        String[] files = currentDir.list();
        JList<String> fileList = new JList<>(files);
        sidebar.add(new JScrollPane(fileList), BorderLayout.CENTER);




        setVisible(true);


        setVisible(true);

    }


    public static void main(String[] args) {
        new Main();
    }
}