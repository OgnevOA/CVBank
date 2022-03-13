package telran.b7a.cv.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import telran.b7a.cv.enums.LevelConfirm;
import telran.b7a.cv.enums.LevelVerification;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "CV")
@EqualsAndHashCode(of = "cvId")
@ToString
public class CV {

	@Id
	ObjectId cvId;
	String firstName;
	String lastName;
	String email;
	String phone;
	String position;
	String location;
	@GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
	Point coordinates;
	LevelVerification cvScore = LevelVerification.NOT_VERIFIED;
	LevelConfirm cvConfirm = LevelConfirm.PENDING;
	boolean isRelocated;
	String preambule;
	Set<String> skills;
	List<Education> educations;
	List<Experience> experience;
	Other other;
	LocalDate dateCreated = LocalDate.now();
	Set<String> links;
	Set<String> hideFields = new HashSet<>();

}
