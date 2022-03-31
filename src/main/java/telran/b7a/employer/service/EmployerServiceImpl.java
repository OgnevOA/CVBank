package telran.b7a.employer.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import telran.b7a.cv.dao.CVRepository;
import telran.b7a.cv.dto.exceptions.CVNotFoundException;
import telran.b7a.cv.models.CV;
import telran.b7a.employer.dao.EmployerMongoRepository;
import telran.b7a.employer.dto.AddCVDto;
import telran.b7a.employer.dto.EmployerDto;
import telran.b7a.employer.dto.NewEmployerDto;
import telran.b7a.employer.dto.UpdateEmployerDto;
import telran.b7a.employer.dto.exceptions.EmployerExistException;
import telran.b7a.employer.dto.exceptions.EmployerNotFoundException;
import telran.b7a.employer.dto.exceptions.LoginAlreadyUsedException;
import telran.b7a.employer.models.Applicant;
import telran.b7a.employer.models.Company;
import telran.b7a.employer.models.Employer;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployerServiceImpl implements EmployerService {

    EmployerMongoRepository employersRepository;
    PasswordEncoder passwordEncoder;
    CVRepository cvRepository;
    ModelMapper modelMapper;

    @Autowired
    public EmployerServiceImpl(EmployerMongoRepository employerMongoRepository, ModelMapper modelMapper,
                               CVRepository cvRepository, PasswordEncoder passwordEncoder) {
        this.employersRepository = employerMongoRepository;
        this.modelMapper = modelMapper;
        this.cvRepository = cvRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public EmployerDto addEmployer(NewEmployerDto newEmployer) {
        if (employersRepository.existsById(newEmployer.getEmail())) {
            throw new EmployerExistException();
        }
        Applicant newApplicant = modelMapper.map(newEmployer.getApplicantInfo(), Applicant.class);
        Company newCompany = modelMapper.map(newEmployer.getCompanyInfo(), Company.class);
        String password = passwordEncoder.encode(newEmployer.getPassword());
        Employer employer = new Employer(newEmployer.getEmail(), newApplicant, newCompany, password);
        employersRepository.save(employer);
        return modelMapper.map(employer, EmployerDto.class);
    }

    @Override
    @ExceptionHandler(value = EmployerNotFoundException.class)
    public EmployerDto loginEmployer(String email) {
        Employer employer = findEmployerById(email);
        return modelMapper.map(employer, EmployerDto.class);
    }

    @Override
    public List<EmployerDto> getEmployerByName(String companyName) {
        List<Employer> employers = employersRepository.findByCompanyInfoNameIgnoreCase(companyName)
                .collect(Collectors.toList());
        if (employers.isEmpty()) {
            throw new EmployerNotFoundException();
        }
        return employers.stream().
                map(e -> modelMapper.map(e, EmployerDto.class)).
                collect(Collectors.toList());
    }

    @Override
    @ExceptionHandler(value = EmployerNotFoundException.class)
    public EmployerDto getEmployerById(String email) {
        Employer employer = findEmployerById(email);
        return modelMapper.map(employer, EmployerDto.class);
    }

    @Override
    @ExceptionHandler(value = EmployerNotFoundException.class)
    public EmployerDto updateEmployer(String email, UpdateEmployerDto newCredentials) {
        Employer employer = findEmployerById(email);
        Applicant applicantInfo = modelMapper.map(newCredentials.getApplicantInfo(), Applicant.class);
        Company companyInfo = modelMapper.map(newCredentials.getCompanyInfo(), Company.class);
        employer.setApplicantInfo(applicantInfo);
        employer.setCompanyInfo(companyInfo);
        employersRepository.save(employer);
        return modelMapper.map(employer, EmployerDto.class);
    }

    @Override
    @ExceptionHandler(value = {EmployerNotFoundException.class})
    public EmployerDto changeLogin(String email, String newLogin) {
        if (employersRepository.existsById(newLogin)) {
            throw new LoginAlreadyUsedException();
        }
        Employer employer = findEmployerById(email);
        employersRepository.delete(employer);
        employer.setEmail(newLogin);
        employersRepository.save(employer);
        return modelMapper.map(employer, EmployerDto.class);
    }

    @Override
    @ExceptionHandler(value = EmployerNotFoundException.class)
    public EmployerDto changePassword(String email, String newPassword) {
        Employer employer = findEmployerById(email);
        newPassword = passwordEncoder.encode(newPassword);
        employer.setPassword(newPassword);
        employersRepository.save(employer);
        return modelMapper.map(employer, EmployerDto.class);
    }

    @Override
    @ExceptionHandler(value = EmployerNotFoundException.class)
    public AddCVDto addCvCollection(String employerId, String collectionName) {
        Employer employer = findEmployerById(employerId);
        employer.getCvCollections().put(collectionName, new HashSet<>());
        employersRepository.save(employer);
        return modelMapper.map(employer, AddCVDto.class);
    }

    @Override
    public AddCVDto addCvToCollection(String employerId, String collectionName, String cvId) {
        CV cv = cvRepository.findById(cvId).orElseThrow(CVNotFoundException::new);
        Employer employer = findEmployerById(employerId);
        employer.getCvCollections().computeIfAbsent(collectionName, k -> new HashSet<>());
        employer.getCvCollections().get(collectionName).add(cv.getCvId().toHexString());
        employersRepository.save(employer);
        return modelMapper.map(employer, AddCVDto.class);
    }

    @Override
    @ExceptionHandler(value = EmployerNotFoundException.class)
    public void removeEmployer(String employerId) {
        Employer employer = findEmployerById(employerId);
        employersRepository.delete(employer);
    }

    @Override
    @ExceptionHandler(value = EmployerNotFoundException.class)
    public void removeCvCollection(String employerId, String collectionName) {
        Employer employer = findEmployerById(employerId);
        employer.getCvCollections().remove(collectionName);
        employersRepository.save(employer);
    }

    @Override
    @ExceptionHandler(value = {EmployerNotFoundException.class, CVNotFoundException.class})
    public void removeCvFromCollection(String employerId, String collectionName, String cvId) {
        CV cv = cvRepository.findById(cvId).orElse(null);
        Employer employer = findEmployerById(employerId);
        employer.getCvCollections().get(collectionName).remove(cv.getCvId().toHexString());
        employersRepository.save(employer);
    }

    private Employer findEmployerById(String employerId) {
        return employersRepository.findById(employerId).orElse(null);
    }
}