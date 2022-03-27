package telran.b7a.notifications.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.b7a.cv.dao.CVRepository;
import telran.b7a.cv.exceptions.CVNotFoundException;
import telran.b7a.cv.models.CV;
import telran.b7a.employeeAccountig.dao.EmployeeAcconutingMongoRepository;
import telran.b7a.employeeAccountig.exceptions.EmployeeNotFoundException;
import telran.b7a.employeeAccountig.model.Employee;
import telran.b7a.notifications.interfaces.NotifyUser;

@Service
public class NotificationServiceImpl implements NotificationService {
	NotifyUser notifyUser;
	EmployeeAcconutingMongoRepository employeeRepo;
	CVRepository cvRepo;

	@Autowired
	public NotificationServiceImpl(NotifyUser notifyUser, EmployeeAcconutingMongoRepository employeeRepo,
			CVRepository cvRepo) {
		this.notifyUser = notifyUser;
		this.employeeRepo = employeeRepo;
		this.cvRepo = cvRepo;
	}

	@Override
	public void notifyUser(String userAddress) {
		notifyUser.notify(userAddress, "");
	}

	@Override
	public void recieveCVConfirmation(String cvId, String userId) {
		Employee employee = employeeRepo.findById(userId).orElseThrow(() -> new EmployeeNotFoundException());
		if (employee.getCv_id().contains(cvId)) {
			CV resume = cvRepo.findById(cvId).orElseThrow(() -> new CVNotFoundException());
			resume.setRelevant(true);
			cvRepo.save(resume);
			System.out.println("CV for " + resume.getPosition() + " confirmed!");
		}
	}

}
