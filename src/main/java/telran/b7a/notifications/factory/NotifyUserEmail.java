package telran.b7a.notifications.factory;

import lombok.*;
import telran.b7a.notifications.factory.types.EmailType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotifyUserEmail extends EmailType {
    LocalDate date;
    String recordId;

    public NotifyUserEmail(LocalDate date, String recordId, String firstName, String lastName, String address, String subject) {
        this.date = date;
        this.recordId = recordId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.body = body;
        this.subject = subject;

    }

    @Override
    public Object getBody() {
        return "<h1>Hello "
                + this.getFirstName() + " " + this.getLastName() +
                "!</h1>" +
                "<p>To confirm your CV click " +
                "<a href=\"http://localhost:8080/cvbank/notify/" + this.getRecordId() + "\">here</a></p>" +
                "<p>This link is will expire after " +
                this.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                "</p>";
    }

    @Override
    public String getType() {
        return "text/html";
    }
}
