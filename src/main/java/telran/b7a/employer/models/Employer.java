package telran.b7a.employer.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Employers")
@EqualsAndHashCode(of = "email")
@ToString
public class Employer {

    @Id
    @NotNull
    @Email
    String email;
    Applicant applicantInfo;
    Company companyInfo;
    @NotNull
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
