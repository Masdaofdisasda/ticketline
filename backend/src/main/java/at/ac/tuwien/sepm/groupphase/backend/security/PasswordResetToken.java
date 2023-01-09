package at.ac.tuwien.sepm.groupphase.backend.security;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {

  private static final int EXPIRATION = 60 * 24;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(nullable = false, unique = true)
  private String token;

  @OneToOne
  private ApplicationUser user;

  @Column(nullable = false)
  private Date expiryDate;

  public PasswordResetToken(final String token, final ApplicationUser user) {
    super();

    this.token = token;
    this.user = user;
    this.expiryDate = calculateExpiryDate(EXPIRATION);
  }

  private Date calculateExpiryDate(final int expiryTimeInMinutes) {
    final Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(new Date().getTime());
    cal.add(Calendar.MINUTE, expiryTimeInMinutes);
    return new Date(cal.getTime().getTime());
  }

  public void updateToken(final String token) {
    this.token = token;
    this.expiryDate = calculateExpiryDate(EXPIRATION);
  }

  public boolean isExpired() {
    return new Date().after(this.expiryDate);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((getExpiryDate() == null) ? 0 : getExpiryDate().hashCode());
    result = prime * result + ((getToken() == null) ? 0 : getToken().hashCode());
    result = prime * result + ((getUser() == null) ? 0 : getUser().hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PasswordResetToken other = (PasswordResetToken) obj;
    if (getExpiryDate() == null) {
      if (other.getExpiryDate() != null) {
        return false;
      }
    } else if (!getExpiryDate().equals(other.getExpiryDate())) {
      return false;
    }
    if (getToken() == null) {
      if (other.getToken() != null) {
        return false;
      }
    } else if (!getToken().equals(other.getToken())) {
      return false;
    }
    if (getUser() == null) {
      if (other.getUser() != null) {
        return false;
      }
    } else if (!getUser().equals(other.getUser())) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("Token [String=").append(token).append("]").append("[Expires").append(expiryDate).append("]");
    return builder.toString();
  }

}
