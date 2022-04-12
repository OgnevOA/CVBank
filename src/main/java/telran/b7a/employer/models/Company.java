package telran.b7a.employer.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {

    String name;
    String website;
    String phone;
    Address address;

}
