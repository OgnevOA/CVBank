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
public class NewEmployerDto {
	
	String email;
	ApplicantDto applicantInfo;
	CompanyDto companyInfo;
	String password;

}
