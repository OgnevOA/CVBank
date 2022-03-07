package telran.b7a.employer.service;

import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.b7a.cv.dao.CVRepository;
import telran.b7a.cv.exceptions.CVNotFoundException;
import telran.b7a.cv.models.CV;
import telran.b7a.employer.dao.EmployerMongoRepository;
import telran.b7a.employer.dto.AddCVDto;
import telran.b7a.employer.dto.EmployerDto;
import telran.b7a.employer.dto.NewEmployerDto;
import telran.b7a.employer.dto.UpdateEmployerDto;
import telran.b7a.employer.exceptions.EmployerExistException;
import telran.b7a.employer.exceptions.EmployerNotFoundException;
import telran.b7a.employer.models.Applicant;
import telran.b7a.employer.models.Company;
import telran.b7a.employer.models.Employer;

@Service
public class EmployerServiceImpl implements EmployerService {

	EmployerMongoRepository employersRepository;
	CVRepository cvRepository;
	ModelMapper modelMapper;

	@Autowired
	public EmployerServiceImpl(EmployerMongoRepository employerMongoRepository, ModelMapper modelMapper,
			CVRepository cvRepository) {
		this.employersRepository = employerMongoRepository;
		this.modelMapper = modelMapper;
		this.cvRepository = cvRepository;
	}

	@Override
	public EmployerDto addEmployer(NewEmployerDto newEmployer) {
		String companyName = newEmployer.getCompanyInfo().getName();
		if (employersRepository.existsByCompanyInfoNameIgnoreCase(companyName)) {
			throw new EmployerExistException(companyName);
		}
		Applicant newApllicant = modelMapper.map(newEmployer.getApplicantInfo(), Applicant.class);
		Company newCompany = modelMapper.map(newEmployer.getCompanyInfo(), Company.class);
		Employer employer = new Employer(newApllicant, newCompany,
				BCrypt.hashpw(newEmployer.getPassword(), BCrypt.gensalt()));
		employersRepository.save(employer);
		return modelMapper.map(employer, EmployerDto.class);
	}

	@Override
	public EmployerDto loginEmployer(String id) {
		String email = getLogin(id).orElse(null);
		if (!employersRepository.existsByApplicantInfoEmailIgnoreCase(email)) {
			throw new EmployerNotFoundException();
		}
		Employer employer = employersRepository.findByApplicantInfoEmailIgnoreCase(email);
		return modelMapper.map(employer, EmployerDto.class);
	}

	@Override
	public EmployerDto getEmployer(String companyName) {
		Employer employer = employersRepository.findByCompanyInfoNameIgnoreCase(companyName);
		if (employer == null) {
			throw new EmployerNotFoundException(companyName);
		}
		return modelMapper.map(employer, EmployerDto.class);
	}

	@Override
	public EmployerDto updateEmployer(String employerId, UpdateEmployerDto newCredentials) {
		Employer employer = employersRepository.findById(employerId).orElseThrow(() -> new EmployerNotFoundException());
		String email = employer.getApplicantInfo().getEmail();
		Applicant applicantInfo = modelMapper.map(newCredentials.getApplicantInfo(), Applicant.class);
		Company companyInfo = modelMapper.map(newCredentials.getCompanyInfo(), Company.class);
		employer.setApplicantInfo(applicantInfo);
		employer.getApplicantInfo().setEmail(email);
		employer.setCompanyInfo(companyInfo);
		employersRepository.save(employer);
		return modelMapper.map(employer, EmployerDto.class);
	}

	@Override
	public void removeEmployer(String employerId) {
		Employer employer = employersRepository.findById(employerId).orElseThrow(() -> new EmployerNotFoundException());
		employersRepository.delete(employer);
	}

	private Optional<String> getLogin(String token) {
		String login = null;
		try {
			token = token.split(" ")[1];
			byte[] bytesDecode = Base64.getDecoder().decode(token);
			token = new String(bytesDecode);
			login = token.split(":")[0];

		} catch (Exception e) {
			e.printStackTrace();

		}
		return Optional.ofNullable(login);
	}

	@Override
	public AddCVDto addCVCollection(String employerId, String collectionName) {
		Employer employer = employersRepository.findById(employerId).orElseThrow(() -> new EmployerNotFoundException());
		String login = employer.getApplicantInfo().getEmail();
		employer.getCvCollections().put(collectionName, new HashSet<>());
		employersRepository.save(employer);
		AddCVDto res = modelMapper.map(employer, AddCVDto.class);
		res.setLogin(login);
		return res;
	}

	@Override
	public AddCVDto addCVtoCollection(String employerId, String collectionName, String cvId) {
		CV cv = cvRepository.findById(cvId).orElseThrow(() -> new CVNotFoundException());
		Employer employer = employersRepository.findById(employerId).orElseThrow(() -> new EmployerNotFoundException());
		String login = employer.getApplicantInfo().getEmail();
		employer.getCvCollections().get(collectionName).add(cv.getCvId());
		employersRepository.save(employer);
		AddCVDto res = modelMapper.map(employer, AddCVDto.class);
		res.setLogin(login);
		return res;
	}

}
