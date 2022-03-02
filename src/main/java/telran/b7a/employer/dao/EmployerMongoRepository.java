package telran.b7a.employer.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.b7a.employer.models.Employer;

public interface EmployerMongoRepository extends MongoRepository<Employer, String> {
	
	boolean existsByCompanyInfoNameIgnoreCase(String companyName);
	
	boolean existsByApplicantInfoEmailIgnoreCase(String email);
	
	Employer findByApplicantInfoEmailIgnoreCase(String email);
	
	Employer findByCompanyInfoNameIgnoreCase(String сompanyName);

}
