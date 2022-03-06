package telran.b7a.cv.dto;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewCVDto {
	
	String firstName;
	String lastName;
	String email;
	String phone;
	int isVerified;
	String preambule;
	Set<String> skills;
	List<ExperienceDto> experience;
	List<EducationDto> educations;
	OtherDto other;
	Set<String> links;

}
