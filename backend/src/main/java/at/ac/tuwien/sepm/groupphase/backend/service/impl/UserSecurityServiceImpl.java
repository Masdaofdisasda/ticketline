package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.repository.PasswordResetTokenRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.PasswordResetToken;
import at.ac.tuwien.sepm.groupphase.backend.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;

@Service
@Transactional
public class UserSecurityServiceImpl implements UserSecurityService {

  @Autowired
  private PasswordResetTokenRepository passwordTokenRepository;

  @Override
  public String validatePasswordResetToken(String token) {
    final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

    return !isTokenFound(passToken) ? "invalidToken"
      : isTokenExpired(passToken) ? "expired"
      : null;
  }

  private boolean isTokenFound(PasswordResetToken passToken) {
    return passToken != null;
  }

  private boolean isTokenExpired(PasswordResetToken passToken) {
    final Calendar cal = Calendar.getInstance();
    return passToken.getExpiryDate().before(cal.getTime());
  }
}

