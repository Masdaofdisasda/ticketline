package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long> {
  public ApplicationUser findUserByEmail(String email);

  public ApplicationUser findUserById(Long id);

  @Query("UPDATE ApplicationUser u SET u.failedAttempt = ?1 WHERE u.email = ?2")
  @Modifying
  @Transactional
  public void updateFailedAttempts(int failAttempts, String email);

  @Query("SELECT u from ApplicationUser u WHERE u.accountNonLocked = false")
  public List<ApplicationUser> getLockedUsers();

  public List<ApplicationUser> findAll();
}
