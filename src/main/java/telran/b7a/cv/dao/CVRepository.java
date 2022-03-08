package telran.b7a.cv.dao;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.b7a.cv.models.CV;

public interface CVRepository extends MongoRepository<CV, String> {
	
	Stream<CV> findBycvIdIn(List<String> cvsId);

}
