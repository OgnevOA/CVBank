package telran.b7a.employee.service;

import telran.b7a.employee.dto.InfoEmployeeDto;
import telran.b7a.employee.dto.RegisterEmployeeDto;
import telran.b7a.employee.dto.UpdateEmployeeDto;

public interface EmployeeAccountService {
	InfoEmployeeDto registerEmployee(RegisterEmployeeDto newEmployee);
	
	InfoEmployeeDto getEmployee(String id);
	
	InfoEmployeeDto updateEmployee(UpdateEmployeeDto employeeData, String id);
	
	InfoEmployeeDto changeEmployeeLogin(String id, String newLogin);
	
	void changeEmployeePassword(String id, String newPassword);
	
	void deleteEmployee(String id);
}