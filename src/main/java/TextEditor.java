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


        File tempFile = FileManager.getLastOpenedFilePathNew();
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
        File tempFile = FileManager.getLastOpenedFilePathNew();
        FileWriter fileWriter = new FileWriter(tempFile, false);
        fileWriter.write(textArea.getText());
        fileWriter.close();
        System.out.println(FileManager.getLastOpenedFilePathNew() + "saved!");
    }

    public void insertAtCursor(RSyntaxTextArea textField, String text) {
        int pos = textField.getCaretPosition();
        String currentText = textField.getText();
        StringBuilder sb = new StringBuilder(currentText);
        sb.insert(pos, text);
        textField.setText(sb.toString());

        textField.setCaretPosition(pos + text.length() - 1); // Place cursor inside the curly brackets
    }
}
