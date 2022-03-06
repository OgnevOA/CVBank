package telran.b7a.cv.models;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Experience {

	String date;
	String company;
	String website;
	String address;
	Set<Project> projects;

}
