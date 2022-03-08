package telran.b7a.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.b7a.employeeAccountig.dao.EmployeeAcconutingMongoRepository;
import telran.b7a.employeeAccountig.dto.exceptions.EmployeeNotFoundException;
import telran.b7a.employeeAccountig.model.Employee;

@Service("customSecurity")
public class CustomSecurity {
	EmployeeAcconutingMongoRepository employeeRepo;

	@Autowired
	public CustomSecurity(EmployeeAcconutingMongoRepository employeeRepo) {
		this.employeeRepo = employeeRepo;
	}
	
	public boolean checkCVAuthority(String cvid, String name) {
		Employee employee = employeeRepo.findById(name).orElseThrow(() -> new EmployeeNotFoundException());
		System.out.println(employee.getCvs().contains(cvid));
		return employee.getCvs().contains(cvid);
	}
}
