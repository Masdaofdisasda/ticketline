package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.service.CreatePdfService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CreatePdfServiceImpl implements CreatePdfService {

  @Override
  public void createTicketPdf(OutputStream outputStream, Booking booking, String domain)
    throws IOException, DocumentException, WriterException {
    // Create the PDF document
    Set<Ticket> ticketList = booking.getTickets();
    Document document = new Document();
    PdfWriter.getInstance(document, outputStream);
    document.open();

    for (final Ticket ticket : ticketList) {
      BaseFont baseFont = FontFactory.getFont(FontFactory.HELVETICA).getBaseFont();
      Paragraph titleElement = new Paragraph("Ticket for " + ticket.getPerformance().getEvent().getName(), new Font(baseFont, 36f));
      titleElement.setSpacingAfter(40f);
      document.add(titleElement);

      Font bold = new Font(baseFont, 16, Font.BOLD);

      Paragraph contentElement = new Paragraph("Thank you for using Ticketline. We hope you will have fun at ", new Font(baseFont, 16f));
      contentElement.setFont(new Font(baseFont, 16));
      contentElement.add(new Phrase(ticket.getPerformance().getEvent().getName(), bold));
      contentElement.add(".\nThis Ticket is valid for the performance by ");
      contentElement.add(new Phrase(ticket.getPerformance().getArtists().stream().map(Artist::getName).collect(Collectors.joining(", ")), bold));
      contentElement.add(" on ");
      contentElement.add(new Phrase(String.format("%s - %s",
        ticket.getPerformance().getStartDate().format(DateTimeFormatter.ofPattern("E dd. MMM yyyy H:m:s")),
        ticket.getPerformance().getEndDate().format(DateTimeFormatter.ofPattern("E dd. MMM yyyy H:m:s"))), bold));
      contentElement.add(".\nYour seat is ");
      contentElement.add(new Phrase(ticket.getSeat().getColNumber() + ":" + ticket.getSeat().getRowNumber(), bold));
      contentElement.add(" in ");
      contentElement.add(new Phrase(ticket.getPerformance().getRoom().getName()));
      contentElement.add(".");
      Paragraph locationElement =
        new Paragraph("Location of Performance: " + ticket.getPerformance().getRoom().getVenue().getAddress(), new Font(baseFont, 16f));
      contentElement.add(locationElement);
      contentElement.setSpacingAfter(20f);
      document.add(contentElement);

      // Create the QR code
      QRCodeWriter qrCodeWriter = new QRCodeWriter();
      String qrContent = String.format("%s#/tickets/validate?hash=%s",
        domain, Base64.getUrlEncoder().encodeToString(ticket.getValidationHash()));
      BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);
      BufferedImage qrCodeImage = bitMatrixToBufferedImage(bitMatrix);
      Image qrCodePdfImage = Image.getInstance(qrCodeImage, null);
      qrCodePdfImage.setAlignment(Element.ALIGN_CENTER);
      qrCodePdfImage.setPaddingTop(280f);
      // Add the QR code to the PDF document
      document.add(qrCodePdfImage);

      document.newPage();
    }

    document.close();
  }

  @Override
  public void createReceiptPdf(OutputStream outputStream, Booking booking, String domain) throws DocumentException {
    // Create the PDF document
    Document document = new Document();
    PdfWriter.getInstance(document, outputStream);
    document.open();

    BaseFont baseFont = FontFactory.getFont(FontFactory.HELVETICA).getBaseFont();
    Paragraph titleElement = new Paragraph("Ticketline GmbH", new Font(baseFont, 36f));
    titleElement.setFont(new Font(baseFont, 24f));
    titleElement.add(new Phrase("\n\nTicketplatz 23"));
    titleElement.add(new Phrase("\n\n1010 Wien"));
    titleElement.setFont(new Font(baseFont, 11));
    titleElement.add(new Phrase("\n\nVAT: AT12345678"));
    titleElement.setSpacingAfter(40f);

    document.add(titleElement);

    Paragraph header = new Paragraph("Receipt for booking nr. " + booking.getId() + "                                      "
      + booking.getCreatedDate().format(DateTimeFormatter.ofPattern("dd. MMM yyyy H:m:s")), new Font(baseFont, 16f));
    header.setSpacingAfter(12f);

    document.add(header);

    Set<Ticket> tickets = booking.getBookingType() == BookingType.CANCELLATION
      ? booking.getCanceledTickets()
      : booking.getTickets();
    for (Ticket ticket : tickets) {
      Performance performance = ticket.getPerformance();
      String eventName = performance.getEvent().getName();
      if (eventName.length() > 20) {
        eventName = eventName.substring(0, 20);
      }
      Paragraph contentElement = new Paragraph("Ticket for: '" + eventName + "', "
        + "Seat: " + ticket.getSeat().getColNumber() + ":" + ticket.getSeat().getRowNumber() + "                      "
        + ticket.getPrice() + " EUR", new Font(baseFont, 16f));
      Paragraph locationElement = new Paragraph("Location of Performance: " + performance.getRoom().getVenue().getAddress(), new Font(baseFont, 16f));
      document.add(contentElement);
      document.add(locationElement);
    }

    Paragraph total = new Paragraph("-----------------------------------------------------------------", new Font(baseFont, 24f));
    total.add(new Phrase("\nTotal (incl. tax): " + booking.calculateTotal() + " EUR"));
    total.setSpacingBefore(6f);
    total.setSpacingAfter(12f);
    document.add(total);

    Paragraph paymentInfo = new Paragraph("Payed with credit card", new Font(baseFont, 11f));
    document.add(paymentInfo);

    document.close();
  }

  @Override
  public void createCancellationPdf(OutputStream outputStream, Booking booking, String domain) throws DocumentException {
    // Create the PDF document
    Document document = new Document();
    PdfWriter.getInstance(document, outputStream);
    document.open();

    BaseFont baseFont = FontFactory.getFont(FontFactory.HELVETICA).getBaseFont();
    Paragraph titleElement = new Paragraph("Ticketline GmbH", new Font(baseFont, 36f));
    titleElement.setFont(new Font(baseFont, 24f));
    titleElement.add(new Phrase("\n\nTicketplatz 23"));
    titleElement.add(new Phrase("\n\n1010 Wien"));
    titleElement.setFont(new Font(baseFont, 11));
    titleElement.add(new Phrase("\n\nVAT: AT12345678"));
    titleElement.setSpacingAfter(40f);
    document.add(titleElement);

    Paragraph header = new Paragraph("Cancellation for booking nr. " + booking.getId() + "                               "
      + booking.getCreatedDate().format(DateTimeFormatter.ofPattern("dd. MMM yyyy H:m:s")), new Font(baseFont, 16f));
    header.setSpacingAfter(12f);
    document.add(header);

    Set<Ticket> tickets = booking.getBookingType() == BookingType.CANCELLATION
      ? booking.getCanceledTickets()
      : booking.getTickets();
    for (final Ticket ticket : tickets) {

      String eventName = ticket.getPerformance().getEvent().getName();
      if (eventName.length() > 20) {
        eventName = eventName.substring(0, 20);
      }
      Paragraph contentElement = new Paragraph("Ticket for: '" + eventName + "', "
        + "Seat: " + ticket.getSeat().getColNumber() + ":" + ticket.getSeat().getRowNumber() + "                      "
        + ticket.getPrice() + " EUR", new Font(baseFont, 16f));
      document.add(contentElement);
    }

    Paragraph total = new Paragraph("-----------------------------------------------------------------", new Font(baseFont, 24f));
    total.add(new Phrase("\nRefund: " + booking.calculateCancelTotal() + " EUR"));
    total.setSpacingBefore(6f);
    total.setSpacingAfter(12f);
    document.add(total);

    Paragraph paymentInfo = new Paragraph("The total price was refundend to your credit card", new Font(baseFont, 11f));
    document.add(paymentInfo);

    document.close();
  }

  private BufferedImage bitMatrixToBufferedImage(final BitMatrix bitMatrix) {
    // Create a BufferedImage with the same dimensions as the BitMatrix
    final int width = bitMatrix.getWidth();
    final int height = bitMatrix.getHeight();
    BufferedImage qrCodeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    // Iterate through the pixels of the BitMatrix and set the corresponding pixels in the BufferedImage
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        qrCodeImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
      }
    }
    return qrCodeImage;
  }

}
