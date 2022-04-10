package telran.b7a;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import telran.b7a.employee.dao.EmployeeMongoRepository;
import telran.b7a.employee.dto.RegisterEmployeeDto;
import telran.b7a.employee.dto.UpdateEmployeeDto;
import telran.b7a.employee.dto.exceptions.EmployeeAlreadyExistException;
import telran.b7a.employee.model.Employee;
import telran.b7a.employee.service.EmployeeAccountServiceImpl;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class CvbankApplicationTests {

    private static final String BASE_URL_EMPLOYEE = "/cvbank/employee";
    EmployeeAccountServiceImpl employeeService;
    EmployeeMongoRepository employeeMongoRepository;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter;
    ModelMapper modelMapper;
    MockMvc mockMvc;


    @Autowired
    public CvbankApplicationTests(EmployeeAccountServiceImpl employeeService, ModelMapper modelMapper, MockMvc mockMvc, EmployeeMongoRepository employeeMongoRepository) {
        this.employeeService = employeeService;
        this.modelMapper = modelMapper;
        this.mockMvc = mockMvc;
        this.employeeMongoRepository = employeeMongoRepository;
    }

    Employee employee = Employee.builder()
            .email("green@goblin.com")
            .password("123456")
            .firstName("Norman")
            .lastName("Osborn")
            .cv_id(new HashSet<>())
            .build();

    UpdateEmployeeDto updateEmployeeDto = UpdateEmployeeDto.builder()
            .firstName("Harry")
            .lastName("Osborn")
            .build();

    @Test
    @Order(1)
    public void addEmployee() throws Exception {
        RegisterEmployeeDto newEmployeeDto = modelMapper.map(employee, RegisterEmployeeDto.class);
        assertEquals(employee.getEmail(), employeeService.registerEmployee(newEmployeeDto).getEmail());
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(employee);
        mockMvc.perform(post(BASE_URL_EMPLOYEE + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("Employee already exist")))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeAlreadyExistException));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "EMPLOYER")
    public void getEmployeeIfEmployer() throws Exception {
        mockMvc.perform(get(BASE_URL_EMPLOYEE + "/green@goblin.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "green@goblin.com", password = "123456", roles = "EMPLOYEE")
    public void getEmployeeIfEmployeeOwner() throws Exception {
        mockMvc.perform(get(BASE_URL_EMPLOYEE + "/green@goblin.com"))
                        .andExpect(status().isOk());
        assertNotNull(employeeService.getEmployee("green@goblin.com"));
        assertEquals("Norman", employeeService.getEmployee("green@goblin.com").getFirstName());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "EMPLOYEE")
    public void getEmployeeIfEmployeeNotOwner() throws Exception {
        mockMvc.perform(get(BASE_URL_EMPLOYEE + "/green@goblin.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "green@goblin.com", password = "123456", roles = "EMPLOYEE")
    public void updateEmployeeIfEmployeeOwner() throws Exception {
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String json = objectWriter.writeValueAsString(updateEmployeeDto);
        mockMvc.perform(put(BASE_URL_EMPLOYEE + "/green@goblin.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
        assertEquals("Harry", employeeService.getEmployee("green@goblin.com").getFirstName());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "EMPLOYEE")
    public void updateEmployeeIfEmployeeNotOwner() throws Exception {
        String json = convertToJson(updateEmployeeDto);
        mockMvc.perform(put(BASE_URL_EMPLOYEE + "/green@goblin.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "EMPLOYER")
    public void updateEmployeeIfEmployer() throws Exception {
        String json = convertToJson(updateEmployeeDto);
        mockMvc.perform(put(BASE_URL_EMPLOYEE + "/green@goblin.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    private String convertToJson(UpdateEmployeeDto updateEmployeeDto) throws JsonProcessingException {
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(updateEmployeeDto);
    }
}
