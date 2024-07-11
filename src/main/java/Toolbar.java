import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Toolbar extends JToolBar implements ActionListener {
    private Main mainFrame;
    private File mainTeXFile;

    public Toolbar(Main mainFrame) {
        this.mainFrame = mainFrame;

        JButton toggleSidebarButton = new JButton("Toggle Sidebar");
        JButton reCompileButton = new JButton("Recompile");
        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");
        JButton saveButton = new JButton("Save");
        JButton saveAsButton = new JButton("Save As");

        toggleSidebarButton.addActionListener(this);
        reCompileButton.addActionListener(this);
        undoButton.addActionListener(this);
        redoButton.addActionListener(this);
        saveButton.addActionListener(this);
        saveAsButton.addActionListener(this);

        add(toggleSidebarButton);
        add(undoButton);
        add(redoButton);
        add(saveButton);
        add(saveAsButton);
        add(reCompileButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        TextEditor textEditorPanel = mainFrame.getTextEditorPanel();
        switch (command) {
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
            case "Toggle Sidebar":

            case "Recompile":
                                //Save whatever is inside textfield
                //Main tex file is compiled instead of text box#
                try {
                    textEditorPanel.saveFile();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                mainFrame.getPdfViewerPanel().getController().openDocument(PDFCompiler.compile(new File(FileManager.getMainTexFile())).getPath());

                mainFrame.getSidebarPanel().refreshSidebar();
                JOptionPane.showMessageDialog( this, "Refreshed!");
        }
    }
}
