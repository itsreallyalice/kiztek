import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

import javax.swing.*;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Toolbar extends JToolBar implements ActionListener   {
    private Main mainFrame;
    private File mainTeXFile;

    private JTextField searchField;


    private boolean isSidebarVisible = true;

    public Toolbar(Main mainFrame) {
        this.mainFrame = mainFrame;

        JButton toggleSidebarButton = new JButton("Toggle Sidebar");
        JButton reCompileButton = new JButton("Recompile");
        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");
        JButton saveButton = new JButton("Save");


        toggleSidebarButton.addActionListener(this);
        reCompileButton.addActionListener(this);
        undoButton.addActionListener(this);
        redoButton.addActionListener(this);
        saveButton.addActionListener(this);


        add(toggleSidebarButton);

        addSeparator();

        searchField = new JTextField(10);
        searchField.setMaximumSize(searchField.getPreferredSize());

        add(searchField);
        final JButton nextButton = new JButton("Find Next");
        nextButton.setActionCommand("FindNext");
        nextButton.addActionListener(this);
        add(nextButton);
        searchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextButton.doClick(0);
            }
        });
        JButton prevButton = new JButton("Find Previous");
        prevButton.setActionCommand("FindPrev");
        prevButton.addActionListener(this);
        add(prevButton);

        addSeparator();

        add(saveButton);
        add(undoButton);
        add(redoButton);

        addSeparator();
        //add(saveAsButton);
        add(Box.createGlue());
        add(reCompileButton);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        TextEditor textEditorPanel = mainFrame.getTextEditorPanel();
        Sidebar sidebarPanel = mainFrame.getSidebarPanel();

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

            case "Toggle Sidebar":
                if (isSidebarVisible) {
                    sidebarPanel.setVisible(false);
                    isSidebarVisible = false;

                } else {
                    sidebarPanel.setVisible(true);
                    isSidebarVisible = true;

                }
                break;
            case "Recompile":
                                //Save whatever is inside textfield
                //Main tex file is compiled instead of text box#
                try {
                    textEditorPanel.saveFile();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                PDFCompiler compiler = new PDFCompiler();
                compiler.setErrorListener(mainFrame);
                try {
                    mainFrame.getPdfViewerPanel().getController().openDocument(PDFCompiler.compile(new File(FileManager.getMainTexFile())).getPath());
                } catch (TransformerException ex) {
                    throw new RuntimeException(ex);
                }

                mainFrame.getSidebarPanel().refreshSidebar();
                JOptionPane.showMessageDialog( this, "Refreshed!");
        }

        // "FindNext" => search forward, "FindPrev" => search backward

        boolean forward = "FindNext".equals(command);

        // Create an object defining our search parameters.
        SearchContext context = new SearchContext();
        String text = searchField.getText();
        if (text.length() == 0) {
            return;
        }
        context.setSearchFor(text);
        context.setSearchForward(forward);
        context.setWholeWord(false);

        boolean found = SearchEngine.find(mainFrame.getTextEditorPanel().getTextArea(), context).wasFound();
        if (!found) {
            JOptionPane.showMessageDialog(this, "Text not found");
        }

    }
}
