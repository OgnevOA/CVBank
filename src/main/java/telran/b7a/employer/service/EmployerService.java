package telran.b7a.employer.service;

import java.util.List;

import telran.b7a.employer.dto.AddCVDto;
import telran.b7a.employer.dto.EmployerDto;
import telran.b7a.employer.dto.NewEmployerDto;
import telran.b7a.employer.dto.UpdateEmployerDto;

public interface EmployerService {

	EmployerDto addEmployer(NewEmployerDto newEmployer);

	void removeEmployer(String employerId);

	EmployerDto loginEmployer(String login);
	
	EmployerDto getEmployerById(String email);

	List<EmployerDto> getEmployerByName(String companyName);

	EmployerDto updateEmployer(String email, UpdateEmployerDto newCredentials);
	
	EmployerDto changeLogin(String email, String newLogin);
	
	EmployerDto changePassword(String email, String newPassword);

	AddCVDto addCvCollection(String email, String collectionName);

	void removeCvCollection(String email, String collectionName);

	AddCVDto addCvToCollection(String email, String collectionName, String cvId);
	
	void removeCvFromCollection(String email, String collectionName, String cvId);

}
