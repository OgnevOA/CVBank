package telran.b7a.employer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import telran.b7a.employer.dto.AddCVDto;
import telran.b7a.employer.dto.EmployerDto;
import telran.b7a.employer.dto.NewEmployerDto;
import telran.b7a.employer.dto.UpdateEmployerDto;
import telran.b7a.employer.service.EmployerService;

@RestController
@RequestMapping("/cvbank/employer")
@CrossOrigin(origins = "*",
methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.PUT},
allowedHeaders = "*", exposedHeaders = "*")
public class EmployerController {

	EmployerService employerService;

	@Autowired
	public EmployerController(EmployerService employerService) {
		this.employerService = employerService;
	}

	@PostMapping("/signup")
	public EmployerDto addEmployer(@RequestBody NewEmployerDto newEmployer) {
		return employerService.addEmployer(newEmployer);
	}

	@PostMapping("/signin")
	public EmployerDto loginEmployer(Authentication authentication) {
		return employerService.loginEmployer(authentication.getName());
	}

	@GetMapping("/company/{companyName}")
	public List<EmployerDto> getEmployerByName(@PathVariable String companyName) {
		return employerService.getEmployerByName(companyName);
	}

	@GetMapping("/{email}")
	public EmployerDto getEmployerById(@PathVariable String email) {
		return employerService.getEmployerById(email);
	}

	@PutMapping
	public EmployerDto updateEmployer(Authentication authentication, @RequestBody UpdateEmployerDto newCredentials) {
		return employerService.updateEmployer(authentication.getName(), newCredentials);
	}

	@PutMapping("/login")
	public EmployerDto changeLogin(Authentication authentication, @RequestHeader("X-Login") String newLogin) {
		return employerService.changeLogin(authentication.getName(), newLogin);
	}

	@PutMapping("/pass")
	public EmployerDto changePassword(Authentication authentication, @RequestHeader("X-Password") String newPassword) {
		return employerService.changePassword(authentication.getName(), newPassword);

	} 

	@PutMapping("/collection/{collectionName}")
	public AddCVDto addCvCollection(Authentication authentication, @PathVariable String collectionName) {
		return employerService.addCvCollection(authentication.getName(), collectionName);
	}

	@PutMapping("/collection/{collectionName}/{cvId}")
	public AddCVDto addCvToCollection(Authentication authentication, @PathVariable String collectionName,
			@PathVariable String cvId) {
		return employerService.addCvToCollection(authentication.getName(), collectionName, cvId);
	}

	@DeleteMapping
	public void removeEmployer(Authentication authentications) {
		employerService.removeEmployer(authentications.getName());
	}

	@DeleteMapping("/collection/{collectionName}")
	public void removeCvCollection(Authentication authentication, @PathVariable String collectionName) {
		employerService.removeCvCollection(authentication.getName(), collectionName);
	}

	@DeleteMapping("/collection/{collectionName}/{cvId}")
	public void removeCvFromCollection(Authentication authentication, @PathVariable String collectionName,
			@PathVariable String cvId) {
		employerService.removeCvFromCollection(authentication.getName(), collectionName, cvId);
	}

}
