package hr.mikrotron.remlog.controller;

import hr.mikrotron.remlog.entity.Device;
import hr.mikrotron.remlog.repository.DeviceRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/devices")
public class DeviceController {
  private final DeviceRepository deviceRepository;

  @Value("${userid}")
  private String userId;

  DeviceController(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
  }

  @GetMapping
  public List<Device> getAllDevices() {
    return deviceRepository.findAll();
  }

  @GetMapping(value = "{id}")
  public Device getDeviceById(@PathVariable(value="id") Long id) {
    return deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));
  }

  @PostMapping(value = "user/{userid}")
  public Device createDevice(@PathVariable(value="userid") String userId,
                             @RequestBody Device device) {
    if (this.userId.equals(userId)) {
      // Generate unique device ID
      device.setDeviceId(RandomStringUtils.random(16, "abcdef0123456789"));
      return deviceRepository.save(device);
    }
    throw new NotAuthorizedException();
  }

  public static class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(Long id) {
      super("Could not find device " + id + "\n");
    }
  }
}
