package hr.mikrotron.remlog.controller;

public class DeviceNotFoundException extends RuntimeException {

  public DeviceNotFoundException(Long id) {
    super("Could not find device " + id + "\n");
  }
}
