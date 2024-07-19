package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import java.io.File;
import java.io.IOException;

public class PdfStamp {

    private float TRANSLATION_INTO_CM = 28.3465f;

    public void showFiles(PropertiesPDF propertiesPDF) {
        File dir = new File(propertiesPDF.getSourceFolder());
        File[] files = dir.listFiles();
        for (File file : files) {
            checkPDFPage(file.getAbsolutePath(), propertiesPDF);
        }
    }

    public void pdfStamper(PDPage page, PDDocument document, PropertiesPDF propertiesPDF) {
        float bottomMarginPoints = propertiesPDF.getYOffset() * TRANSLATION_INTO_CM;
        float rightMarginPoints = propertiesPDF.getXOffset() * TRANSLATION_INTO_CM;
        float stampWidthPoints = propertiesPDF.getStampWidth() * TRANSLATION_INTO_CM;
        float stampHeightPoints = propertiesPDF.getStampHeight() * TRANSLATION_INTO_CM;

        PDImageXObject stampImage = null;
        try {
            stampImage = PDImageXObject.createFromFile(propertiesPDF.getImagePath(), document);
        } catch (IOException e) {
            System.out.println("Error reading the stamp image: " + e.getMessage());
        }

        PDRectangle mediaBox = page.getMediaBox();
        int rotation = page.getRotation();
        float pageWidth = mediaBox.getWidth();
        float pageHeight = mediaBox.getHeight();

        float x, y;
        PDPageContentStream contentStream = null;
        try {
            contentStream = new PDPageContentStream(document, page, true, true, true);
        } catch (IOException e) {
            System.out.println("Error creating a stream to write to the document: " + e.getMessage());
        }
        x = pageWidth - rightMarginPoints - stampWidthPoints;
        y = bottomMarginPoints;

        if (rotation == 90 || rotation == 270) {
            try {
                if (rotation == 90) {
                    contentStream.transform(new Matrix(0, 1, -1, 0, pageHeight, 0));

                } else if (rotation == 270) {
                    contentStream.transform(new Matrix(0, -1, 1, 0, 0, pageWidth));
                }
            } catch (IOException e) {
                System.out.println("Stamp rotation error: " + e.getMessage());
            }
        }

        try {
            contentStream.drawImage(stampImage, x, y, stampWidthPoints, stampHeightPoints);
            contentStream.close();
        } catch (IOException e) {
            System.out.println("Error adding a stamp to a document: " + e.getMessage());
        }

    }


    public void checkPDFPage(String pdfFilePath, PropertiesPDF propertiesPDF) {

        File pdfFile = new File(pdfFilePath);
        if (!pdfFile.exists()) {
            System.out.println("File not found: " + pdfFilePath);
            return;
        }

        try (PDDocument document = PDDocument.load(pdfFile)) {
            int pageCount = document.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                PDPage page = document.getPage(i);
                pdfStamper(page,document,propertiesPDF);
            }

            String savePath;
            if (propertiesPDF.getDestinationFolder().equals(pdfFile.getParent())) {
                savePath = propertiesPDF.getSourceFolder();
            } else {
                savePath = propertiesPDF.getDestinationFolder() + "/" + pdfFile.getName();
            }

            document.save(new File(savePath));
        } catch (IOException e) {
            System.out.println("An error occurred while processing the PDF file: " + e.getMessage());
        }
    }
}
