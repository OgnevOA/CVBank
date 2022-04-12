package telran.b7a.employer.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {


    String country;
    String city;
    String street;
    Integer building;
    Integer zip;

}
