package telran.b7a.cv.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.b7a.cv.models.CV;

public interface CVRepository extends MongoRepository<CV, String> {

}
