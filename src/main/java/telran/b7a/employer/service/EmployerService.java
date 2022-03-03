package telran.b7a.employer.service;

import telran.b7a.employer.dto.AddCVDto;
import telran.b7a.employer.dto.EmployerDto;
import telran.b7a.employer.dto.NewEmployerDto;
import telran.b7a.employer.dto.UpdateEmployerDto;

public interface EmployerService {
	
	EmployerDto addEmployer(NewEmployerDto newEmployer);
	
	EmployerDto loginEmployer(String token);
	
	EmployerDto getEmployer(String companyName);
	
	EmployerDto updateEmployer(String employerId, UpdateEmployerDto newCredentials);
	
	AddCVDto addCVCollection(String employerId, String collectionName);
	
	AddCVDto addCVtoCollection(String employerId, String collectionName, String cvId);
	
	void removeEmployer(String employerId);
}
