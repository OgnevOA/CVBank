package telran.b7a.employer.notifications.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
public class NotificationRecord {
    @Id
    ObjectId recordId;
    String cvId;
    LocalDate expirationDate;

    public NotificationRecord(String cvId, LocalDate expirationDate) {
        this.cvId = cvId;
        this.expirationDate = expirationDate;
    }
}
