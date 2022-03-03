package telran.b7a.employer.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Employers")
@ToString
public class Employer {
	@Id
	String id;
	Applicant applicantInfo;
	Company companyInfo;
	String password;
	Map<String, List<String>> cvCollections;
	
	public Employer(Applicant applicantInfo, Company companyInfo, String password) {
		this.applicantInfo = applicantInfo;
		this.companyInfo = companyInfo;
		this.password = password;
		this.cvCollections = new HashMap<>();
	}
	
	

}