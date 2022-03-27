package telran.b7a.employer.dao;

import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.b7a.employer.models.Employer;

public interface EmployerMongoRepository extends MongoRepository<Employer, String> {
	
	Stream<Employer> findByCompanyInfoNameIgnoreCase(String сompanyName);

}
