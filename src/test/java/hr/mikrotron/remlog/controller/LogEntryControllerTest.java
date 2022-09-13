package hr.mikrotron.remlog.controller;

import hr.mikrotron.remlog.entity.Device;
import hr.mikrotron.remlog.entity.LogEntry;
import hr.mikrotron.remlog.repository.DeviceRepository;
import hr.mikrotron.remlog.repository.Log;
import hr.mikrotron.remlog.repository.LogEntryRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({LogEntryController.class, DeviceController.class})
class LogEntryControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  LogEntryRepository logEntryRepository;
  @MockBean
  DeviceRepository deviceRepository;

  // Test data
  String postBody = "{\"content\" : \"test log 1\"}";
  Device device = new Device(1L, "test", RandomStringUtils.random(16, "abcdef0123456789"));
  List<Log> logs = new ArrayList<>(Arrays.asList(
      new Log() {
        public LocalDateTime getDateTime(){return LocalDateTime.now().minusMinutes(2);}
        public String getContent(){return "log 1";}
      },
      new Log() {
        public LocalDateTime getDateTime(){return LocalDateTime.now().minusMinutes(1);}
        public String getContent(){return "log 2";}
      }
  ));

  @Test
  void getLogEntriesByDeviceId() throws Exception{
    Mockito.when(deviceRepository.findDeviceByDeviceId(anyString())).thenReturn(Optional.of(device));
    Mockito.when(logEntryRepository.findLogEntriesByDevice(any(Device.class))).thenReturn(logs);

    mockMvc.perform(MockMvcRequestBuilders
            .get("/logs/" + device.getDeviceId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$[1].content", is("log 2")));
  }

  @Test
  void getLogByDeviceByDeviceIdNotFound() throws Exception{
    Mockito.when(deviceRepository.findDeviceByDeviceId(anyString()))
        .thenThrow(DeviceController.DeviceNotFoundException.class);

    mockMvc.perform(MockMvcRequestBuilders
            .get("/logs/abc")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void pathNotFound() throws Exception{
    mockMvc.perform(MockMvcRequestBuilders
            .get("/log/abc/123/xyz")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void createLogEntry() throws Exception{
    Mockito.when(deviceRepository.findDeviceByDeviceId(anyString())).thenReturn(Optional.of(device));
    Mockito.when(logEntryRepository.save(any(LogEntry.class))).thenReturn(
        LogEntry.builder()
            .id(1L)
            .content("test log 1")
            .device(device)
            .build());

    mockMvc.perform(MockMvcRequestBuilders
            .post("/logs/device/" + device.getDeviceId())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(postBody))
        .andExpect(status().isOk());
  }

  @Test
  void createLogEntryDeviceNotFound() throws Exception{
    Mockito.when(deviceRepository.findDeviceByDeviceId(anyString()))
        .thenThrow(DeviceController.DeviceNotFoundException.class);
   mockMvc.perform(MockMvcRequestBuilders
            .post("/logs/device/" + device.getDeviceId())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(postBody))
        .andExpect(status().isNotFound());
  }
}