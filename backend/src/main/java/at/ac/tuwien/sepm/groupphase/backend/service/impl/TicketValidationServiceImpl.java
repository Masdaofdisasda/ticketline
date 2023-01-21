package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Booking;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookingRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketValidationService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Optional;

@Service
public class TicketValidationServiceImpl implements TicketValidationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String SEPERATOR = "-";
  private final TicketRepository ticketRepository;
  private final SecretKeySpec keySpec;
  private final BookingRepository bookingRepository;

  @SneakyThrows
  public TicketValidationServiceImpl(TicketRepository ticketRepository, BookingRepository bookingRepository) {
    this.ticketRepository = ticketRepository;
    this.bookingRepository = bookingRepository;
    byte[] salt = new byte[] {2, 7, 5, 1, 2, 6, 3, 1, 4, 1, 0, 3, 4, 2, 4, 5};
    KeySpec spec = new PBEKeySpec(this.getKey().toCharArray(), salt, 65536, 256);
    byte[] key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
    keySpec = new SecretKeySpec(key, "AES");
  }

  @Override
  public byte[] generateTicketValidationHash(Ticket ticket) {
    try {
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.ENCRYPT_MODE, keySpec);
      return cipher.doFinal((ticket.getId().toString() + SEPERATOR + ticket.getBooking().getId()).getBytes());
    } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
      throw new RuntimeException("A encryption error occured trying to generate a validation Hash for a ticket", e);
    }
  }

  @Override
  public TicketValidationResult validateTicket(byte[] validationHash) {
    String decryptedString;
    try {
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.DECRYPT_MODE, keySpec);
      byte[] decrypted = cipher.doFinal(validationHash);
      decryptedString = new String(decrypted);
    } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      return TicketValidationResult.DECRYPTION_FAILED;
    }
    String[] split = decryptedString.split(SEPERATOR);
    String ticketId = split[0];
    String bookingId = split[1];

    Optional<Ticket> ticket = ticketRepository.findById(Long.valueOf(ticketId));
    if (ticket.isEmpty()) {
      return TicketValidationResult.TICKET_NOT_FOUND;
    }

    if (ticket.get().getUsed()) {
      return TicketValidationResult.TICKET_ALREADY_USED;
    }

    Optional<Booking> booking = bookingRepository.findById(Long.valueOf(bookingId));

    if (booking.isEmpty()) {
      return TicketValidationResult.NO_BOOKING_FOUND;
    }

    if (booking.get().getTickets() == null || booking.get().getTickets().size() == 0) {
      return TicketValidationResult.BOOKING_CANCELED;
    }

    return TicketValidationResult.VALID;
  }

  private String getKey() {
    try {
      return Files.readString(ResourceUtils.getFile("classpath:" + "crypto/validation.key").toPath());
    } catch (IOException e) {
      LOGGER.error("Error when opening file " + "crypto/validation.key", e);
      return "";
    }
  }
}
