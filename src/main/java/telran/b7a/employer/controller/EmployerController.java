package telran.b7a.employer.controller;

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

import telran.b7a.employer.dto.AddCVDto;
import telran.b7a.employer.dto.EmployerDto;
import telran.b7a.employer.dto.NewEmployerDto;
import telran.b7a.employer.dto.UpdateEmployerDto;
import telran.b7a.employer.service.EmployerService;

@RestController
@RequestMapping("/cvbank/employer")
public class EmployerController {

	EmployerService employerService;

	@Autowired
	public EmployerController(EmployerService employerService) {
		this.employerService = employerService;
	}

	@PostMapping("/register")
	public EmployerDto addEmployer(@RequestBody NewEmployerDto newEmployer) {
		return employerService.addEmployer(newEmployer);

	}

	@PostMapping("/login")
	public EmployerDto loginEmployer(@RequestHeader("Authorization") String token) {
		return employerService.loginEmployer(token);
	}

	@GetMapping("/company/{companyName}")
	public EmployerDto getEmployer(@PathVariable String companyName) {
		return employerService.getEmployer(companyName);
	}

	@PutMapping("/{employerId}")
	public EmployerDto updateEmployer(@PathVariable String employerId, @RequestBody UpdateEmployerDto newCredentials) {
		return employerService.updateEmployer(employerId, newCredentials);
	}

	@DeleteMapping("/{employerId}")
	public void removeEmployer(@PathVariable String employerId) {
		employerService.removeEmployer(employerId);
	}

	@PutMapping("/{employerId}/collection/{collectionName}")
	public AddCVDto addCvCollection(@PathVariable String employerId, @PathVariable String collectionName) {
		return employerService.addCVCollection(employerId, collectionName);
	}

	@PutMapping("/{employerId}/collection/{collectionName}/{cvId}")
	public AddCVDto addCvToCollection(@PathVariable String employerId, @PathVariable String collectionName,
			@PathVariable String cvId) {
		return employerService.addCVtoCollection(employerId, collectionName, cvId);
	}

}
