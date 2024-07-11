import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.icepdf.ri.common.*;

public class Main extends JFrame {
    private TextEditor textEditorPanel;
    private PDFViewer pdfViewerPanel;
    private Toolbar toolbarPanel;
    private Sidebar sidebarPanel;

    private Menubar menubarPanel;

    public Main () throws IOException {

        setTitle("KiZTeK: The best LaTeX compiler!");
        ImageIcon logo = new ImageIcon(getClass().getResource("logo.png"));
        setIconImage(logo.getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        //lastOpenedFile = new File(FileManager.getLastOpenedFilePath());

        textEditorPanel = new TextEditor();
        pdfViewerPanel = new PDFViewer();
        toolbarPanel = new Toolbar(this);
        sidebarPanel = new Sidebar(this);
        menubarPanel = new Menubar(this);

        setJMenuBar(menubarPanel);
        setLayout(new BorderLayout());
        add(toolbarPanel, BorderLayout.NORTH);
        add(textEditorPanel, BorderLayout.CENTER);
        add(pdfViewerPanel, BorderLayout.EAST);
        add(sidebarPanel, BorderLayout.WEST);


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                    //FileManager.saveLastOpenedFilePath(lastOpenedFile.getAbsolutePath());

            }
        });

        setVisible(true);
    }
    public TextEditor getTextEditorPanel() {
        return textEditorPanel;
    }

    public PDFViewer getPdfViewerPanel() {
        return pdfViewerPanel;
    }

    public Toolbar getToolbarPanel() {return  toolbarPanel;}

    public Sidebar getSidebarPanel() {
        return sidebarPanel;
    }




    public static void main(String[] args) throws IOException {

        new Main();
    }
}


