package telran.b7a.admin.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

@Data
@EqualsAndHashCode(of = {"email"})
public class Expert {
    String email;
    String firstName;
    String lastName;
    String speciality;
    Collection<String> topSkills;
    String phone;
}
