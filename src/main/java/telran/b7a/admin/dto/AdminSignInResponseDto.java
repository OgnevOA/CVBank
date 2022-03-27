package telran.b7a.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import telran.b7a.admin.dto.expert.AddUpdateExpertDto;
import telran.b7a.admin.model.Admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminSignInResponseDto {
    SkillsVerificationDto skillsVerification = new SkillsVerificationDto();
    CompaniesDto companies = new CompaniesDto();
    Collection<AddUpdateExpertDto> experts = new ArrayList<>();

    public AdminSignInResponseDto(Admin admin) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        companies = CompaniesDto.builder()
                .archive(admin.getCompanies().getArchive())
                .rejected(admin.getCompanies().getRejected())
                .requests(admin.getCompanies().getRequests())
                .build();
        skillsVerification = SkillsVerificationDto.builder()
                .pending(admin.getSkillsVerification().getPending())
                .veryfied(admin.getSkillsVerification().getVeryfied())
                .rejected(admin.getSkillsVerification().getRejected())
                .requests(admin.getSkillsVerification().getRequests())
                .build();
        experts = admin.getExperts().stream().map(e -> modelMapper.map(e, AddUpdateExpertDto.class)).collect(Collectors.toList());
    }
}


