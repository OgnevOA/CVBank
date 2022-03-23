package telran.b7a.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompaniesDto {
    Collection<String> requests = new ArrayList<>();
    Collection<String> rejected = new ArrayList<>();
    Collection<String> archive = new ArrayList<>();

}
