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
class CvbankApplicationEmployeeTests {

    private static final String BASE_URL_EMPLOYEE = "/cvbank/employee";
    EmployeeAccountServiceImpl employeeService;
    EmployeeMongoRepository employeeMongoRepository;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter;
    ModelMapper modelMapper;
    MockMvc mockMvc;


    @Autowired
    public CvbankApplicationEmployeeTests(EmployeeAccountServiceImpl employeeService, ModelMapper modelMapper, MockMvc mockMvc, EmployeeMongoRepository employeeMongoRepository) {
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
        String json = convertToJson(newEmployeeDto);
        mockMvc.perform(post(BASE_URL_EMPLOYEE + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("Employee already exist")))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployeeAlreadyExistException));
    }

    @Test
    @Order(2)
    @WithMockUser(username = "test", password = "test", roles = "EMPLOYER")
    public void getEmployeeIfEmployer() throws Exception {
        mockMvc.perform(get(BASE_URL_EMPLOYEE + "/green@goblin.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "green@goblin.com", password = "123456", roles = "EMPLOYEE")
    public void getEmployeeIfEmployeeOwner() throws Exception {
        mockMvc.perform(get(BASE_URL_EMPLOYEE + "/green@goblin.com"))
                .andExpect(status().isOk());
        assertNotNull(employeeService.getEmployee("green@goblin.com"));
        assertEquals("Norman", employeeService.getEmployee("green@goblin.com").getFirstName());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "test", password = "test", roles = "EMPLOYEE")
    public void getEmployeeIfEmployeeNotOwner() throws Exception {
        mockMvc.perform(get(BASE_URL_EMPLOYEE + "/green@goblin.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(10)
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
    @Order(10)
    @WithMockUser(username = "test", password = "test", roles = "EMPLOYEE")
    public void updateEmployeeIfEmployeeNotOwner() throws Exception {
        String json = convertToJson(updateEmployeeDto);
        mockMvc.perform(put(BASE_URL_EMPLOYEE + "/green@goblin.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(10)
    @WithMockUser(username = "test", password = "test", roles = "EMPLOYER")
    public void updateEmployeeIfEmployer() throws Exception {
        String json = convertToJson(updateEmployeeDto);
        mockMvc.perform(put(BASE_URL_EMPLOYEE + "/green@goblin.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(11)
    @WithMockUser(username = "green@goblin.com", password = "123456", roles = "EMPLOYEE")
    public void changeLoginIfEmployeeOwner() throws Exception {
        mockMvc.perform(put(BASE_URL_EMPLOYEE + "/login")
                        .header("X-Login", "miles@spidermail.com"))
                .andExpect(status().isOk());
        assertNull(employeeMongoRepository.findById("green@goblin.com").orElse(null));
        assertNotNull(employeeMongoRepository.findById("miles@spidermail.com").orElse(null));
    }

    @Test
    @Order(12)
    @WithMockUser(username = "test", password = "test", roles = "EMPLOYEE")
    public void changeLoginIfAnotherEmployee() throws Exception {
        mockMvc.perform(put(BASE_URL_EMPLOYEE + "/login")
                        .header("X-Login", "miles@spidermail.com"))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(11)
    @WithMockUser(username = "test", password = "test", roles = "EMPLOYER")
    public void changeLoginIfEmployer() throws Exception {
        mockMvc.perform(put(BASE_URL_EMPLOYEE + "/login")
                        .header("X-Login", "miles@spidermail.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(13)
    @WithMockUser(username = "miles@spidermail.com", password = "123456", roles = "EMPLOYEE")
    public void changePasswordIfEmployee() throws Exception {
        String oldPassword = employeeMongoRepository.findById("miles@spidermail.com").orElse(null).getPassword();
        mockMvc.perform(put(BASE_URL_EMPLOYEE + "/pass")
                        .header("X-Password", "0000"))
                .andExpect(status().isOk());
        String newPassword = employeeMongoRepository.findById("miles@spidermail.com").orElse(null).getPassword();
        assertNotEquals(oldPassword, newPassword);
    }

    @Test
    @Order(13)
    @WithMockUser(username = "miles@spidermail.com", password = "0000", roles = "EMPLOYER")
    public void changePasswordIfEmployer() throws Exception {
        mockMvc.perform(put(BASE_URL_EMPLOYEE + "/pass")
                        .header("X-Password", "0000"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(100)
    @WithMockUser(username = "miles@spidermail.com", password = "123456", roles = "EMPLOYEE")
    public void deleteEmployeeIfEmployeeOwner() throws Exception {
        assertNotNull(employeeMongoRepository.findById("miles@spidermail.com"));
        mockMvc.perform(delete(BASE_URL_EMPLOYEE + "/miles@spidermail.com"))
                .andExpect(status().isOk());
        assertNull(employeeMongoRepository.findById("miles@spidermail.com").orElse(null));
    }

    @Test
    @Order(101)
    @WithMockUser(username = "test", password = "test", roles = "EMPLOYEE")
    public void deleteEmployeeIfEmployeeNotOwner() throws Exception {
        assertNotNull(employeeMongoRepository.findById("miles@spidermail.com"));
        mockMvc.perform(delete(BASE_URL_EMPLOYEE + "/miles@spidermail.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(102)
    @WithMockUser(username = "test", password = "test", roles = "EMPLOYER")
    public void deleteEmployeeIfEmployer() throws Exception {
        assertNotNull(employeeMongoRepository.findById("miles@spidermail.com"));
        mockMvc.perform(delete(BASE_URL_EMPLOYEE + "/miles@spidermail.com"))
                .andExpect(status().isForbidden());
    }


    private String convertToJson(Object objectForConvert) throws JsonProcessingException {
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(objectForConvert);
    }
}
