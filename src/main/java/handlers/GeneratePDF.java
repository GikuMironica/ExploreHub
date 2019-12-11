package handlers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import models.Account;
import models.Transactions;

import java.io.FileOutputStream;


public class GeneratePDF {

    private Document document;
    private String filename;
    private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

    public GeneratePDF(Account customer, Transactions item) throws Exception {
        this.filename = "Invoice_"+item.getUser().getLastname()+"_"+item.getId()+".pdf";

        document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        addCompanyName(document, item.getEvent().getCompany());
        addCustomer(document, customer);
        addItem(document, item);
        document.close();
    }

    private void addItem(Document document, Transactions bookeditem) throws DocumentException {
        Paragraph item = new Paragraph();
        item.add(new Paragraph("Booked Event:", boldFont));
        item.add(new Paragraph(bookeditem.getEvent().getCompany()+" Excursion"));
        item.add(new Paragraph(bookeditem.getEvent().getShortDescription()));
        item.add(new Paragraph(String.valueOf(bookeditem.getEvent().getPrice())));
        item.add(new Paragraph(String.valueOf(bookeditem.getDate())));
        document.add(item);
    }

    private void addCustomer(Document document, Account customer) throws DocumentException {
        Paragraph client = new Paragraph();
        client.add(new Paragraph("Billing To:", boldFont));
        client.add(new Paragraph(customer.getFirstname()));
        client.add(new Paragraph(customer.getLastname()));
        client.add(new Paragraph(customer.getEmail()));
        addEmptyLine(client, 2);
        document.add(client);
    }

    private static void addCompanyName(Document document, String title) throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(title, titleFont));

        addEmptyLine(preface, 2);
        preface.add(new Paragraph("Prittwitzstraße 10"));
        preface.add(new Paragraph("89075, Ulm"));
        preface.add(new Paragraph("0731 50208"));
        addEmptyLine(preface, 2);
        document.add(preface);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public String getFilename(){
        return filename;
    }
}
