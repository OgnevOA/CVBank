package telran.b7a.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.b7a.employee.dao.EmployeeMongoRepository;
import telran.b7a.employee.dto.exceptions.EmployeeNotFoundException;
import telran.b7a.employee.model.Employee;
import telran.b7a.employer.dao.EmployerMongoRepository;

@Service("customSecurity")
public class CustomSecurity {
	EmployeeMongoRepository employeeRepo;
	EmployerMongoRepository employerRepo;

	@Autowired
	public CustomSecurity(EmployeeMongoRepository employeeRepo, EmployerMongoRepository employerRepo) {
		this.employeeRepo = employeeRepo;
		this.employerRepo = employerRepo;
	}
	
	public boolean checkCVAuthority(String cvid, String name) {
		Employee employee = employeeRepo.findById(name).orElseThrow(() -> new EmployeeNotFoundException());
		return employee.getCv_id().contains(cvid);
	}
}
