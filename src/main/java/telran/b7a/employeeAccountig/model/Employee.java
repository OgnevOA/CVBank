package telran.b7a.employeeAccountig.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
	@Id
	String email;
	String password;
	String firstName;
	String lastName;
	Set<String> cvs = new HashSet<String>();
}
