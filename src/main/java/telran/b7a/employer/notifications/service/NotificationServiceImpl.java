package telran.b7a.employer.notifications.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telran.b7a.cv.dao.CVRepository;
import telran.b7a.cv.dto.exceptions.CVNotFoundException;
import telran.b7a.cv.models.CV;
import telran.b7a.employee.dao.EmployeeMongoRepository;
import telran.b7a.employee.dto.exceptions.EmployeeNotFoundException;
import telran.b7a.employer.notifications.dao.NotificationBankMongoRepository;
import telran.b7a.employer.notifications.interfaces.NotifyUser;
import telran.b7a.employer.notifications.model.NotificationRecord;

import java.time.LocalDate;

@Service
public class NotificationServiceImpl implements NotificationService {
    NotifyUser notifyUser;
    EmployeeMongoRepository employeeRepo;
    CVRepository cvRepo;
    NotificationBankMongoRepository notificationRepo;

    @Autowired
    public NotificationServiceImpl(NotifyUser notifyUser, EmployeeMongoRepository employeeRepo, CVRepository cvRepo, NotificationBankMongoRepository notificationRepo) {
        this.notifyUser = notifyUser;
        this.employeeRepo = employeeRepo;
        this.cvRepo = cvRepo;
        this.notificationRepo = notificationRepo;
    }

    @Override
    public void notifyUser(String userAddress) {
        notifyUser.notify(userAddress, "");
    }

    @Override
    public String recieveCVConfirmation(String token) {
        NotificationRecord record = notificationRepo.findById(token).orElseThrow(EmployeeNotFoundException::new);
        if (record.getExpirationDate().isAfter(LocalDate.now())) {
            CV resume = cvRepo.findById(record.getCvId()).orElseThrow(CVNotFoundException::new);
            resume.setRelevant(true);
            resume.setDatePublished(LocalDate.now());
            cvRepo.save(resume);
            notificationRepo.delete(record);
            return "<p>CV for " + resume.getPosition() + " confirmed!</p>" +
                    "<p>You may now close this page</p>";
        }
        return "<p>Wrong token</p>";
    }
}
