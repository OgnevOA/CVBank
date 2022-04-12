package telran.b7a.employer.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Applicant {

    String firstName;
    String lastName;
    String position;
    String phone;

}
