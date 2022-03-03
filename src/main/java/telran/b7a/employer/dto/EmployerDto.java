package telran.b7a.employer.dto;

import java.util.List;
import java.util.Map;

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
	
	ApplicantDto applicantInfo;
	CompanyDto companyInfo;
	Map<String, List<String>> cvCollections;

}
