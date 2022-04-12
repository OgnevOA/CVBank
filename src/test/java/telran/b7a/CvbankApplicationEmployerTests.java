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
import org.springframework.test.web.servlet.MockMvc;
import telran.b7a.employer.dao.EmployerMongoRepository;
import telran.b7a.employer.dto.NewEmployerDto;
import telran.b7a.employer.dto.exceptions.EmployerExistException;
import telran.b7a.employer.models.Address;
import telran.b7a.employer.models.Applicant;
import telran.b7a.employer.models.Company;
import telran.b7a.employer.models.Employer;
import telran.b7a.employer.service.EmployerService;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
class CvbankApplicationEmployerTests {

    private static final String BASE_URL_EMPLOYEE = "/cvbank/employer";
    EmployerService employerService;
    EmployerMongoRepository employerMongoRepository;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter;
    ModelMapper modelMapper;
    MockMvc mockMvc;


    @Autowired
    public CvbankApplicationEmployerTests(EmployerService employerService, ModelMapper modelMapper, MockMvc mockMvc, EmployerMongoRepository employerMongoRepository) {
        this.employerService = employerService;
        this.modelMapper = modelMapper;
        this.mockMvc = mockMvc;
        this.employerMongoRepository = employerMongoRepository;
    }

    Applicant applicatInfo = Applicant.builder()
            .firstName("Bruce")
            .lastName("Wayne")
            .position("CEO")
            .phone("000-000-000")
            .build();

    Address address = Address.builder()
            .country("USA")
            .city("Gotham")
            .street("Franklin")
            .building(5)
            .zip(123456)
            .build();

    Company companyInfo = Company.builder()
            .name("Wayne Enterprise")
            .website("www.batman.com")
            .phone("000-000-000")
            .address(address)
            .build();

    Employer employer = Employer.builder()
            .email("bruce@wayne.com")
            .applicantInfo(applicatInfo)
            .companyInfo(companyInfo)
            .password("123456")
            .cvCollections(new HashMap<>())
            .build();

    @Test
    @Order(1)
    public void addEmployer() throws Exception {
        NewEmployerDto newEmployerDto = modelMapper.map(employer, NewEmployerDto.class);
        assertEquals(newEmployerDto.getEmail(), employerService.addEmployer(newEmployerDto).getEmail());
        String json = convertToJson(newEmployerDto);
        mockMvc.perform(post(BASE_URL_EMPLOYEE + "/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("Employer already exist")))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmployerExistException));
    }


    private String convertToJson(Object objectForConvert) throws JsonProcessingException {
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(objectForConvert);
    }
}
