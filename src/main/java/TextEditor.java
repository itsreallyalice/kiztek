import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
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


        File tempFile = FileManager.getLastOpenedFilePath();

        String content = "";
        try {content = FileManager.readFileToString(tempFile);}

        catch (IOException e) {
            textArea.setText("");
        }

        textArea.setText(content);

        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public RSyntaxTextArea getTextArea() {
        return textArea;
    }


    public void saveFile() throws IOException {
        File tempFile = FileManager.getLastOpenedFilePath();
        FileWriter fileWriter = new FileWriter(tempFile, false);
        fileWriter.write(textArea.getText());
        fileWriter.close();
        System.out.println(FileManager.getLastOpenedFilePath() + "saved!");
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
