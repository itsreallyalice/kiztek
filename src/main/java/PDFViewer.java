//import org.apache.pdfbox.Loader;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.rendering.ImageType;
//import org.apache.pdfbox.rendering.PDFRenderer;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//public class PDFViewer {
//
//
//    public static BufferedImage renderPdf(File pdfFile) throws IOException {
//        try (PDDocument document = Loader.loadPDF(pdfFile)) {
//
//            PDFRenderer pdfRenderer = new PDFRenderer(document);
//            int totalWidth = 0;
//            int totalHeight = 0;
//            int numPages = document.getNumberOfPages();
//
//            for (int pageIndex = 0; pageIndex < numPages; pageIndex++) {
//                PDPage page = document.getPage(pageIndex);
//                //I THINK ITS THESE TWO LINES BELOW THAT KEEP FUCKING IT UP
//                totalWidth = Math.max(totalWidth, (int) page.getCropBox().getWidth());
//                totalHeight += (int) page.getCropBox().getHeight();
//            }
//
//            // Create a BufferedImage to hold the combined image
//            BufferedImage combinedImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
//
//            int yOffset = 0;
//
//            // Render each page onto the combined image
//            for (int pageIndex = 0; pageIndex < numPages; pageIndex++) {
//                BufferedImage pageImage = pdfRenderer.renderImageWithDPI(pageIndex, 300); // Render at 300 DPI
//
//                // Draw the page onto the combined image
//                combinedImage.createGraphics().drawImage(pageImage, 0, yOffset, null);
//
//                // Update the Y offset for the next page
//                yOffset += pageImage.getHeight();
//            }
//
//
//            File outputfile = new File("testimage.jpg");
//            ImageIO.write(combinedImage, "jpg", outputfile);
//            return combinedImage;
//        }
//    }
//    private static BufferedImage combineImagesHorizontally(BufferedImage image1, BufferedImage image2) {
//        int combinedWidth = image1.getWidth() + image2.getWidth();
//        int combinedHeight = Math.max(image1.getHeight(), image2.getHeight());
//
//        BufferedImage combinedImage = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);
//        Graphics g = combinedImage.getGraphics();
//
//        g.drawImage(image1, 0, 0, null);
//        g.drawImage(image2, image1.getWidth(), 0, null);
//
//        return combinedImage;
//    }
//}
