package hr.mikrotron.remlog.repository;

import hr.mikrotron.remlog.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {

  @Query("select d from Device d where d.deviceId = :deviceId")
  Optional <Device> findDeviceByDeviceId(@Param("deviceId") String deviceId);
}