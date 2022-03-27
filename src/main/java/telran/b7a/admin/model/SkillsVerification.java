package telran.b7a.admin.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class SkillsVerification {
    Collection<String> requests = new ArrayList<>();
    Collection<String> pending = new ArrayList<>();
    Collection<String> veryfied = new ArrayList<>();
    Collection<String> rejected = new ArrayList<>();
}
