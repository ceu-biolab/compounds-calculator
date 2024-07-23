package org.example.utils;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class PDFUtils {

    public PDFUtils(String name) throws Exception {
        String dest = "src/main/resources/" + name + ".pdf";
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.addNewPage();
        Document document = new Document(pdfDoc);

        Paragraph title = new Paragraph("Lipid Mass Spectrometry Data").setFont(PdfFontFactory.createFont(FontConstants.TIMES_ROMAN)).setFontSize(12).setTextAlignment(TextAlignment.CENTER);

        LocalDate localDate = LocalDate.now();
        Text text1 = new Text(localDate.toString()).setFontColor(Color.BLACK).setFont(PdfFontFactory.createFont(FontConstants.TIMES_ROMAN)).setFontSize(10).setTextAlignment(TextAlignment.CENTER);
        Paragraph paragraph1 = new Paragraph(text1);

        String string = "This file contains the possible lipid matches according to the data introduced in the application. For further information, please see" +
                "https://lipidmaps.com, where specific information on each lipid can be found";
        Text text2 = new Text(string).setFontColor(Color.BLACK).setFont(PdfFontFactory.createFont(FontConstants.TIMES_ROMAN)).setFontSize(10).setTextAlignment(TextAlignment.CENTER);
        Paragraph paragraph2 = new Paragraph(text2);

        String possibleMatches = "Possible Matches Found: 6";
        Text possibleMatchesText = new Text(possibleMatches).setFontColor(Color.BLACK).setFont(PdfFontFactory.createFont(FontConstants.TIMES_ROMAN)).setFontSize(10);
        Paragraph paragraphMatches = new Paragraph(possibleMatchesText);

        float[] pointColumnWidths = {150F, 150F, 150F, 150F, 150F, 150F};
        Table table = new Table(pointColumnWidths);
        addTable(table);

        document.add(title);
        document.add(paragraph1);
        document.add(paragraph2);
        document.add(new Paragraph("\n\n"));
        document.add(paragraphMatches);
        document.add(table);

        document.close();

        openPdf(dest);

    }

    public static void openPdf(String filePath) {
        if (Desktop.isDesktopSupported()) {
            try {
                File pdfFile = new File(filePath);
                if (pdfFile.exists()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    JOptionPane.showMessageDialog(null, "File does not exist.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid operation. ");
        }
    }

    public static void addTable(Table table) throws IOException {
        table.setFontSize(8).setFont(PdfFontFactory.createFont(FontConstants.TIMES_ROMAN));

        table.addCell(new Cell().add("Lipid ID").setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("Common Name").setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("Formula").setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("Precursor Ion").setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("NL Ion 1").setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("NL Ion 2").setBackgroundColor(Color.LIGHT_GRAY));

        table.addCell(new Cell().add("LMGL03019AAC"));
        table.addCell(new Cell().add("TG(10:0/10:0/12:0)"));
        table.addCell(new Cell().add("C35H66O6"));
        table.addCell(new Cell().add("600,4707"));
        table.addCell(new Cell().add("383,3159"));
        table.addCell(new Cell().add("411,3476"));

        table.addCell(new Cell().add("LMGL02134KKS"));
        table.addCell(new Cell().add("TG(10:0/12:0/12:0)"));
        table.addCell(new Cell().add("C34H65O5"));
        table.addCell(new Cell().add("122,3243"));
        table.addCell(new Cell().add("564,6576"));
        table.addCell(new Cell().add("347,8657"));

        table.addCell(new Cell().add("LMGL00239SKL"));
        table.addCell(new Cell().add("TG(14:0/14:0/12:0)"));
        table.addCell(new Cell().add("C33H63O3"));
        table.addCell(new Cell().add("456,3243"));
        table.addCell(new Cell().add("343,4323"));
        table.addCell(new Cell().add("654,2343"));

        table.addCell(new Cell().add("LMGL03019AAC"));
        table.addCell(new Cell().add("TG(10:0/10:0/12:0)"));
        table.addCell(new Cell().add("C35H66O6"));
        table.addCell(new Cell().add("600,4707"));
        table.addCell(new Cell().add("383,3159"));
        table.addCell(new Cell().add("411,3476"));

        table.addCell(new Cell().add("LMGL02134KKS"));
        table.addCell(new Cell().add("TG(10:0/12:0/12:0)"));
        table.addCell(new Cell().add("C34H65O5"));
        table.addCell(new Cell().add("122,3243"));
        table.addCell(new Cell().add("564,6576"));
        table.addCell(new Cell().add("347,8657"));

        table.addCell(new Cell().add("LMGL00239SKL"));
        table.addCell(new Cell().add("TG(14:0/14:0/12:0)"));
        table.addCell(new Cell().add("C33H63O3"));
        table.addCell(new Cell().add("456,3243"));
        table.addCell(new Cell().add("343,4323"));
        table.addCell(new Cell().add("654,2343"));
    }
}

