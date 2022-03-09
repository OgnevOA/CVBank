package telran.b7a.cv.dto;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CVDto {

	String cvId;
	@JsonInclude(Include.NON_NULL)
	String firstName;
	@JsonInclude(Include.NON_NULL)
	String lastName;
	String email;
	@JsonInclude(Include.NON_NULL)
	String phone;
	String position;
	String location;
	int cvScore;
	boolean isVerified;
	boolean isRelocated;
	String preambule;
	Set<String> skills;
	@JsonInclude(Include.NON_NULL)
	List<ExperienceDto> experience;
	List<EducationDto> educations;
	OtherDto other;
	@JsonInclude(Include.NON_NULL)
	Set<String> links;

}
