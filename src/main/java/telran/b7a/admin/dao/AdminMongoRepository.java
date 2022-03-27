package telran.b7a.admin.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import telran.b7a.admin.model.Admin;

public interface AdminMongoRepository extends MongoRepository<Admin, String> {
}
