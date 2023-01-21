package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface CreatePdfService {
  /**
   * This function creates a PDF file and streams it to the given OutputStream.
   *
   * @param outputStream The Stream the file is streamed to
   * @param ticketList   A list of tickets that will be printed
   * @throws DocumentException when a DocumentException occurs
   * @throws WriterException   when a WriterException occurs
   * @throws IOException       when an Exception with the file occurs
   */
  void createTicketPdf(@NotNull OutputStream outputStream, @NotNull List<Ticket> ticketList, String domain) throws DocumentException, WriterException, IOException;
}
