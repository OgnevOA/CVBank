package telran.b7a.employer.notifications.service;

public interface NotificationService {
	void notifyUser(String userAddress);

	String recieveCVConfirmation(String token);
}
