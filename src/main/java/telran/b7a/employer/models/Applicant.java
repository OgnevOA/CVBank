package telran.b7a.employer.models;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "email")
public class Applicant  {
	

	@Id
	String email;
	String firstName;
	String lastName;
	String position;
	String phone;

}
