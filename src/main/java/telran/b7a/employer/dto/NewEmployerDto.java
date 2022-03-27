package telran.b7a.employer.dto;

import lombok.*;

//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewEmployerDto {

//    @Email(message = "Email not valid")
//    @NotEmpty(message = "Email can't be empty")
    String email;
    ApplicantDto applicantInfo;
    CompanyDto companyInfo;
//    @Size(min = 6, max = 20, message = "Password not valid")
    String password;

}
