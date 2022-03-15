package telran.b7a.employeeAccountig.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import telran.b7a.employeeAccountig.dao.EmployeeAcconutingMongoRepository;
import telran.b7a.employeeAccountig.dto.InfoEmployeeDto;
import telran.b7a.employeeAccountig.dto.RegisterEmployeeDto;
import telran.b7a.employeeAccountig.dto.UpdateEmployeeDto;
import telran.b7a.employeeAccountig.dto.exceptions.EmployeeAlreadyExistException;
import telran.b7a.employeeAccountig.dto.exceptions.EmployeeNotFoundException;
import telran.b7a.employeeAccountig.model.Employee;

@Service
public class EmployeeAccountServiceImpl implements EmployeeAccountService {
	EmployeeAcconutingMongoRepository repository;
	ModelMapper modelMapper;
	PasswordEncoder passwordEncoder;

	@Autowired
	public EmployeeAccountServiceImpl(EmployeeAcconutingMongoRepository repository, ModelMapper modelMapper,
			PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public InfoEmployeeDto registerEmployee(RegisterEmployeeDto newEmployee) {
		if (repository.existsById(newEmployee.getEmail())) {
			throw new EmployeeAlreadyExistException();
		}
		Employee employee = modelMapper.map(newEmployee, Employee.class);
		String password = passwordEncoder.encode(newEmployee.getPassword());
		employee.setPassword(password);
		repository.save(employee);
		return modelMapper.map(employee, InfoEmployeeDto.class);
	}

	@Override
	public InfoEmployeeDto getEmployee(String id) {
		Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException());
		return modelMapper.map(employee, InfoEmployeeDto.class);
	}

	@Override
	public InfoEmployeeDto updateEmployee(UpdateEmployeeDto employeeData, String id) {
		Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException());
		employee.setFirstName(employeeData.getFirstName());
		employee.setLastName(employeeData.getLastName());
		repository.save(employee);
		return modelMapper.map(employee, InfoEmployeeDto.class);
	}

	@Override
	public void deleteEmployee(String id) {
		Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException());
		repository.delete(employee);
	}

	@Override
	public InfoEmployeeDto changeEmployeeLogin(String id, String newLogin) {
		if (repository.existsById(newLogin)) {
			throw new EmployeeAlreadyExistException();
		}
		Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException());
		employee.setEmail(newLogin);
		repository.deleteById(id);
		repository.save(employee);
		return modelMapper.map(employee, InfoEmployeeDto.class);
	}

	@Override
	public void changeEmployeePassword(String id, String newPassword) {
		Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException());
		String password = passwordEncoder.encode(newPassword);
		employee.setPassword(password);
		repository.save(employee);
	}
}
