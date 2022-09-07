package hr.mikrotron.remlog.repository;

import hr.mikrotron.remlog.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
}