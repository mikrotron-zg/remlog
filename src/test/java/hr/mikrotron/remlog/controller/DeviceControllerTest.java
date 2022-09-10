package hr.mikrotron.remlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.mikrotron.remlog.entity.Device;
import hr.mikrotron.remlog.repository.DeviceRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(DeviceController.class)
class DeviceControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper mapper;

  @Value("${userid}")
  private String userId;
  @MockBean
  private DeviceRepository deviceRepository;

  // Test data
  List<String> testStrings = new ArrayList<>(Arrays.asList("test 1", "test 2", "test 3"));
  List<Device> devices = new ArrayList<>(Arrays.asList(
    new Device(1l, testStrings.get(0), RandomStringUtils.random(16, "abcdef0123456789")),
    new Device(2l, testStrings.get(1), RandomStringUtils.random(16, "abcdef0123456789")),
    new Device(3l, testStrings.get(2), RandomStringUtils.random(16, "abcdef0123456789"))));
  String postBody = "{\"name\" : \"test 1\"}";

  @Test
  void getAllDevices() throws Exception{
    Mockito.when(deviceRepository.findAll()).thenReturn(devices);

    mockMvc.perform(MockMvcRequestBuilders
        .get("/devices")
        .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(3)))
          .andExpect(jsonPath("$[2].name", is(testStrings.get(2))));
  }

  @Test
  void getDeviceById() throws Exception{
    Mockito.when(deviceRepository.findById(devices.get(0).getId()))
        .thenReturn(Optional.of(devices.get(0)));

    mockMvc.perform(MockMvcRequestBuilders
        .get("/devices/1")
        .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", notNullValue()))
          .andExpect(jsonPath("$.name", is(testStrings.get(0))));
  }

  @Test
  void getDeviceByIdNotFound() throws Exception{
    Mockito.when(deviceRepository.findById(anyLong()))
        .thenThrow(DeviceController.DeviceNotFoundException.class);

    mockMvc.perform(MockMvcRequestBuilders
        .get("/devices/1")
        .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());
  }

  @Test
  void createDeviceAuthorized() throws Exception{
    Mockito.when(deviceRepository.save(any(Device.class))).thenReturn(devices.get(0));
    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
          .post("/devices/user/" + userId)
          .accept(MediaType.APPLICATION_JSON)
          .contentType(MediaType.APPLICATION_JSON)
          .content(postBody);

    mockMvc.perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", notNullValue()))
        .andExpect(jsonPath("$.name", is(testStrings.get(0))));;
  }

  @Test
  void createDeviceNotAuthorized() throws Exception{
    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
        .post("/devices/user/" + "unauthorized")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(postBody);

    mockMvc.perform(mockRequest).andExpect(status().isForbidden());
  }

}