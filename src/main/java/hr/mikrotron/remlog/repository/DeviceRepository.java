package hr.mikrotron.remlog.repository;

import hr.mikrotron.remlog.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}