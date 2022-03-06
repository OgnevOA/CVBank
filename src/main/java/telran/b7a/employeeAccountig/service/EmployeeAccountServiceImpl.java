package telran.b7a.employeeAccountig.service;

import java.util.Base64;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	public EmployeeAccountServiceImpl(EmployeeAcconutingMongoRepository repository, ModelMapper modelMapper) {
		this.repository = repository;
		this.modelMapper = modelMapper;
	}

	@Override
	public InfoEmployeeDto registerEmployee(RegisterEmployeeDto newEmployee) {
		if (repository.existsById(newEmployee.getEmail())) {
			throw new EmployeeAlreadyExistException();
		}
		Employee employee = modelMapper.map(newEmployee, Employee.class);
		String password = BCrypt.hashpw(newEmployee.getPassword(), BCrypt.gensalt());
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
	public UpdateEmployeeDto updateEmployee(UpdateEmployeeDto employeeData, String id) {
		Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException());
		employee.setFirstName(employeeData.getFirstName());
		employee.setLastName(employeeData.getLastName());
		repository.save(employee);
		return modelMapper.map(employee, UpdateEmployeeDto.class);
	}

	@Override
	public void deleteEmployee(String id) {
		Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException());
		repository.delete(employee);
	}

	@Override
	public InfoEmployeeDto loginEmployee(String token) {
		String email = getLogin(token).orElse("");
		Employee employee = repository.findById(email).orElseThrow(() -> new EmployeeNotFoundException());
		return modelMapper.map(employee, InfoEmployeeDto.class);
	}
	
	private Optional<String> getLogin(String token) {
		String login = null;
		try {
			token = token.split(" ")[1];
			byte[] bytesDecode = Base64.getDecoder().decode(token);
			token = new String(bytesDecode);
			login = token.split(":")[0];

		} catch (Exception e) {
			e.printStackTrace();

		}
		return Optional.ofNullable(login);
	}

}
