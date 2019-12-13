package handlers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import models.Account;
import models.Transactions;

import java.io.FileOutputStream;

/**
 * Class responsible for generating PDF files for invoices
 * @author Gheorghe Mironica
 */
public class GeneratePDF {

    private Document document;
    private String filename;
    private Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private String HOCHSCHULE_ADDRESS = "Prittwitzstraße 10";
    private String HOCHSCHULE_ZIP = "89075, Ulm";
    private String HOCHSCHULE_PHONE = "0731 50208";
    private String BOOKED_ITEM = "Booked Event:";
    private String ITEM1 = " Excursion";
    private String BILLINGTO = "Billing To:";

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
        item.add(new Paragraph( BOOKED_ITEM, boldFont));
        item.add(new Paragraph(bookeditem.getEvent().getCompany()+ITEM1));
        item.add(new Paragraph(bookeditem.getEvent().getShortDescription()));
        item.add(new Paragraph(String.valueOf(bookeditem.getEvent().getPrice())));
        item.add(new Paragraph(String.valueOf(bookeditem.getDate())));
        document.add(item);
    }

    private void addCustomer(Document document, Account customer) throws DocumentException {
        Paragraph client = new Paragraph();
        client.add(new Paragraph(BILLINGTO, boldFont));
        client.add(new Paragraph(customer.getFirstname()));
        client.add(new Paragraph(customer.getLastname()));
        client.add(new Paragraph(customer.getEmail()));
        addEmptyLine(client, 2);
        document.add(client);
    }

    private void addCompanyName(Document document, String title) throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(title, titleFont));

        addEmptyLine(preface, 2);
        preface.add(new Paragraph(HOCHSCHULE_ADDRESS));
        preface.add(new Paragraph(HOCHSCHULE_ZIP));
        preface.add(new Paragraph(HOCHSCHULE_PHONE));
        addEmptyLine(preface, 2);
        document.add(preface);
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public String getFilename(){
        return filename;
    }
}
