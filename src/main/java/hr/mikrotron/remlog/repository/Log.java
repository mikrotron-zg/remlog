package hr.mikrotron.remlog.repository;

import java.time.LocalDateTime;

public interface Log {
  LocalDateTime getDateTime();
  String getContent();
}
