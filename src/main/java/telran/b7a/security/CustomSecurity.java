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
	
	public boolean checkCVAuthority(String cvid, String name) {
		Employee employee = employeeRepo.findById(name).orElseThrow(() -> new EmployeeNotFoundException());
		return employee.getCv_id().contains(cvid);
	}
}
