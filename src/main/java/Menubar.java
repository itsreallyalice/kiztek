import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Menubar extends JMenuBar implements ActionListener {
    private Main mainFrame;

    public Menubar(Main mainFrame) {
        this.mainFrame = mainFrame;

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

        add(fileMenu);
        add(editMenu);
        add(formatMenu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        TextEditor textEditorPanel = mainFrame.getTextEditorPanel();
        switch (command) {
            case "Cut":
                textEditorPanel.getTextArea().cut();
                break;
            case "Copy":
                textEditorPanel.getTextArea().copy();
                break;
            case "Paste":
                textEditorPanel.getTextArea().paste();
                break;
            case "Undo":
                textEditorPanel.getUndoManager().undo();
                break;
            case "Redo":
                textEditorPanel.getUndoManager().redo();
                break;
            case "Save":
                try {
                    textEditorPanel.saveFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
//            case "Save As":
//                JFileChooser fileChooser = new JFileChooser();
//                if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
//                    File file = fileChooser.getSelectedFile();
//                    mainFrame.setLastOpenedFile(file);
//                    try {
//                        textEditorPanel.saveFile();
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
//                }
//                mainFrame.getSidebarPanel().refreshSidebar();
//                break;
            case "Open":
                JFileChooser openFileChooser = new JFileChooser();
                if (openFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File file = openFileChooser.getSelectedFile();
                    FileManager.saveLastOpenedFilePath(file.getAbsolutePath());
                    FileManager.setMainTexFile(file.getAbsolutePath());
                    try {
                        String content = FileManager.readFileToString(file);
                        textEditorPanel.getTextArea().setText(content);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                mainFrame.getSidebarPanel().refreshSidebar();
                break;
            case "New":
                // Implement the New action logic here
                break;
        }
    }
}
