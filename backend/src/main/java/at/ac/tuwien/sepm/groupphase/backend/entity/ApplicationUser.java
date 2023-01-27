package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.security.PasswordResetToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "APPLICATION_USER")
public class ApplicationUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "ACCOUNT_NON_LOCKED")
  private boolean accountNonLocked;

  @Column(name = "ADMIN")
  private boolean admin;

  @Size(max = 255)
  @NotNull
  @Column(name = "EMAIL", nullable = false)
  private String email;

  @Column(name = "FAILED_ATTEMPT")
  private Integer failedAttempt;

  @Size(max = 255)
  @NotNull
  @Column(name = "FIRST_NAME", nullable = false)
  private String firstName;

  @Size(max = 255)
  @NotNull
  @Column(name = "LAST_NAME", nullable = false)
  private String lastName;

  @Column(name = "LOCK_TIME")
  private LocalDateTime lockTime;

  @Size(max = 100)
  @NotNull
  @Column(name = "PASSWORD", nullable = false, length = 100)
  private String password;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PASSWORD_TOKEN_ID")
  private PasswordResetToken passwordToken;

  @OneToMany(mappedBy = "user")
  private Set<PasswordResetToken> passwordResetTokens = new LinkedHashSet<>();

  @OneToMany(mappedBy = "user")
  private Set<Booking> bookings = new LinkedHashSet<>();

  @Builder.Default
  @ManyToMany(mappedBy = "hasSeen", cascade = CascadeType.PERSIST)
  private Set<News> news = new LinkedHashSet<>();


  public void addBooking(Booking booking) {
    if (bookings.contains(booking)) {
      return;
    }
    bookings.add(booking);
    booking.setUser(this);
  }

  public void removeBooking(Booking booking) {
    if (!bookings.contains(booking)) {
      return;
    }
    bookings.remove(booking);
    booking.setUser(null);
  }

  public void addHasSeen(News news) {
    this.news.add(news);
  }
}