import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Sidebar extends JPanel {
    private JList<String> fileList;
    private Main mainFrame;

    public Sidebar(Main mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        JToolBar sideToolbar = new JToolBar();
        JButton newFileButton = new JButton("New File");
        JButton uploadButton = new JButton("Upload");
        newFileButton.addActionListener(mainFrame.getToolbarPanel());
        uploadButton.addActionListener(mainFrame.getToolbarPanel());
        sideToolbar.add(newFileButton);
        sideToolbar.add(uploadButton);
        add(sideToolbar, BorderLayout.NORTH);

        fileList = new JList<>();
        refreshSidebar();
        fileList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedFile = fileList.getSelectedValue();
                    if (selectedFile != null) {

                        File tempFile = new File(FileManager.getLastOpenedFilePath());
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

    public void refreshSidebar() {
        File currentFile = new File(FileManager.getLastOpenedFilePath());
        File currentDir = new File(currentFile.getParent());
        String[] files = currentDir.list();
        fileList.setListData(files != null ? files : new String[0]);
    }
}
