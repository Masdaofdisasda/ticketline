package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;

public interface CreatePdfService {

  /**
   * Creates a stream of PDF files for all given tickets.
   *
   * @param outputStream The Stream the file is streamed to
   * @param booking      that was purchased
   * @throws DocumentException when a DocumentException occurs
   * @throws WriterException   when a WriterException occurs
   * @throws IOException       when an Exception with the file occurs
   */
  void createTicketPdf(@NotNull OutputStream outputStream, @NotNull Booking booking, String domain)
    throws DocumentException, WriterException, IOException;

  /**
   * Creates a stream of PDF files listing all purchased tickets and their prices.
   *
   * @param outputStream The Stream the file is streamed to
   * @param booking      that was purchased and is not cancelled
   * @throws DocumentException when a DocumentException occurs
   * @throws WriterException   when a WriterException occurs
   * @throws IOException       when an Exception with the file occurs
   */
  void createReceiptPdf(@NotNull OutputStream outputStream, @NotNull Booking booking, String domain)
    throws DocumentException, WriterException, IOException;

  /**
   * Creates a stream of PDF files listing all canceled tickets and their prices.
   *
   * @param outputStream The Stream the file is streamed to
   * @param booking      that was purchased/reserved and then cancelled
   * @throws DocumentException when a DocumentException occurs
   * @throws WriterException   when a WriterException occurs
   * @throws IOException       when an Exception with the file occurs
   */
  void createCancellationPdf(@NotNull OutputStream outputStream, @NotNull Booking booking, String domain)
    throws DocumentException, WriterException, IOException;
}
