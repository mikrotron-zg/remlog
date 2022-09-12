package hr.mikrotron.remlog.repository;

import hr.mikrotron.remlog.entity.Device;
import hr.mikrotron.remlog.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {

  @Query("select le.createdDateTime as dateTime, le.content as content from LogEntry le " +
        "where le.device = :device order by le.id desc")
  List<Log> findLogEntriesByDevice(@Param("device")Device device);
}