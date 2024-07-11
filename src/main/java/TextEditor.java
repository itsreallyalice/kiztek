import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextEditor extends JPanel {
    private RSyntaxTextArea textArea;
    private UndoManager undoManager;

    public TextEditor() throws IOException {
        setLayout(new BorderLayout());
        textArea = new RSyntaxTextArea();
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LATEX);
        textArea.setCodeFoldingEnabled(true);


        File tempFile = new File(FileManager.getLastOpenedFilePath());
        String content = FileManager.readFileToString(tempFile);
        textArea.setText(content);


        undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
            }
        });
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public RSyntaxTextArea getTextArea() {
        return textArea;
    }

    public UndoManager getUndoManager() {
        return undoManager;
    }

    public void saveFile() throws IOException {
        FileWriter fileWriter = new FileWriter(FileManager.getLastOpenedFilePath(), false);
        fileWriter.write(textArea.getText());
        fileWriter.close();
        System.out.println(FileManager.getLastOpenedFilePath() + "saved!");
    }
}
