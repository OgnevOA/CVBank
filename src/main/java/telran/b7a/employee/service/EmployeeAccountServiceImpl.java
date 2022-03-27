package telran.b7a.employee.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import telran.b7a.employee.dao.EmployeeMongoRepository;
import telran.b7a.employee.dto.InfoEmployeeDto;
import telran.b7a.employee.dto.RegisterEmployeeDto;
import telran.b7a.employee.dto.UpdateEmployeeDto;
import telran.b7a.employee.dto.exceptions.EmployeeAlreadyExistException;
import telran.b7a.employee.dto.exceptions.EmployeeNotFoundException;
import telran.b7a.employee.model.Employee;

@Service
public class EmployeeAccountServiceImpl implements EmployeeAccountService {
    EmployeeMongoRepository employeeRepo;
    ModelMapper modelMapper;
    PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeAccountServiceImpl(EmployeeMongoRepository employeeRepo, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.employeeRepo = employeeRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public InfoEmployeeDto registerEmployee(RegisterEmployeeDto newEmployee) {
        if (employeeRepo.existsById(newEmployee.getEmail())) {
            throw new EmployeeAlreadyExistException();
        }
        Employee employee = modelMapper.map(newEmployee, Employee.class);
        String password = passwordEncoder.encode(newEmployee.getPassword());
        employee.setPassword(password);
        employeeRepo.save(employee);
        return modelMapper.map(employee, InfoEmployeeDto.class);
    }

    @Override
    public InfoEmployeeDto getEmployee(String id) {
        Employee employee = getEmployeeById(id);
        return modelMapper.map(employee, InfoEmployeeDto.class);
    }

    @Override
    public InfoEmployeeDto updateEmployee(UpdateEmployeeDto employeeData, String id) {
        Employee employee = getEmployeeById(id);
        employee.setFirstName(employeeData.getFirstName());
        employee.setLastName(employeeData.getLastName());
        employeeRepo.save(employee);
        return modelMapper.map(employee, InfoEmployeeDto.class);
    }

    @Override
    public void deleteEmployee(String id) {
        Employee employee = getEmployeeById(id);
        employeeRepo.delete(employee);
    }

    @Override
    public InfoEmployeeDto changeEmployeeLogin(String id, String newLogin) {
        if (employeeRepo.existsById(newLogin)) {
            throw new EmployeeAlreadyExistException();
        }
        Employee employee = getEmployeeById(id);
        employee.setEmail(newLogin);
        employeeRepo.deleteById(id);
        employeeRepo.save(employee);
        return modelMapper.map(employee, InfoEmployeeDto.class);
    }

    @Override
    public void changeEmployeePassword(String id, String newPassword) {
        Employee employee = getEmployeeById(id);
        String password = passwordEncoder.encode(newPassword);
        employee.setPassword(password);
        employeeRepo.save(employee);
    }

    private Employee getEmployeeById(String id) {
        return employeeRepo.findById(id).orElseThrow(EmployeeNotFoundException::new);
    }
}
