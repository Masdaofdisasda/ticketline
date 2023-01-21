package at.ac.tuwien.sepm.groupphase.backend.service;

public interface UserSecurityService {
  String validatePasswordResetToken(String token);

}
