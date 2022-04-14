package telran.b7a.notifications.factory.types;

import lombok.Data;

@Data
public abstract class EmailType {
    public String address;
    public String firstName;
    public String lastName;
    public Object body;
    public String subject;
    public String type;
}
