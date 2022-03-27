package telran.b7a.admin.service;

import telran.b7a.admin.dto.AdminAddExpertResponseDto;
import telran.b7a.admin.dto.AdminRegisterDto;
import telran.b7a.admin.dto.AdminRegisterResponseDto;
import telran.b7a.admin.dto.AdminSignInResponseDto;
import telran.b7a.admin.dto.expert.AddUpdateExpertDto;

public interface AdminService {
    AdminRegisterResponseDto adminSignUp(AdminRegisterDto registerData);

    AdminSignInResponseDto adminSignIn(String login);

    AdminAddExpertResponseDto addExpert(AddUpdateExpertDto newExpert, String adminId);

    AdminSignInResponseDto updateExpert(AddUpdateExpertDto expert, String adminId);

    void deleteExpert(String expertId, String adminId);
}
