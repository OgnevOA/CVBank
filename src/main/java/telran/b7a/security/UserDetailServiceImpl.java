package telran.b7a.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import telran.b7a.admin.dao.AdminMongoRepository;
import telran.b7a.admin.model.Admin;
import telran.b7a.employee.dao.EmployeeMongoRepository;
import telran.b7a.employee.model.Employee;
import telran.b7a.employer.dao.EmployerMongoRepository;
import telran.b7a.employer.models.Employer;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    EmployeeMongoRepository employeeRepo;
    EmployerMongoRepository employerRepo;
    AdminMongoRepository adminRepo;

    @Autowired
    public UserDetailServiceImpl(EmployeeMongoRepository employeeRepo, EmployerMongoRepository employerRepo, AdminMongoRepository adminRepo) {
        this.employeeRepo = employeeRepo;
        this.employerRepo = employerRepo;
        this.adminRepo = adminRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        if (employerRepo.existsById(username)) {
            System.out.println("employer");
            Employer employer = employerRepo.findById(username).orElse(null);
            user = new User(username, employer.getPassword(),
                    AuthorityUtils.createAuthorityList("ROLE_EMPLOYER"));
            return user;
        }
        if (employeeRepo.existsById(username)) {
            System.out.println("employee");
            Employee employee = employeeRepo.findById(username).orElse(null);
            System.out.println(employee);
            user = new User(username, employee.getPassword(),
                    AuthorityUtils.createAuthorityList("ROLE_EMPLOYEE"));
            return user;
        }
        if (adminRepo.existsById(username)) {
            System.out.println("admin");
            Admin admin = adminRepo.findById(username).orElse(null);
            user = new User(username, admin.getPassword(),
                    AuthorityUtils.createAuthorityList("ROLE_ADMINISTRATOR"));
            return user;
        }
        return null;
    }
}