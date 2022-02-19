package com.mola.cargo.model;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.mola.cargo.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

public class FactureColisPDFExporter {

    private List<ColisMaritime> listeColis;

    public FactureColisPDFExporter(List<ColisMaritime> listeColis) {
        this.listeColis = listeColis;
    }
/*
    private double calculerTotalPrix(){
        double prix = 0;
        for (Colis c : listeColis){
            prix = prix + c.getPrixColis();
        }
        return prix;
    }

    public void onStartPage(PdfWriter writer,Document document) {
        Rectangle rect = writer.getBoxSize("art");
        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase("Top Left"), rect.getLeft(), rect.getTop(), 0);
        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase("Top Right"), rect.getRight(), rect.getTop(), 0);
    }

    public void onEndPage(PdfWriter writer,Document document) {
        Rectangle rect = writer.getBoxSize("art");
        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase("Bottom Left"), rect.getLeft(), rect.getBottom(), 0);
        ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase("Bottom Right"), rect.getRight(), rect.getBottom(), 0);
    }

    private void writeTableTotalPrix(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.YELLOW);
        cell.setPadding(5);
        cell.setUseBorderPadding(true);
        cell.setBorder(1);
        Font font = new Font(Font.HELVETICA);
        font.setColor(Color.BLACK);
        cell.setPhrase(new Phrase("Total à payer en FCFA", font));
        table.addCell(cell);
        cell.setPaddingRight(0);
        Phrase phrase = new Phrase(String.valueOf(String.format("% ,.2f", calculerTotalPrix()))+" XOF", font);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPhrase(phrase);
        table.addCell(cell);
    }

    private void writeTableTotalPrixEuro(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.lightGray);
        cell.setPadding(5);
        cell.setUseBorderPadding(true);
        cell.setBorder(1);
        //cell.set
        Font font = new Font(Font.HELVETICA);
        font.setColor(Color.BLACK);
        cell.setPhrase(new Phrase("Total à payer en Euro", font));
        table.addCell(cell);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPhrase(new Phrase(String.valueOf(String.format("% ,.2f", calculerTotalPrix()/660.45))+" £", font));
        table.addCell(cell);
    }

    private void writeTableMessage(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setPaddingLeft(40);
        //cell.setUseBorderPadding(true);
        cell.setBorder(1);
        //cell.set
        Font font = new Font(Font.HELVETICA);
        font.setColor(Color.BLACK);
        Phrase phrase = new Phrase("Agent commercial\n\n  Menad BIAO", font);
        cell.setPhrase(phrase);
        table.addCell(cell);
        cell.setPaddingRight(40);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPhrase(new Phrase("Client\n\n  TOTO", font));
        table.addCell(cell);
    }

    private void writeTableAvis(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setPaddingLeft(5);
        cell.setUseBorderPadding(true);
        cell.setBorder(0);
        Font font = new Font(Font.HELVETICA, Font.BOLDITALIC, Font.ITALIC);
        font.setColor(Color.BLACK);
        font.setSize(12);
        Phrase phrase = new Phrase("Facture à regler avant le départ du colis", font);
        cell.setPhrase(phrase);
        table.addCell(cell);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPhrase(new Phrase("Merci pour votre confiance !", font));
        table.addCell(cell);
    }

    private void writeTableFooter(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setPaddingLeft(5);
        cell.setUseBorderPadding(true);
        cell.setBorder(1);
        Font font = new Font(Font.HELVETICA, Font.BOLDITALIC, Font.ITALIC);
        font.setColor(Color.BLACK);
        font.setSize(8);
        Phrase phrase = new Phrase("Mola et Fils, M. Biao\n" +
                                         "Quartier Tokoin Wuiti, Av. Akei, Lomé Togo", font);
        cell.setPhrase(phrase);
        table.addCell(cell);

        cell.setPhrase(new Phrase("RCCM: TG-LFW-01-2021-A10-06917\n" +
                                        "CNSS: 148728\n" +
                                        "NIF: 1001778160", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("info@molaetfils.com\n" +
                                        "www.moletfils.com\n" +
                                        "Tel.: +228 70 11 12 19 / +228 79 73 66 21\n" +
                                        "+49 1575 872 55 80", font));
        table.addCell(cell);
    }

    private void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setPadding(5);
        cell.setUseBorderPadding(true);
        cell.setBorder(1);
        Font font = new Font(Font.HELVETICA);
        font.setColor(Color.BLACK);
        cell.setPhrase(new Phrase("Type de carton", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Contenu", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Poids", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Prix en FCFA", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table){
        for (ColisMaritime c: listeColis){
            table.addCell(c.getCarton().getLibelleCarton());
            table.addCell(c.getLibelleColis());
            table.addCell(String.valueOf(c.getPoids()));
            table.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(String.valueOf(String.format("% ,.2f",c.getPrixColis())));
        }

    }

    public void export(HttpServletResponse response,
                       Date denvoi,
                       String numf,
                       String objet,
                       String envoyeur,
                       String receveur,
                       String etat,
                       String pays,
                       String ville,
                       String contact) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font font = new Font(Font.HELVETICA, 8, Font.BOLD);
        Paragraph p = new Paragraph("Mola et Fils, M. Biao\n" +
                "Commerce général, Import & Export, cargo shipping (MOLA CARGO),\n" +
                "bureau de représentation commerciale et de conseils en matière de douane\n", font);
       p.setAlignment(Element.ALIGN_RIGHT);
       document.add(p);

       Image image = Image.getInstance("head.png");
       image.setAlignment(Element.ALIGN_LEFT);
       image.setAbsolutePosition(32, 750);
       document.add(image);

       Paragraph p2 = new Paragraph("Adresse : Quartier Tokoin Wuiti, Av. Akei\n" +
               "Lomé-Togo\n" +
               "Email : info@molaetfils.com / www.molaetfils.com\n" +
               "Tél : +228 70 11 1219 / +228 79 73 66 21 / +49 1575 872 55 80\n", font);
       p2.setAlignment(Element.ALIGN_LEFT);
       p2.setSpacingBefore(20);
       document.add(p2);

       Font font1 = new Font(Font.HELVETICA, Font.BOLD, Font.UNDERLINE);
       font1.setSize(16);
       Paragraph pfacture = new Paragraph("FACTURE", font1);
       pfacture.setAlignment(Element.ALIGN_CENTER);
       pfacture.setSpacingBefore(10);
       document.add(pfacture);

        Font fontinfo = new Font(Font.HELVETICA);
        fontinfo.setSize(11);
        Paragraph pinfo = new Paragraph(
                         "Date :                                "+denvoi+"\n"+
                               "Numéro de la facture :      "+numf+"\n"+
                               "Objet :                               Envoi marchandise Togo - "+objet+"\n"+
                               "Expéditeur :                      "+envoyeur+"\n"+
                               "Destinataire :                    "+receveur+"\n"+
                               "Etat :                                 "+etat+"\n"+
                               "Pays :                                "+pays+"\n"+
                                 "Ville :                                 "+ville+"\n"+
                               "Contact :                           "+contact+"\n", fontinfo);
        pinfo.setAlignment(Element.ALIGN_LEFT);
        pinfo.setSpacingBefore(10);
        document.add(pinfo);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        writeTableHeader(table);
        writeTableData(table);
        document.add(table);

        PdfPTable tableprix = new PdfPTable(2);
        tableprix.setWidthPercentage(100);
        tableprix.setSpacingBefore(20);
        writeTableTotalPrix(tableprix);
        document.add(tableprix);

        PdfPTable tableprixeuro = new PdfPTable(2);
        tableprixeuro.setWidthPercentage(100);
        tableprixeuro.setSpacingBefore(0);
        writeTableTotalPrixEuro(tableprixeuro);
        document.add(tableprixeuro);

        PdfPTable tablemessage = new PdfPTable(2);
        tablemessage.setWidthPercentage(100);
        tablemessage.setSpacingBefore(50);
        writeTableMessage(tablemessage);
        document.add(tablemessage);

        PdfPTable tableavis = new PdfPTable(2);
        tableavis.setWidthPercentage(100);
        tableavis.setSpacingBefore(100);
        writeTableAvis(tableavis);
        document.add(tableavis);

        PdfPTable tablefooter = new PdfPTable(3);
        tablefooter.setWidthPercentage(100);
        tablefooter.setSpacingBefore(50);
        tablefooter.setHorizontalAlignment(Element.ALIGN_BASELINE);
        writeTableFooter(tablefooter);
        document.add(tablefooter);

        document.close();
    }*/
}
