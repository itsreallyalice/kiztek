import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sidebar extends JPanel implements ActionListener {
    private JList<String> fileList;
    private Main mainFrame;

    JCheckBox filterButton = new JCheckBox("Filter");
    public boolean filterToggle = false;

    public Sidebar(Main mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        JToolBar sideToolbar = new JToolBar();
        JButton newFileButton = new JButton("New File");
        JButton uploadButton = new JButton("Upload");


        newFileButton.addActionListener(this);
        uploadButton.addActionListener(this);
        filterButton.addActionListener(this);

        sideToolbar.add(newFileButton);
        sideToolbar.add(uploadButton);
        sideToolbar.add(filterButton);

        add(sideToolbar, BorderLayout.NORTH);

        fileList = new JList<>();
        refreshSidebar();
        fileList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedFile = fileList.getSelectedValue();
                    if (selectedFile != null) {

                        File tempFile = FileManager.getLastOpenedFilePathNew();
                        File file = new File(tempFile.getParent(), selectedFile);
                        System.out.println(file.getAbsolutePath());
                        FileManager.saveLastOpenedFilePath(file.getAbsolutePath());
                        try {
                            String content = FileManager.readFileToString(file);
                            mainFrame.getTextEditorPanel().getTextArea().setText(content);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        add(new JScrollPane(fileList), BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent a) {
        String command = a.getActionCommand();

        switch (command) {
            case "New File":
//                String content = "\\documentclass{article}\n\n"
//                        + "\\begin{document}\n\n"
//                        + "(Type your content here.)\n\n"
//                        + "\\end{document}";
//
//                File newFile;
//                String baseFilename = JOptionPane.showInputDialog(null,"New File","untitled.tex");
//
//
//                try {
//
//                    newFile = FileManager.createNewFile(baseFilename,FileManager.getLastOpenedFilePathNew().getParent());
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//
//                try (FileWriter fileWriter = new FileWriter(newFile)) {
//                    fileWriter.write(content);
//                    System.out.println("File created and content written successfully.");
//                } catch (IOException e) {
//                    System.out.println("An error occurred while creating/writing to the file.");
//                    e.printStackTrace();
//                }
                File newFile = FileManager.createMainTeX("untitled.tex", FileManager.getLastOpenedFilePathNew().getParent());
                String content = null;
                try {
                    content = FileManager.readFileToString(newFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                mainFrame.getTextEditorPanel().getTextArea().setText(content);
                refreshSidebar();
                break;

            case "Upload":

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    } else {
                        String filename = f.getName().toLowerCase();
                        return filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png") || filename.endsWith(".gif");
                    }
                }

                @Override
                public String getDescription() {
                    return "Image Files (*.jpg, *.jpeg, *.png, *.gif)";
                }
            });

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                File tempFile = new File(FileManager.getLastOpenedFilePathNew().getParent(), selectedFile.getName());
                try {
                    Files.copy(selectedFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                refreshSidebar();
                JOptionPane.showMessageDialog(null, "Image uploaded: " + selectedFile.getAbsolutePath());
            }

                break;

            case "Filter":
                refreshSidebar();



        }
    }

    public void refreshSidebar() {
        boolean isFilter = filterButton.isSelected();
        File currentFile = FileManager.getLastOpenedFilePathNew();
        File currentDir = new File(currentFile.getParent());
        ArrayList<String> files = new ArrayList<String>(Arrays.asList(currentDir.list()));

        ArrayList<String> newList = new ArrayList<>();

        if (isFilter) {
            for (String element : files) {
                if (element.endsWith(".bib") || element.endsWith(".tex")) {
                    newList.add(element);
                }
            }
            fileList.setListData(newList.toArray(new String[newList.size()]));
        }
        else{
            fileList.setListData(files.toArray(new String[newList.size()]));
        }

    }


}
