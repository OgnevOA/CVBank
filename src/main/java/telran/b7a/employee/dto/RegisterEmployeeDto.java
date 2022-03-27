package telran.b7a.employee.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterEmployeeDto {
	String email;
	String firstName;
	String lastName;
	String password;
}
