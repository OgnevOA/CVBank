package telran.b7a.employer.notifications.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import telran.b7a.cv.dao.CVRepository;
import telran.b7a.cv.models.CV;
import telran.b7a.notifications.dao.NotificationBankMongoRepository;
import telran.b7a.notifications.factory.Email;
import telran.b7a.notifications.factory.NotifyUserEmail;
import telran.b7a.notifications.factory.types.EmailType;
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
        Stream<CV> cvs = cvRepo.findBydatePublished(LocalDate.now());
        cvs.forEach(cv -> {
            NotificationRecord record = new NotificationRecord(cv.getCvId().toHexString(), LocalDate.now().plusWeeks(1));
            notificationRepo.save(record);
//            notifyUserHelper(cv.getEmail(), record.getExpirationDate(), cv.getFirstName(), cv.getLastName(), record.getRecordId().toHexString());
            NotifyUserEmail data = new NotifyUserEmail(record.getExpirationDate(), record.getRecordId().toHexString(), cv.getFirstName(), cv.getLastName(), cv.getEmail(), "Confirm your CV relevance");
            Email email = new Email(data);
            email.send();
        });
    }

//    @Scheduled(cron = "0 * 10 ? * *")
    public void testMail() {
        EmailType data = new NotifyUserEmail(LocalDate.now().plusWeeks(2), "RecordId", "Oleg", "Ognev", "ognevoa94@gmail.com", "Confirm your CV relevance");
        Email email = new Email(data);
        email.send();
    }

    //    @Scheduled(cron = "0 0 8 ? * *")
    public void unpublishNotConfirmedCvs() {
        Stream<NotificationRecord> deletedRecords = notificationRepo.deleteRecordsByExpirationDateBefore(LocalDate.now());
        List<String> cvIds = deletedRecords.map(NotificationRecord::getCvId).collect(Collectors.toList());
        Iterable<CV> cvsToUnpublish = cvRepo.findAllById(cvIds);
        cvsToUnpublish.forEach(cv -> cv.setPublished(false));
        cvRepo.saveAll(cvsToUnpublish);
    }

    private void notifyUserHelper(String email, LocalDate date, String firstName, String lastName, String recordId) {
        notifyUser.notify(email, recordId, firstName + " " + lastName, date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}