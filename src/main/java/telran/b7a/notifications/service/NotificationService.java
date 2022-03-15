package telran.b7a.notifications.service;

public interface NotificationService {
	void notifyUser(String userAddress);
	
	void recieveCVConfirmation(String cvId, String userId);
}
