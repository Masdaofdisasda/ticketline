package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

  Optional<ApplicationUser> findUserByEmail(String email);

  @Query("SELECT u from ApplicationUser u LEFT JOIN FETCH u.news WHERE u.id = ?1")
  ApplicationUser findUserById(Long id);

  @Query("UPDATE ApplicationUser u SET u.failedAttempt = ?1 WHERE u.email = ?2")
  @Modifying
  @Transactional
  void updateFailedAttempts(int failAttempts, String email);

  @Query("SELECT u from ApplicationUser u WHERE u.accountNonLocked = false")
  List<ApplicationUser> getLockedUsers();

  List<ApplicationUser> findAll();
}
