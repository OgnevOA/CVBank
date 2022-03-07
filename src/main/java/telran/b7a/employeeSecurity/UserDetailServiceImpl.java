package telran.b7a.employeeSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import telran.b7a.employeeAccountig.dao.EmployeeAcconutingMongoRepository;
import telran.b7a.employeeAccountig.dto.exceptions.EmployeeNotFoundException;
import telran.b7a.employeeAccountig.model.Employee;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	EmployeeAcconutingMongoRepository employeeRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = employeeRepo.findById(username).orElseThrow(() -> new EmployeeNotFoundException());
		return new User(username, employee.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
	}
}
