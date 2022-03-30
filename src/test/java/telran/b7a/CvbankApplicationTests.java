package telran.b7a;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import telran.b7a.employee.model.Employee;
import telran.b7a.employee.service.EmployeeAccountServiceImpl;

import java.util.HashSet;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class CvbankApplicationTests {

    MongoTemplate mongoTemplate;
    EmployeeAccountServiceImpl employeeService;


    @BeforeEach
    public void initial() {
        Employee employee = Employee.builder()
                .email("peter@mail.com")
                .password("123456")
                .firstName("Peter")
                .lastName("Parker")
                .cv_id(new HashSet<>())
                .build();
    }

    @Test
    public void getEmployee() {


    }
}
