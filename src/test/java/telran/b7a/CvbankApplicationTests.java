package telran.b7a;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import telran.b7a.employee.dto.RegisterEmployeeDto;
import telran.b7a.employee.model.Employee;
import telran.b7a.employee.service.EmployeeAccountServiceImpl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashSet;

@SpringBootTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class CvbankApplicationTests {

    EmployeeAccountServiceImpl employeeService;
    ModelMapper modelMapper;

    @Autowired
    public CvbankApplicationTests(EmployeeAccountServiceImpl employeeService, ModelMapper modelMapper) {
        this.employeeService = employeeService;
        this.modelMapper = modelMapper;
    }

    @BeforeEach
    public void initial() {
        Employee employee = Employee.builder()
                .email("peter@mail.com")
                .password("123456")
                .firstName("Peter")
                .lastName("Parker")
                .cv_id(new HashSet<>())
                .build();
        employeeService.registerEmployee(modelMapper.map(employee, RegisterEmployeeDto.class));
    }

    @Test
    public void getEmployee() {
        assertNotNull(employeeService.getEmployee("peter@mail.com"));


    }
}
