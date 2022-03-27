package telran.b7a.admin.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import telran.b7a.admin.dao.AdminMongoRepository;
import telran.b7a.admin.dao.ExpertMongoRepository;
import telran.b7a.admin.dto.*;
import telran.b7a.admin.dto.exceptions.AdminAlreadyExistException;
import telran.b7a.admin.dto.exceptions.AdminNotFoundException;
import telran.b7a.admin.dto.expert.AddUpdateExpertDto;
import telran.b7a.admin.model.Admin;
import telran.b7a.admin.model.Expert;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    AdminMongoRepository adminRepo;
    ExpertMongoRepository expertRepo;
    ModelMapper modelMapper;
    PasswordEncoder passwordEncoder;

    @Autowired
    public AdminServiceImpl(AdminMongoRepository adminRepo, ExpertMongoRepository expertRepo, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.adminRepo = adminRepo;
        this.expertRepo = expertRepo;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public AdminRegisterResponseDto adminSignUp(AdminRegisterDto registerData) {
        if (adminRepo.existsById(registerData.getLogin())) {
            throw new AdminAlreadyExistException();
        }
        Admin admin = modelMapper.map(registerData, Admin.class);
        String password = passwordEncoder.encode(registerData.getPassword());
        admin.setPassword(password);
        adminRepo.save(admin);
        return modelMapper.map(admin, AdminRegisterResponseDto.class);
    }

    @Override
    public AdminSignInResponseDto adminSignIn(String login) {
        Admin admin = adminRepo.findById(login).orElseThrow(AdminNotFoundException::new);
        return new AdminSignInResponseDto(admin);
    }

    @Override
    public AdminAddExpertResponseDto addExpert(AddUpdateExpertDto newExpert, String adminId) {
        Admin admin = adminRepo.findById(adminId).orElseThrow(AdminNotFoundException::new);
        Expert expert = modelMapper.map(newExpert, Expert.class);
        expertRepo.save(expert);
        admin.getExperts().add(expert);
        adminRepo.save(admin);
        List<AddUpdateExpertDto> experts = admin.getExperts().stream()
                .map(e -> modelMapper.map(e, AddUpdateExpertDto.class))
                .collect(Collectors.toList());
        return AdminAddExpertResponseDto.builder()
                .login(admin.getLogin())
                .experts(experts)
                .build();
    }

    @Override
    public AdminSignInResponseDto updateExpert(AddUpdateExpertDto expertDto, String adminId) {
        Admin admin = adminRepo.findById(adminId).orElseThrow(AdminNotFoundException::new);
        Expert expert = modelMapper.map(expertDto, Expert.class);
        admin.getExperts().remove(expert);
        admin.getExperts().add(expert);
        adminRepo.save(admin);
        return new AdminSignInResponseDto(admin);
    }

    @Override
    public void deleteExpert(String expertId, String adminId) {
        Admin admin = adminRepo.findById(adminId).orElseThrow(AdminNotFoundException::new);
        admin.setExperts(admin.getExperts().stream()
                .filter(e -> !e.getEmail().equals(expertId))
                .collect(Collectors.toSet()));
        adminRepo.save(admin);
    }
}
