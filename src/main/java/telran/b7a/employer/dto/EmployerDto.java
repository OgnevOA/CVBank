package telran.b7a.employer.dto;

import java.util.Map;
import java.util.Set;

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
public class EmployerDto {
	
	String email;
	ApplicantDto applicantInfo;
	CompanyDto companyInfo;
	Map<String, Set<String>> cvCollections;

}
