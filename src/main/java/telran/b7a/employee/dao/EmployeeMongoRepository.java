package telran.b7a.employee.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.b7a.employee.model.Employee;

public interface EmployeeMongoRepository extends MongoRepository<Employee, String> {

}
