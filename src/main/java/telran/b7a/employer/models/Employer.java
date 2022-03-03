package telran.b7a.employer.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Employers")
@EqualsAndHashCode(of = "id")
@ToString
public class Employer {
	@Id
	String id;
	Applicant applicantInfo;
	Company companyInfo;
	String password;
	Map<String, Set<String>> cvCollections;
	
	public Employer(Applicant applicantInfo, Company companyInfo, String password) {
		this.applicantInfo = applicantInfo;
		this.companyInfo = companyInfo;
		this.password = password;
		this.cvCollections = new HashMap<>();
	}
	
	

}
