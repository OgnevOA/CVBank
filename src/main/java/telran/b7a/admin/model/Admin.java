package telran.b7a.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Admins")
public class Admin {
    @Id
    String login;
    String password;
    Set<Expert> experts = new HashSet<>();
    SkillsVerification skillsVerification = new SkillsVerification();
    Companies companies = new Companies();
}
