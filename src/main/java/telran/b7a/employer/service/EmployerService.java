package telran.b7a.employer.service;

import telran.b7a.employer.dto.AddCVDto;
import telran.b7a.employer.dto.EmployerDto;
import telran.b7a.employer.dto.NewEmployerDto;
import telran.b7a.employer.dto.UpdateEmployerDto;

public interface EmployerService {

	EmployerDto addEmployer(NewEmployerDto newEmployer);

	void removeEmployer(String employerId);

	EmployerDto loginEmployer(String login);

	EmployerDto getEmployer(String companyName);

	EmployerDto updateEmployer(String employerId, UpdateEmployerDto newCredentials);

	AddCVDto addCvCollection(String employerId, String collectionName);

	void removeCvCollection(String employerId, String collectionName);

	AddCVDto addCvToCollection(String employerId, String collectionName, String cvId);
	
	void removeCvFromCollection(String employerId, String collectionName, String cvId);

}
