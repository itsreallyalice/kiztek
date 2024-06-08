import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.CustomViewBuilder;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

public class Main extends JFrame implements ActionListener {
    private RSyntaxTextArea textField;

    private JButton submitButton;
    private JPanel pdfPanel;
    private JPanel sidebar; // Added sidebar panel
    private boolean isSidebarVisible = true; // Track sidebar visibility
    UndoManager undoManager = new UndoManager();

    File lastOpenedFile = new File(FileManager.getLastOpenedFilePath());

    public Main () throws IOException {
        setTitle("KiZTeK: The best LaTeX compiler!");
        ImageIcon logo = new ImageIcon(getClass().getResource("logo.png"));
        setIconImage(logo.getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        undoManager  = new UndoManager();
        initUI();




    }

    private void initUI() throws IOException {

        pdfPanel = new JPanel();

        JToolBar toolbar = new JToolBar();
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newMI = new JMenuItem("New");
        JMenuItem openMI = new JMenuItem("Open");
        JMenuItem saveMI = new JMenuItem("Save");
        JMenuItem saveAsMI = new JMenuItem("Save As");
        newMI.addActionListener(this);
        openMI.addActionListener(this);
        saveMI.addActionListener(this);
        saveAsMI.addActionListener(this);
        fileMenu.add(newMI);
        fileMenu.add(openMI);
        fileMenu.add(saveMI);
        fileMenu.add(saveAsMI);



        JMenu editMenu = new JMenu("Edit");
        JMenuItem undoMI = new JMenuItem("Undo");
        JMenuItem redoMI = new JMenuItem("Redo");
        JMenuItem cutMI = new JMenuItem("Cut");
        JMenuItem copyMI = new JMenuItem("Copy");
        JMenuItem pasteMI = new JMenuItem("Paste");
        //Maybe add change case?
        undoMI.addActionListener(this);
        redoMI.addActionListener(this);
        cutMI.addActionListener(this);
        copyMI.addActionListener(this);
        pasteMI.addActionListener(this);
        editMenu.add(undoMI);
        editMenu.add(redoMI);
        editMenu.add(cutMI);
        editMenu.add(copyMI);
        editMenu.add(pasteMI);

        JMenu formatMenu = new JMenu("Format");


        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);


        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");
        JButton saveButton = new JButton("Save");
        JButton saveAsButton = new JButton("Save As");
        undoButton.addActionListener(this);
        redoButton.addActionListener(this);
        saveButton.addActionListener(this);
        saveAsButton.addActionListener(this);


        // build a controller
        SwingController controller = new SwingController();

        // Build a SwingViewFactory configured with the controller

        CustomViewBuilder factory = new CustomViewBuilder(controller);

        // Use the factory to build a JPanel that is pre-configured
        //with a complete, active Viewer UI.
        JPanel viewerComponentPanel = factory.buildViewerPanel();




        // add copy keyboard command
        ComponentKeyBinding.install(controller, viewerComponentPanel);

        // add interactive mouse link annotation support via callback
        controller.getDocumentViewController().setAnnotationCallback(
                new org.icepdf.ri.common.MyAnnotationCallback(
                        controller.getDocumentViewController()));

        pdfPanel = viewerComponentPanel;

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
        toolbar.add(undoButton);
        toolbar.add(redoButton);
        toolbar.add(saveButton);
        toolbar.add(saveAsButton);


        textField = new RSyntaxTextArea();
        textField.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LATEX);
        textField.setCodeFoldingEnabled(true);

        if (FileManager.getLastOpenedFilePath() != null) {
            lastOpenedFile = new File(FileManager.getLastOpenedFilePath());

            if (lastOpenedFile.exists()) {

                String content = FileManager.readFileToString(lastOpenedFile);
                textField.setText(content);
            }
        }


        textField.getDocument().addUndoableEditListener(
                new UndoableEditListener() {
                    @Override
                    public void undoableEditHappened(UndoableEditEvent e) {
                        undoManager.addEdit(e.getEdit());
                    }
                }
        );





        JButton submitButton = new JButton("Refresh");

//        pdfPanel.setLayout(new FlowLayout());
//        pdfPanel.setSize(2500, 3500);

        sidebar = new JPanel(); // Initialize the sidebar panel
        sidebar.setPreferredSize(new Dimension(200, 0)); // Set preferred size of sidebar


        // Wrap the text area and PDF panel in JScrollPane
        JScrollPane textScrollPane = new JScrollPane(textField);
        //JScrollPane pdfScrollPane = new JScrollPane(pdfPanel);
        //pdfScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        JPanel bottomPanel = new JPanel(new FlowLayout());

        //topPanel.add(toolbar);
        centerPanel.add(textScrollPane);
        centerPanel.add(pdfPanel);
        bottomPanel.add(submitButton);

        setJMenuBar(menuBar);
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




        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (lastOpenedFile != null) {
                    FileManager.saveLastOpenedFilePath(lastOpenedFile.getAbsolutePath());
                }
            }
        });


        setVisible(true);

    }


    public static void main(String[] args) throws IOException {

        new Main();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("Cut")) {
            textField.cut();
        }
        else if (s.equals("Copy")){
            textField.copy();
        }
        else if (s.equals("Paste")){
            textField.paste();
        }
        else if (s.equals("Undo")){
            undoManager.undo();
        }
        else if (s.equals("Redo")){
            undoManager.redo();
        }
        else if (s.equals("Save")){
            try {
                FileWriter fileWriter = new FileWriter(lastOpenedFile, false);
                fileWriter.write(textField.getText());
                fileWriter.close();
            } catch (IOException z) {
                z.printStackTrace();
        }
        }
        else if (s.equals("Save As")) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                lastOpenedFile = file;
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    textField.write(writer);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if (s.equals("Open"))  {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                lastOpenedFile = fileChooser.getSelectedFile();
                try {
                    String content = FileManager.readFileToString(lastOpenedFile);
                    textField.setText(content);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error opening file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if (s.equals("New")) {

        }

    }
}