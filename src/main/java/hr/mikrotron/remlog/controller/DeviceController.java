package hr.mikrotron.remlog.controller;

import hr.mikrotron.remlog.entity.Device;
import hr.mikrotron.remlog.repository.DeviceRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/devices")
public class DeviceController {
  private final DeviceRepository deviceRepository;

  DeviceController(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
  }

  @GetMapping
  public List<Device> getAllRecords() {
    return deviceRepository.findAll();
  }

  @GetMapping(value = "{id}")
  public Device getDeviceById(@PathVariable(value="id") Long id) {
    return deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));
  }

  @PostMapping
  public Device createDevice(@RequestBody Device device) {
    // TODO: generate unique device ID
    return deviceRepository.save(device);
  }

  public static class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(Long id) {
      super("Could not find device " + id + "\n");
    }
  }
}
