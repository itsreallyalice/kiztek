import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartWindow extends JFrame implements ActionListener {

    private JButton OpenButton;
    private JButton NewButton;
    private JButton TemplateButton;
    public StartWindow(){
        setTitle("KiZTeK: Open or create a file");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        OpenButton = new JButton("Open a .tex file");
        NewButton = new JButton("Create a new working directory");
        TemplateButton = new JButton("Open from a template");

        OpenButton.addActionListener(this);
        OpenButton.setActionCommand("Open");
        NewButton.addActionListener(this);
        NewButton.setActionCommand("New");
        TemplateButton.addActionListener(this);
        TemplateButton.setActionCommand("Template");

        add(OpenButton);
        add(NewButton);
        add(TemplateButton);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        String cmd = e.getActionCommand();
        if (cmd.equals("Open")){

        }
        if (cmd.equals("New")){

        }
        if (cmd.equals("Template")){

        }
    }
}
