import org.icepdf.ri.common.*;

import javax.swing.*;
import java.awt.*;

public class    PDFViewer extends JPanel {
    private SwingController controller;

    public PDFViewer() {
        setLayout(new BorderLayout());
        controller = new SwingController();
        CustomViewBuilder factory = new CustomViewBuilder(controller);
        JPanel viewerComponentPanel = factory.buildViewerPanel();
        ComponentKeyBinding.install(controller, viewerComponentPanel);
        controller.getDocumentViewController().setAnnotationCallback(new MyAnnotationCallback(controller.getDocumentViewController()));
        add(viewerComponentPanel, BorderLayout.CENTER);
    }

    public SwingController getController() {
        return controller;
    }
}
