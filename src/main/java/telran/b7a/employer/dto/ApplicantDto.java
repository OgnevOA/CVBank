package telran.b7a.employer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApplicantDto {
	
	String email;
	String firstName;
	String lastName;
	String position;
	String phone;
	

}
