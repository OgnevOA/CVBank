package telran.b7a.employer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address  {
	

	String country;
	String city;
	String street;
	Integer building;
	Integer zip;

}
