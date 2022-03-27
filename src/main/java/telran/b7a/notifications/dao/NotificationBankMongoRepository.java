package telran.b7a.notifications.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import telran.b7a.notifications.model.NotificationRecord;

public interface NotificationBankMongoRepository extends MongoRepository<NotificationRecord, String> {
}
