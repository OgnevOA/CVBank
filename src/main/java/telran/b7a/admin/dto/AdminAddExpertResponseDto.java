package telran.b7a.admin.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import telran.b7a.admin.dto.expert.AddUpdateExpertDto;

import java.util.Collection;

@Data
@Builder
public class AdminAddExpertResponseDto {
    String login;
    Collection<AddUpdateExpertDto> experts;
}
