package telran.b7a.employeeAccountig.service;

import telran.b7a.employeeAccountig.dto.InfoEmployeeDto;
import telran.b7a.employeeAccountig.dto.RegisterEmployeeDto;
import telran.b7a.employeeAccountig.dto.UpdateEmployeeDto;

public interface EmployeeAccountService {
	InfoEmployeeDto registerEmployee(RegisterEmployeeDto newEmployee);
	
	InfoEmployeeDto getEmployee(String id);
	
	InfoEmployeeDto loginEmployee(String token);
	
	UpdateEmployeeDto updateEmployee(UpdateEmployeeDto employeeData, String id);
	
	void deleteEmployee(String id);
}
