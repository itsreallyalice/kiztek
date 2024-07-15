//import javax.swing.*;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;
//import javax.swing.event.UndoableEditEvent;
//import javax.swing.event.UndoableEditListener;
//import javax.swing.undo.UndoManager;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.nio.file.FileSystems;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
//import org.fife.ui.rtextarea.*;
//import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
//import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
//import org.icepdf.ri.common.*;
//
//public class Main extends JFrame implements ActionListener {
//    private RSyntaxTextArea textField;
//
//    private JButton submitButton;
//    private JPanel pdfPanel;
//    private JPanel sidebar; // Added sidebar panel
//    JList<String> fileList = new JList<>();
//    private boolean isSidebarVisible = true; // Track sidebar visibility
//    UndoManager undoManager = new UndoManager();
//
//    File lastOpenedFile = new File(FileManager.getLastOpenedFilePath());
//    File mainTeXFile = new File(FileManager.getMainTexFile());
//
//    public Main () throws IOException {
//        setTitle("KiZTeK: The best LaTeX compiler!");
//        ImageIcon logo = new ImageIcon(getClass().getResource("logo.png"));
//        setIconImage(logo.getImage());
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setLocationRelativeTo(null);
//
//        undoManager  = new UndoManager();
//        initUI();
//
//
//
//
//    }
//
//    private void initUI() throws IOException {
//
//        pdfPanel = new JPanel();
//
//        JToolBar toolbar = new JToolBar();
//        JMenuBar menuBar = new JMenuBar();
//
//        JMenu fileMenu = new JMenu("File");
//        JMenuItem newMI = new JMenuItem("New");
//        JMenuItem openMI = new JMenuItem("Open");
//        JMenuItem saveMI = new JMenuItem("Save");
//        JMenuItem saveAsMI = new JMenuItem("Save As");
//        newMI.addActionListener(this);
//        openMI.addActionListener(this);
//        saveMI.addActionListener(this);
//        saveAsMI.addActionListener(this);
//        fileMenu.add(newMI);
//        fileMenu.add(openMI);
//        fileMenu.add(saveMI);
//        fileMenu.add(saveAsMI);
//
//
//
//        JMenu editMenu = new JMenu("Edit");
//        JMenuItem undoMI = new JMenuItem("Undo");
//        JMenuItem redoMI = new JMenuItem("Redo");
//        JMenuItem cutMI = new JMenuItem("Cut");
//        JMenuItem copyMI = new JMenuItem("Copy");
//        JMenuItem pasteMI = new JMenuItem("Paste");
//        //Maybe add change case?
//        undoMI.addActionListener(this);
//        redoMI.addActionListener(this);
//        cutMI.addActionListener(this);
//        copyMI.addActionListener(this);
//        pasteMI.addActionListener(this);
//        editMenu.add(undoMI);
//        editMenu.add(redoMI);
//        editMenu.add(cutMI);
//        editMenu.add(copyMI);
//        editMenu.add(pasteMI);
//
//        JMenu formatMenu = new JMenu("Format");
//
//
//        menuBar.add(fileMenu);
//        menuBar.add(editMenu);
//        menuBar.add(formatMenu);
//
//
//        JButton undoButton = new JButton("Undo");
//        JButton redoButton = new JButton("Redo");
//        JButton saveButton = new JButton("Save");
//        JButton saveAsButton = new JButton("Save As");
//        undoButton.addActionListener(this);
//        redoButton.addActionListener(this);
//        saveButton.addActionListener(this);
//        saveAsButton.addActionListener(this);
//
//
//        // SIDEBAR CONGIF
//        sidebar = new JPanel(); // Initialize the sidebar panel
//        JToolBar sideToolbar = new JToolBar();
//
//
////        JButton newFolderButton = new JButton("New Folder");
//        JButton newFileButton = new JButton("New File");
//        JButton uploadButton = new JButton("Upload");
//
//
//        // newFolderButton.addActionListener(this);
//        newFileButton.addActionListener(this);
//        uploadButton.addActionListener(this);
//
//
//        //   sideToolbar.add(newFolderButton);
//        sideToolbar.add(newFileButton);
//        sideToolbar.add(uploadButton);
//
//
//
//        File currentDir = new File(lastOpenedFile.getParent());
//        String[] files = currentDir.list();
//        fileList = new JList<>(files);
//        if (files == null) {
//            DefaultListModel<String> listModel = new DefaultListModel<>();
//            fileList = new JList<>(listModel);
//        }
//
//
//        // Add list selection listener
//        fileList.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                String tempContent = new String();
//                if (!e.getValueIsAdjusting()) {
//                    String selectedFile = fileList.getSelectedValue();
//
//                    if (selectedFile != null) {
//                        File tempFile = new File(lastOpenedFile.getParent(), selectedFile);
//                        lastOpenedFile = tempFile;
//                        try {
//                            tempContent = FileManager.readFileToString(tempFile);
//                        } catch (IOException ex) {
//                            throw new RuntimeException(ex);
//                        }
//                        textField.setText(tempContent);
//                    }
//                }
//            }
//        });
//        sidebar.setLayout(new BorderLayout());
//        sidebar.add(sideToolbar, BorderLayout.NORTH);
//        sidebar.add(new JScrollPane(fileList), BorderLayout.CENTER);
//
//
//        // build a controller
//        SwingController controller = new SwingController();
//
//        // Build a SwingViewFactory configured with the controller
//
//        CustomViewBuilder factory = new CustomViewBuilder(controller);
//
//        // Use the factory to build a JPanel that is pre-configured
//        //with a complete, active Viewer UI.
//        JPanel viewerComponentPanel = factory.buildViewerPanel();
//
//
//
//
//        // add copy keyboard command
//        ComponentKeyBinding.install(controller, viewerComponentPanel);
//
//        // add interactive mouse link annotation support via callback
//        controller.getDocumentViewController().setAnnotationCallback(
//                new MyAnnotationCallback(
//                        controller.getDocumentViewController()));
//
//        pdfPanel = viewerComponentPanel;
//
//        pdfPanel.setVisible(true);
//
//
//        // Define action listener for the toggle button
//        JButton toggleSidebarButton = new JButton("Toggle Sidebar");
//        toggleSidebarButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (isSidebarVisible) {
//                    sidebar.setVisible(false);
//                    isSidebarVisible = false;
//                    toggleSidebarButton.setText("Show Sidebar");
//                } else {
//                    sidebar.setVisible(true);
//                    isSidebarVisible = true;
//                    toggleSidebarButton.setText("Hide Sidebar");
//                }
//            }
//        });
//
//
//
//
//        toolbar.add(toggleSidebarButton);
//        toolbar.add(undoButton);
//        toolbar.add(redoButton);
//        toolbar.add(saveButton);
//        toolbar.add(saveAsButton);
//
//
//        textField = new RSyntaxTextArea();
//        textField.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LATEX);
//        textField.setCodeFoldingEnabled(true);
//
//
//        if (FileManager.getLastOpenedFilePath() != null) {
//            lastOpenedFile = new File(FileManager.getLastOpenedFilePath());
//
//            if (lastOpenedFile.exists()) {
//
//                String content = FileManager.readFileToString(lastOpenedFile);
//                textField.setText(content);
//            }
//        }
//
//
//        textField.getDocument().addUndoableEditListener(
//                new UndoableEditListener() {
//                    @Override
//                    public void undoableEditHappened(UndoableEditEvent e) {
//                        undoManager.addEdit(e.getEdit());
//                    }
//                }
//        );
//
//
//
//
//
//        JButton submitButton = new JButton("Refresh");
//
////        pdfPanel.setLayout(new FlowLayout());
////        pdfPanel.setSize(2500, 3500);
//
//
//        //sidebar.setPreferredSize(new Dimension(200, 0)); // Set preferred size of sidebar
//
//
//        // Wrap the text area and PDF panel in JScrollPane
//        JScrollPane textScrollPane = new JScrollPane(textField);
//        //JScrollPane pdfScrollPane = new JScrollPane(pdfPanel);
//        //pdfScrollPane.getVerticalScrollBar().setUnitIncrement(20);
//        JPanel topPanel = new JPanel(new FlowLayout());
//        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
//        JPanel bottomPanel = new JPanel(new FlowLayout());
//
//        //topPanel.add(toolbar);
//        centerPanel.add(textScrollPane);
//        centerPanel.add(pdfPanel);
//        bottomPanel.add(submitButton);
//
//        setJMenuBar(menuBar);
//        setLayout(new BorderLayout());
//        add(toolbar, BorderLayout.NORTH);
//        add(centerPanel, BorderLayout.CENTER);
//        add(bottomPanel, BorderLayout.SOUTH);
//
//
//        add(sidebar, BorderLayout.WEST); // Add sidebar to the west
//
//        submitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //Save whatever is inside textfield
//                //Main tex file is compiled instead of text box#
//                try {
//                    saveFile();
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//                controller.openDocument(PDFCompiler.compile(mainTeXFile).getPath());
//                refreshSidebar();
//                JOptionPane.showMessageDialog( Main.this, "Refreshed!");
//            }
//        });
//
//
//
//
//
//
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                super.windowClosing(e);
//                if (lastOpenedFile != null) {
//                    FileManager.saveLastOpenedFilePath(lastOpenedFile.getAbsolutePath());
//                }
//            }
//        });
//
//
//        setVisible(true);
//
//    }
//
//
//    public static void main(String[] args) throws IOException {
//
//        new Main();
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        String s = e.getActionCommand();
//        if (s.equals("Cut")) {
//            textField.cut();
//        }
//        else if (s.equals("Copy")){
//            textField.copy();
//        }
//        else if (s.equals("Paste")){
//            textField.paste();
//        }
//        else if (s.equals("Undo")){
//            undoManager.undo();
//        }
//        else if (s.equals("Redo")){
//            undoManager.redo();
//        }
//        else if (s.equals("Save")){
//            try {
//                saveFile();
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
////            try {
////                FileWriter fileWriter = new FileWriter(lastOpenedFile, false);
////                fileWriter.write(textField.getText());
////                fileWriter.close();
////                System.out.println(lastOpenedFile.getName() + "saved!");
////            } catch (IOException z) {
////                z.printStackTrace();
////        }
//        }
//        else if (s.equals("Save As")) {
//            JFileChooser fileChooser = new JFileChooser();
//            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
//                File file = fileChooser.getSelectedFile();
//                lastOpenedFile = file;
//                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//                    textField.write(writer);
//                } catch (IOException ex) {
//                    JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//            refreshSidebar();
//        }
//        //OPEN MAIN TEX FILE
//        else if (s.equals("Open"))  {
//            JFileChooser fileChooser = new JFileChooser();
//
//
//            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
//
//                mainTeXFile = fileChooser.getSelectedFile();
//                lastOpenedFile = fileChooser.getSelectedFile();
//
//
//                FileManager.setMainTexFile(mainTeXFile.getAbsolutePath());
//                FileManager.saveLastOpenedFilePath(lastOpenedFile.getAbsolutePath());
//                //refresh sidebar
//                refreshSidebar();
////                File currentDir = new File(lastOpenedFile.getParent());
////                String[] files = currentDir.list();
////                fileList.setListData(files);
//
//                try {
//
//                    String content = FileManager.readFileToString(lastOpenedFile);
//
//                    textField.setText(content);
//                } catch (IOException ex) {
//                    JOptionPane.showMessageDialog(this, "Error opening file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        }
//        else if (s.equals("New")) {
//            //Are you sure?/choose directory
//            //Name it (name of folder)
//            // Then set everything to blank
//        }
//        else if (s.equals("New File")) {
//            File newFile;
//            String baseFilename = JOptionPane.showInputDialog(null,"New File","untitled.tex");
//            try {
//
//                newFile = FileManager.createNewFile(baseFilename,lastOpenedFile.getParent());
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//            textField.setText("");
//            refreshSidebar();
//        }
////        else if (s.equals("New Folder")) {
////
////        }
//        else if (s.equals("Upload")) {
//            JFileChooser fileChooser = new JFileChooser();
//            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
//                @Override
//                public boolean accept(File f) {
//                    if (f.isDirectory()) {
//                        return true;
//                    } else {
//                        String filename = f.getName().toLowerCase();
//                        return filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png") || filename.endsWith(".gif");
//                    }
//                }
//
//                @Override
//                public String getDescription() {
//                    return "Image Files (*.jpg, *.jpeg, *.png, *.gif)";
//                }
//            });
//
//            int result = fileChooser.showOpenDialog(null);
//            if (result == JFileChooser.APPROVE_OPTION) {
//                File selectedFile = fileChooser.getSelectedFile();
//                File newFile = new File(lastOpenedFile.getParent(), selectedFile.getName());
//                try {
//                    Files.copy(selectedFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//                refreshSidebar();
//                JOptionPane.showMessageDialog(null, "Image uploaded: " + selectedFile.getAbsolutePath());
//            }
//
//        }
//
//    }
//    private void refreshSidebar() {
//        File currentDir = new File(lastOpenedFile.getParent());
//        String[] files = currentDir.list();
//        fileList.setListData(files);
//    }
//    private void saveFile() throws IOException {
//        FileWriter fileWriter = new FileWriter(lastOpenedFile, false);
//        fileWriter.write(textField.getText());
//        fileWriter.close();
//        System.out.println(lastOpenedFile.getName() + "saved!");
//    }
//}