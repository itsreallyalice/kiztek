import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolbarGUI extends JPanel {
    public ToolbarGUI() {


        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton newButton = new JButton("New");
        JButton uploadButton = new JButton("Upload");
        JButton exportButton = new JButton("Export");
        JButton directoryButton = new JButton("Directory");

        toolbarPanel.add(newButton);
        toolbarPanel.add(uploadButton);
        toolbarPanel.add(exportButton);
        toolbarPanel.add(directoryButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));

        JTextField textField1 = new JTextField();
        JTextField textField2 = new JTextField();

        mainPanel.add(toolbarPanel);
        mainPanel.add(textField1);
        mainPanel.add(textField2);

        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle New button action
            }
        });

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Upload button action
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Export button action
            }
        });

        directoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Directory button action
            }
        });

    }
}
