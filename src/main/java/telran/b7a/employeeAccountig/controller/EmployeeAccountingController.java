package telran.b7a.employeeAccountig.controller;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.b7a.employeeAccountig.dto.InfoEmployeeDto;
import telran.b7a.employeeAccountig.dto.RegisterEmployeeDto;
import telran.b7a.employeeAccountig.dto.UpdateEmployeeDto;
import telran.b7a.employeeAccountig.service.EmployeeAccountService;

@RestController
@RequestMapping("/cvbank/employee")
public class EmployeeAccountingController {
	EmployeeAccountService employeeAccountService;

	@Autowired
	public EmployeeAccountingController(EmployeeAccountService employeeAccountService) {
		this.employeeAccountService = employeeAccountService;
	}

	@PostMapping("/register")
	public InfoEmployeeDto registerEmployee(@RequestBody RegisterEmployeeDto newEmployee) {
		return employeeAccountService.registerEmployee(newEmployee);
	}

	@PostMapping("/login")
	public InfoEmployeeDto loginEmployee(@RequestHeader("Authorization") String token) {
		System.out.println(token);
		String login = decodeBasicToken(token);
		System.out.println(login);
		return employeeAccountService.getEmployee(login);
	}

	@PutMapping("/{id}")
	public UpdateEmployeeDto updateEmployee(@RequestBody UpdateEmployeeDto employeeData,
			@PathVariable String id) {
		return employeeAccountService.updateEmployee(employeeData, id);
	}

	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable String id) {
		employeeAccountService.deleteEmployee(id);
	}

	@GetMapping("/{id}")
	public InfoEmployeeDto findEmployee(@PathVariable String id) {
		return employeeAccountService.getEmployee(id);
	}
	
	private String decodeBasicToken(String token) {
		String credentials = token.split(" ")[1];
		byte[] login = Base64.getDecoder().decode(credentials);
		return new String(login).split(":")[0];
	}
}
