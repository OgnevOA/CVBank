package telran.b7a.cv.models;

import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "CV")
@EqualsAndHashCode(of = "cvId")
public class CV {
	
	@Id
	String cvId;
	String firstName;
	String lastName;
	String email;
	String phone;
	int isVerified;
	String preambule;
	Set<String> skills;
	List<Education> educations;
	List<Experience> experience;
	Other other;
	Set<String> links;

}
