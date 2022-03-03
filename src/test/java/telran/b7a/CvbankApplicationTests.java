package telran.b7a;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.b7a.employer.dao.EmployerMongoRepository;
import telran.b7a.employer.dto.AddressDto;
import telran.b7a.employer.dto.ApplicantDto;
import telran.b7a.employer.dto.CompanyDto;
import telran.b7a.employer.dto.EmployerDto;
import telran.b7a.employer.dto.NewEmployerDto;
import telran.b7a.employer.models.Employer;
import telran.b7a.employer.service.EmployerService;

@SpringBootTest
class CvbankApplicationTests {

	EmployerMongoRepository employersRepository;
	EmployerService employerService;

	NewEmployerDto testNewEmployerDto;
	ApplicantDto testApplicant = new ApplicantDto("mary@company.com", "Mary", "Star", "HR", "+972-55-111-11-15");
	AddressDto testAddres = new AddressDto("USA", "New York", "Manhattan", 1, 100000);
	CompanyDto testCompany = new CompanyDto("Company", "company.com", "+972-55-111-11-01", testAddres);
	String testPassword = "0000";
	Map<String, List<String>> cvCollectionTest = new HashMap<>();
	EmployerDto employerTestDto;

	@Autowired
	public CvbankApplicationTests(EmployerMongoRepository employersRepository, EmployerService employerService) {
		this.employersRepository = employersRepository;
		this.employerService = employerService;
	}

	@BeforeEach
	void setUp() throws Exception {
		testNewEmployerDto = new NewEmployerDto(testApplicant, testCompany, testPassword);
		employerTestDto = new EmployerDto(testApplicant, testCompany, cvCollectionTest);
	}

	@Test
	public void testAdd() {
		EmployerDto employerDto = employerService.addEmployer(testNewEmployerDto);
		EmployerDto getEmployerDto = employerService.getEmployer(testCompany.getName());
		assertEquals(employerDto.getApplicantInfo().getEmail(), getEmployerDto.getApplicantInfo().getEmail());
	}

	@AfterEach
	void clear() {
		Employer employer = employersRepository.findByApplicantInfoEmailIgnoreCase(testApplicant.getEmail());
		employersRepository.delete(employer);
	}

}
