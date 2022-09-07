package hr.mikrotron.remlog.entity;

import hr.mikrotron.remlog.entity.Device;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "log_entry", indexes = {
    @Index(name = "idx_logentry_device_id", columnList = "device_id")
})
public class LogEntry {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @NonNull
  private String content;

  @CreationTimestamp
  private LocalDateTime createdDateTime;

  @ManyToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "device_id", nullable = false)
  private Device device;
}