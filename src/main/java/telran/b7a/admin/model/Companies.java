package telran.b7a.admin.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class Companies {
    Collection<String> requests = new ArrayList<>();
    Collection<String> rejected = new ArrayList<>();
    Collection<String> archive = new ArrayList<>();
}
