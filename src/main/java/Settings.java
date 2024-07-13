import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

class Settings extends JDialog {
    private JTextField texFileField;
    private JComboBox<String> compilerComboBox;

    public Settings(Frame owner) {
        super(owner, "Settings", true);
        setLayout(new GridBagLayout());
        setSize(400, 150);

        // Create and add components
        JLabel texFileLabel = new JLabel("main.tex file:");
        texFileField = new JTextField(15);

        texFileField.setText(FileManager.getMainTexFile());
        JButton browseButton = new JButton("Browse");

        JLabel compilerLabel = new JLabel("LaTeX Compiler:");
        String[] compilers = {"pdflatex+bibtex+pdflatex","pdflatex", "bibtex", "xelatex", "lualatex"};
        compilerComboBox = new JComboBox<>(compilers);

        JButton saveButton = new JButton("Save Changes");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add texFileLabel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(texFileLabel, gbc);

        // Add texFileField
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(texFileField, gbc);

        // Add browseButton
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(browseButton, gbc);

        // Add compilerLabel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(compilerLabel, gbc);

        // Add compilerComboBox
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(compilerComboBox, gbc);

        // Add saveButton
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(saveButton, gbc);

        // Action listener for the browse button
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(Settings.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    FileManager.setMainTexFile(selectedFile.getAbsolutePath());
                    texFileField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        // Action listener for the save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFile = texFileField.getText();
                String selectedCompiler = (String) compilerComboBox.getSelectedItem();
                FileManager.setCompiler(selectedCompiler);
                System.out.println("Selected main.tex file: " + selectedFile);
                System.out.println("Selected LaTeX compiler: " + selectedCompiler);
                dispose();
            }
        });

        // Display the settings dialog
        setVisible(true);
    }
}