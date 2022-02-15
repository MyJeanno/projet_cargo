package com.mola.cargo.model;

import com.lowagie.text.*;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HeaderFooterPageEvent extends PdfPageEventHelper {

    public void onStartPage(PdfWriter writer, Document document) {
        Rectangle rect = writer.getBoxSize("art");
        Font font = new Font(Font.HELVETICA, 8);
        ColumnText.showTextAligned(writer.getDirectContent(),
                                   Element.ALIGN_CENTER,
                                   new Phrase("Top Left"),
                                   rect.getLeft(),
                                   rect.getTop(), 0);
        Paragraph pdroite = new Paragraph("Mola et Fils, M. Biao\nCommerce général, Import & Export, cargo shipping (MOLA CARGO)," +
                "bureau de représentation commerciale et de conseils en matière de douane", font);
        ColumnText.showTextAligned(writer.getDirectContent(),
                                   Element.ALIGN_CENTER,
                                   new Paragraph(pdroite),
                                   rect.getRight(),
                                   rect.getTop(), 0);
    }
    public void onEndPage(PdfWriter writer,Document document) {
        Rectangle rect = writer.getBoxSize("art");
        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase("Bottom Left"), rect.getLeft(), rect.getBottom(), 0);
        ColumnText.showTextAligned(writer.getDirectContent(),
                                   Element.ALIGN_CENTER,
                                   new Phrase("Bottom Right"),
                                   rect.getRight(), rect.getBottom(), 0);
    }

    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        Rectangle rect = new Rectangle(30, 30, 550, 800);
        writer.setBoxSize("art", rect);
        HeaderFooterPageEvent event = new HeaderFooterPageEvent();
        writer.setPageEvent(event);
        document.open();
        document.add(new Paragraph("This is Page One"));
        //document.newPage();
        //document.add(new Paragraph("This is Page two"));
        document.close();
    }
}
