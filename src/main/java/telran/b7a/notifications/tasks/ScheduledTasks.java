package telran.b7a.notifications.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import telran.b7a.cv.dao.CVRepository;
import telran.b7a.cv.models.CV;
import telran.b7a.notifications.dao.NotificationBankMongoRepository;
import telran.b7a.notifications.interfaces.NotifyUser;
import telran.b7a.notifications.model.NotificationRecord;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@EnableScheduling
public class ScheduledTasks {
    NotificationBankMongoRepository notificationRepo;
    CVRepository cvRepo;
    NotifyUser notifyUser;

    @Autowired
    public ScheduledTasks(NotificationBankMongoRepository notificationRepo, CVRepository cvRepo, NotifyUser notifyUser) {
        this.notificationRepo = notificationRepo;
        this.cvRepo = cvRepo;
        this.notifyUser = notifyUser;
    }

    //    @Scheduled(cron = "0 0 8 ? * *")
    public void sendNotifications() {
        Stream<CV> cvs = cvRepo.findBydatePublished(LocalDate.now().minusWeeks(2).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        cvs.forEach(cv -> {
            NotificationRecord record = new NotificationRecord(cv.getCvId().toHexString(), LocalDate.now().plusWeeks(1));
            notificationRepo.save(record);
            notifyUserHelper(cv.getEmail(), record.getExpirationDate(), cv.getFirstName(), cv.getLastName(), record.getRecordId().toHexString());
        });
    }

    //    @Scheduled(cron = "0 0 8 ? * *")
    public void unpublishNotConfirmedCvs() {
        Stream<NotificationRecord> deletedRecords = notificationRepo.deleteRecordsByExpirationDateBefore(LocalDate.now());
        List<String> cvIds = deletedRecords.map(r -> r.getCvId()).collect(Collectors.toList());
        Iterable<CV> cvsToUnpublish = cvRepo.findAllById(cvIds);
        cvsToUnpublish.forEach(cv -> cv.setPublished(false));
        cvRepo.saveAll(cvsToUnpublish);
    }

    private void notifyUserHelper(String email, LocalDate date, String firstName, String lastName, String recordId) {
        notifyUser.notify(email, recordId, firstName + " " + lastName, date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}