package telran.b7a.security;

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
import telran.b7a.employer.dao.EmployerMongoRepository;
import telran.b7a.employer.models.Employer;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	EmployeeAcconutingMongoRepository employeeRepo;

	@Autowired
	EmployerMongoRepository employerRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user;
		Employer employerAccount = employerRepo.findByApplicantInfoEmailIgnoreCase(username);
		if (employerAccount != null) {
			user = new User(username, employerAccount.getPassword(),
					AuthorityUtils.createAuthorityList("ROLE_EMPLOYER"));
		} else {
			Employee employee = employeeRepo.findById(username).orElseThrow(() -> new EmployeeNotFoundException());
			user = new User(username, employee.getPassword(), AuthorityUtils.createAuthorityList("ROLE_EMPLOYEE"));
		}
		return user;
	}
}