package telran.b7a.admin.dto.expert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUpdateExpertDto {
    String email;
    String firstName;
    String lastName;
    String speciality;
    Collection<String> topSkills = new ArrayList<>();
    String phone;
}
