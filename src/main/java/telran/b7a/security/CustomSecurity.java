package telran.b7a.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.b7a.employeeAccountig.dao.EmployeeAcconutingMongoRepository;
import telran.b7a.employeeAccountig.dto.exceptions.EmployeeNotFoundException;
import telran.b7a.employeeAccountig.model.Employee;
import telran.b7a.employer.dao.EmployerMongoRepository;
import telran.b7a.employer.exceptions.EmployerNotFoundException;
import telran.b7a.employer.models.Employer;

@Service("customSecurity")
public class CustomSecurity {
	EmployeeAcconutingMongoRepository employeeRepo;
	EmployerMongoRepository employerRepo;

	@Autowired
	public CustomSecurity(EmployeeAcconutingMongoRepository employeeRepo, EmployerMongoRepository employerRepo) {
		this.employeeRepo = employeeRepo;
		this.employerRepo = employerRepo;
	}

	public boolean checkEmployeeCollectionAuthority(String companyId, String email) {
		Employer employerA = employerRepo.findById(companyId).orElseThrow(() -> new EmployerNotFoundException());
		Employer employerB = employerRepo.findByApplicantInfoEmailIgnoreCase(email);
		return employerA.equals(employerB);
	}
	
	public boolean checkCVAuthority(String cvid, String name) {
		Employee employee = employeeRepo.findById(name).orElseThrow(() -> new EmployeeNotFoundException());
		System.out.println(employee.getCvs().contains(cvid));
		return employee.getCvs().contains(cvid);
	}
}
