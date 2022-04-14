package telran.b7a.employer.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Employers")
@EqualsAndHashCode(of = "email")
@Builder
@ToString
public class Employer {

    @Id
    String email;
    Applicant applicantInfo;
    Company companyInfo;
    String password;
    Map<String, Collection<String>> cvCollections;

    public Employer(String email, Applicant applicantInfo, Company companyInfo, String password) {
        this.email = email;
        this.applicantInfo = applicantInfo;
        this.companyInfo = companyInfo;
        this.password = password;
        this.cvCollections = new HashMap<>();
    }

}
