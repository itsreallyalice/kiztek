import org.fife.ui.rtextarea.RTextArea;

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

        newMI.addActionListener(this);
        openMI.addActionListener(this);
        saveMI.addActionListener(this);

        fileMenu.add(newMI);
        fileMenu.add(openMI);
        fileMenu.add(saveMI);


        JMenu editMenu = new JMenu("Edit");

        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.UNDO_ACTION)));
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.REDO_ACTION)));
        editMenu.addSeparator();
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.CUT_ACTION)));
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.COPY_ACTION)));
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.PASTE_ACTION)));
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.DELETE_ACTION)));
        editMenu.addSeparator();
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.SELECT_ALL_ACTION)));




//        JMenuItem undoMI = new JMenuItem("Undo");
//        JMenuItem redoMI = new JMenuItem("Redo");
//        JMenuItem cutMI = new JMenuItem("Cut");
//        JMenuItem copyMI = new JMenuItem("Copy");
//        JMenuItem pasteMI = new JMenuItem("Paste");
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



        add(fileMenu);
        add(editMenu);







    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        TextEditor textEditorPanel = mainFrame.getTextEditorPanel();
        switch (command) {
            case "Save":
                try {
                    textEditorPanel.saveFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case "Open":
                JFileChooser openFileChooser = new JFileChooser("c:/Documents");

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
                JFileChooser newFileChooser = new JFileChooser();
                newFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (newFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File dir = newFileChooser.getSelectedFile();
                    File file;
                    file = FileManager.createMainTeX("main.tex",dir.getAbsolutePath());
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
        }
    }
    private static JMenuItem createMenuItem(Action action) {
        JMenuItem item = new JMenuItem(action);
        item.setToolTipText(null); // Swing annoyingly adds tool tip text to the menu item
        return item;
    }
}
