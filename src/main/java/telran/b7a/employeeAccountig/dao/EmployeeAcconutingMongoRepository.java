package telran.b7a.employeeAccountig.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.b7a.employeeAccountig.model.Employee;

public interface EmployeeAcconutingMongoRepository extends MongoRepository<Employee, String> {

}
