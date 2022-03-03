package telran.b7a.employer.service;

import java.util.Base64;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.b7a.employer.dao.EmployerMongoRepository;
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
	ModelMapper modelMapper;

	@Autowired
	public EmployerServiceImpl(EmployerMongoRepository employerMongoRepository, ModelMapper modelMapper) {
		this.employersRepository = employerMongoRepository;
		this.modelMapper = modelMapper;
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
	public EmployerDto loginEmployer(String token) {
		String email = getLogin(token).orElse(null);
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
	public EmployerDto updateEmployer(String email, UpdateEmployerDto newCredentials) {
		Employer employer = employersRepository.findByApplicantInfoEmailIgnoreCase(email);
		if (employer == null) {
			throw new EmployerNotFoundException();
		}
		Applicant applicantInfo = modelMapper.map(newCredentials.getApplicantInfo(), Applicant.class);
		Company companyInfo = modelMapper.map(newCredentials.getCompanyInfo(), Company.class);
		employer.setApplicantInfo(applicantInfo);
		employer.getApplicantInfo().setEmail(email);
		employer.setCompanyInfo(companyInfo);
		employersRepository.save(employer);
		return modelMapper.map(employer, EmployerDto.class);
	}

	@Override
	public void removeEmployer(String email) {
		Employer employer = employersRepository.findByApplicantInfoEmailIgnoreCase(email);
		if (employer == null) {
			throw new EmployerNotFoundException();
		}
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

}
