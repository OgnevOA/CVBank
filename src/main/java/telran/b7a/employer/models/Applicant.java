package telran.b7a.employer.models;

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
	

	String email;
	String firstName;
	String lastName;
	String position;
	String phone;

}
