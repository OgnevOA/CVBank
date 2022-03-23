package telran.b7a.admin.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import telran.b7a.admin.model.Expert;

public interface ExpertMongoRepository extends MongoRepository<Expert, String> {
}
