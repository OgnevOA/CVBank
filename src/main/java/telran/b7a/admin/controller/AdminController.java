package telran.b7a.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import telran.b7a.admin.dto.AdminAddExpertResponseDto;
import telran.b7a.admin.dto.AdminRegisterDto;
import telran.b7a.admin.dto.AdminRegisterResponseDto;
import telran.b7a.admin.dto.AdminSignInResponseDto;
import telran.b7a.admin.dto.expert.AddUpdateExpertDto;
import telran.b7a.admin.service.AdminService;

@RestController
@RequestMapping("/cvbank/admin")
@CrossOrigin(origins = "*",
        methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.PUT},
        allowedHeaders = "*", exposedHeaders = "*")
public class AdminController {
    AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/signup")
    public AdminRegisterResponseDto adminRegister(@RequestBody AdminRegisterDto registerData) {
        return adminService.adminSignUp(registerData);
    }

    @PostMapping("/signin")
    public AdminSignInResponseDto adminLogin(Authentication authentication) {
        return adminService.adminSignIn(authentication.getName());
    }

    @PostMapping("/expert")
    public AdminAddExpertResponseDto addExpert(@RequestBody AddUpdateExpertDto newExpert, Authentication authentication) {
        return adminService.addExpert(newExpert, authentication.getName());
    }

    @PutMapping("/expert")
    public AdminSignInResponseDto updateExpert(@RequestBody AddUpdateExpertDto expert, Authentication authentication) {
        return adminService.updateExpert(expert, authentication.getName());
    }

    @DeleteMapping("/expert/{expertId}")
    public void deleteExpert(@PathVariable String expertId, Authentication authentication) {
        adminService.deleteExpert(expertId, authentication.getName());
    }
}
