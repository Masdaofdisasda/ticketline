package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
@Table(name = "NEWS")
public class News {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Size(max = 100)
  @Column(name = "FILE_NAME", length = 100)
  private String fileName;

  @NotNull
  @Column(name = "PUBLISHED_AT", nullable = false)
  private LocalDateTime publishedAt;

  @Size(max = 500)
  @NotNull
  @Column(name = "SUMMARY", nullable = false, length = 500)
  private String summary;

  @Size(max = 10000)
  @NotNull
  @Column(name = "TEXT", nullable = false, length = 10000)
  private String text;

  @Size(max = 100)
  @NotNull
  @Column(name = "TITLE", nullable = false, length = 100)
  private String title;

  @Builder.Default
  @ManyToMany(mappedBy = "news", cascade = CascadeType.PERSIST)
  private Set<ApplicationUser> hasSeen = new LinkedHashSet<>();

  public void add(ApplicationUser user) {
    this.hasSeen.add(user);
  }
}