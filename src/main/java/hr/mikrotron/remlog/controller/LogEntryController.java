package hr.mikrotron.remlog.controller;

import hr.mikrotron.remlog.entity.LogEntry;
import hr.mikrotron.remlog.repository.DeviceRepository;
import hr.mikrotron.remlog.repository.LogEntryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/logs")
public class LogEntryController {
  private final LogEntryRepository logEntryRepository;
  private final DeviceRepository deviceRepository;

  LogEntryController(LogEntryRepository logEntryRepository,
                     DeviceRepository deviceRepository) {
    this.logEntryRepository = logEntryRepository;
    this.deviceRepository = deviceRepository;
  }

  @GetMapping(value = "{deviceId}")
  public List<LogEntry> getLogEntriesByDeviceId(@PathVariable(value = "deviceId") String deviceId){
    return logEntryRepository.findLogEntriesByDevice(
        deviceRepository.findDeviceByDeviceId(deviceId)
            .orElseThrow(NotAuthorizedException::new));
  }

  @PostMapping(value = "device/{deviceId}")
  public String createLogEntry(@PathVariable(value="deviceId") String deviceId,
                             @RequestBody LogEntry logEntry) {
    logEntry.setDevice(deviceRepository.findDeviceByDeviceId(deviceId)
        .orElseThrow(NotAuthorizedException::new));
    logEntryRepository.save(logEntry);
    return "success";
  }
}
