import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

import javax.swing.*;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Toolbar extends JToolBar implements ActionListener   {
    private Main mainFrame;
    private File mainTeXFile;

    private JTextField searchField;


    private boolean isSidebarVisible = true;

    public Toolbar(Main mainFrame) {
        this.mainFrame = mainFrame;

        JButton toggleSidebarButton = new JButton("Toggle Sidebar");
        JButton reCompileButton = new JButton("Recompile");
        JButton undoButton = new JButton(RTextArea.getAction(RTextArea.UNDO_ACTION));
        JButton redoButton = new JButton(RTextArea.getAction(RTextArea.REDO_ACTION));
        JButton saveButton = new JButton("Save");
        JButton settingsButton = new JButton("Settings");

        JButton sectionButton = new JButton("Section");

        // Create the dropdown menu
        JPopupMenu sectionMenu = new JPopupMenu();
        JMenuItem sectionItem = new JMenuItem("Section");
        JMenuItem subsectionItem = new JMenuItem("Subsection");
        JMenuItem subsubsectionItem = new JMenuItem("Subsubsection");
        JMenuItem paragraphItem = new JMenuItem("Paragraph");
        JMenuItem subparagraphItem = new JMenuItem("Subparagraph");

        JButton boldButton = new JButton("Bold");
        JButton italicButton = new JButton("Italic");

        JButton insertButton = new JButton("Insert");

        // Create the dropdown menu
        JPopupMenu insertMenu = new JPopupMenu();
        JMenuItem inlineMathItem = new JMenuItem("Inline Math");
        JMenuItem displayMathItem = new JMenuItem("Display Math");
        JMenuItem linkItem = new JMenuItem("Link");
        JMenuItem citationItem = new JMenuItem("Citation");
        JMenuItem crossReferenceItem = new JMenuItem("Cross-Reference");
        JMenuItem figureItem = new JMenuItem("Figure");
        JMenuItem tableItem = new JMenuItem("Table");
        JMenuItem listItem = new JMenuItem("List");


        sectionMenu.add(sectionItem);
        sectionMenu.add(subsectionItem);
        sectionMenu.add(subsubsectionItem);
        sectionMenu.add(paragraphItem);
        sectionMenu.add(subparagraphItem);

        insertMenu.add(inlineMathItem);
        insertMenu.add(displayMathItem);
        insertMenu.add(linkItem);
        insertMenu.add(citationItem);
        insertMenu.add(crossReferenceItem);
        insertMenu.add(figureItem);
        insertMenu.add(tableItem);
        insertMenu.add(listItem);


        // Add action listener to the button to show the dropdown menu
        sectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sectionMenu.show(sectionButton, sectionButton.getWidth() / 2, sectionButton.getHeight() / 2);
            }
        });

        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertMenu.show(insertButton, insertButton.getWidth() / 2, insertButton.getHeight() / 2);
            }
        });

        sectionItem.addActionListener(this);
        subsectionItem.addActionListener(this);
        subsubsectionItem.addActionListener(this);
        paragraphItem.addActionListener(this);
        subparagraphItem.addActionListener(this);

        boldButton.addActionListener(this);
        italicButton.addActionListener(this);

        inlineMathItem.addActionListener(this);
        displayMathItem.addActionListener(this);
        linkItem.addActionListener(this);
        citationItem.addActionListener(this);
        crossReferenceItem.addActionListener(this);
        figureItem.addActionListener(this);
        tableItem.addActionListener(this);
        listItem.addActionListener(this);

        toggleSidebarButton.addActionListener(this);
        reCompileButton.addActionListener(this);
        settingsButton.addActionListener(this);
        undoButton.addActionListener(this);
        redoButton.addActionListener(this);
        saveButton.addActionListener(this);



        add(toggleSidebarButton);

        addSeparator();

        searchField = new JTextField(10);
        JLabel searchLabel = new JLabel("Search:");
        searchField.setMaximumSize(searchField.getPreferredSize());

        add(searchLabel);
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

        add(sectionButton);
        add(boldButton);
        add(italicButton);
        add(insertButton);

        addSeparator();
        add(Box.createGlue());
        add(settingsButton);
        add(reCompileButton);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        TextEditor textEditorPanel = mainFrame.getTextEditorPanel();
        Sidebar sidebarPanel = mainFrame.getSidebarPanel();

        switch (command) {
            case "Save":
                try {
                    mainFrame.getTextEditorPanel().saveFile();
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

                switch (FileManager.getCompilerKey()) {
                    case "pdflatex+bibtex+pdflatex":
                        mainFrame.getPdfViewerPanel().getController().openDocument(
                                PDFCompiler.compilepdflatexbibtexpdflatex(new File(FileManager.getMainTexFile())).getPath());
                        break;
                    case "pdflatex":
                        try {
                            mainFrame.getPdfViewerPanel().getController().openDocument(PDFCompiler.compilepdflatex(new File(FileManager.getMainTexFile())).getPath());
                        } catch (TransformerException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    case "bibtex":
                        try {
                            mainFrame.getPdfViewerPanel().getController().openDocument(PDFCompiler.compilebibtex(new File(FileManager.getMainTexFile())).getPath());
                        } catch (TransformerException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    case "xelatex":
                        try {
                            mainFrame.getPdfViewerPanel().getController().openDocument(PDFCompiler.compilexelatex(new File(FileManager.getMainTexFile())).getPath());
                        } catch (TransformerException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    case "lualatex":
                        try {
                            mainFrame.getPdfViewerPanel().getController().openDocument(PDFCompiler.compilelualatex(new File(FileManager.getMainTexFile())).getPath());
                        } catch (TransformerException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                }
                break;

            case "Settings":
                new Settings(mainFrame);

                break;
            case "Section":
                mainFrame.getTextEditorPanel().insertAtCursor(mainFrame.getTextEditorPanel().getTextArea(), "\\section{}");
                break;
            case "Subsection":
                mainFrame.getTextEditorPanel().insertAtCursor(mainFrame.getTextEditorPanel().getTextArea(), "\\subsection{}");
                break;
            case "Subsubsection":
                mainFrame.getTextEditorPanel().insertAtCursor(mainFrame.getTextEditorPanel().getTextArea(), "\\subsubsection{}");
                break;
            case "Paragraph":
                mainFrame.getTextEditorPanel().insertAtCursor(mainFrame.getTextEditorPanel().getTextArea(), "\\paragraph{}");
                break;
            case "Subparagraph":
                mainFrame.getTextEditorPanel().insertAtCursor(mainFrame.getTextEditorPanel().getTextArea(), "\\subparagraph{}");
                break;
            case "Bold":
                mainFrame.getTextEditorPanel().insertAtCursor(mainFrame.getTextEditorPanel().getTextArea(), "\\textbf{}");
                break;
            case "Italic":
                mainFrame.getTextEditorPanel().insertAtCursor(mainFrame.getTextEditorPanel().getTextArea(), "\\textit{}");
                break;
            case "Inline Math":
                mainFrame.getTextEditorPanel().insertAtCursor(mainFrame.getTextEditorPanel().getTextArea(), "\\(\\)");
                break;
            case "Display Math":
                mainFrame.getTextEditorPanel().insertAtCursor(mainFrame.getTextEditorPanel().getTextArea(), "\\[\\]");
                break;
            case "Link":
                mainFrame.getTextEditorPanel().insertAtCursor(mainFrame.getTextEditorPanel().getTextArea(), "\\href{}{}");
                break;
            case "Citation":
                mainFrame.getTextEditorPanel().insertAtCursor(mainFrame.getTextEditorPanel().getTextArea(), "\\cite{}");
                break;
            case "Cross-Reference":
                mainFrame.getTextEditorPanel().insertAtCursor(mainFrame.getTextEditorPanel().getTextArea(), "\\ref{}");
                break;
            case "Figure":
                mainFrame.getTextEditorPanel().insertAtCursor(mainFrame.getTextEditorPanel().getTextArea(), "\\begin{figure}\n\n" + "\\end{figure}\n\n");
                break;
            case "Table":
                mainFrame.getTextEditorPanel().insertAtCursor(mainFrame.getTextEditorPanel().getTextArea(), "\\begin{table}\n\n" + "\\end{table}\n\n");
                break;
            case "List":
                mainFrame.getTextEditorPanel().insertAtCursor(mainFrame.getTextEditorPanel().getTextArea(), "\\begin{itemize}\n\n" + "\\end{itemize}\n\n");
                break;
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
