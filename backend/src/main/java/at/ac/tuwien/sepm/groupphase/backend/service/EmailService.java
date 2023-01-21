package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
  SimpleMailMessage constructResetTokenEmail(
    String contextPath, String token, ApplicationUser user);
}
