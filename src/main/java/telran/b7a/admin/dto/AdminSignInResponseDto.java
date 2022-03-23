package telran.b7a.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import telran.b7a.admin.dto.expert.AddUpdateExpertDto;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminSignInResponseDto {
    SkillsVerificationDto skillsVerification = new SkillsVerificationDto();
    CompaniesDto companies = new CompaniesDto();
    Collection<AddUpdateExpertDto> experts = new ArrayList<>();
}
