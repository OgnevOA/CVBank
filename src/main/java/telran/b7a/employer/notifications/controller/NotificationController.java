package telran.b7a.employer.notifications.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import telran.b7a.employer.notifications.service.NotificationService;

@RestController
@RequestMapping("/cvbank/notify")
@CrossOrigin(origins = "*", methods = { RequestMethod.DELETE, RequestMethod.GET, RequestMethod.OPTIONS,
		RequestMethod.POST, RequestMethod.PUT }, allowedHeaders = "*", exposedHeaders = "*")
public class NotificationController {
	NotificationService notificationService;

	@Autowired
	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@GetMapping("/{token}")
	public String confirmUserCV(@PathVariable String token) {
		return notificationService.recieveCVConfirmation(token);
	}
}
