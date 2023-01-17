package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.security.PasswordResetToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, length = 100)
  private String password;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(name = "account_non_locked")
  private boolean accountNonLocked;

  @Column(name = "failed_attempt")
  private int failedAttempt;

  @Column(name = "lock_time")
  private Date lockTime;

  private Boolean admin;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(FetchMode.JOIN)
  @Builder.Default
  private List<Booking> bookings = new ArrayList<>();

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "password_token_id", referencedColumnName = "id")
  private PasswordResetToken passwordResetToken;

  public ApplicationUser(String email, String password, String firstName, String lastName, Boolean admin) {
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.admin = admin;
  }

  public void addBooking(Booking booking) {
    if (bookings.contains(booking)) {
      return;
    }
    bookings.add(booking);
    booking.setUser(this);
  }
}
