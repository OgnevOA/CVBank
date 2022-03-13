package telran.b7a.employer.service;

import java.util.HashSet;

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
	public EmployerDto loginEmployer(String login) {
		Employer employer = employersRepository.findByApplicantInfoEmailIgnoreCase(login);
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
		Employer employer = findEmployerById(employerId);
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
		Employer employer = findEmployerById(employerId);
		employersRepository.delete(employer);
	}

	@Override
	public AddCVDto addCvCollection(String employerId, String collectionName) {
		Employer employer = findEmployerById(employerId);
		String login = employer.getApplicantInfo().getEmail();
		employer.getCvCollections().put(collectionName, new HashSet<>());
		employersRepository.save(employer);
		AddCVDto res = modelMapper.map(employer, AddCVDto.class);
		res.setLogin(login);
		return res;
	}

	@Override
	public void removeCvCollection(String employerId, String collectionName) {
		Employer employer = findEmployerById(employerId);
		employer.getCvCollections().remove(collectionName);
		employersRepository.save(employer);
	}

	@Override
	public AddCVDto addCvToCollection(String employerId, String collectionName, String cvId) {
		CV cv = cvRepository.findById(cvId).orElseThrow(() -> new CVNotFoundException());
		Employer employer = findEmployerById(employerId);
		String login = employer.getApplicantInfo().getEmail();
		if (employer.getCvCollections().get(collectionName) == null) {
			employer.getCvCollections().put(collectionName, new HashSet<>());
		}
		employer.getCvCollections().get(collectionName).add(cv.getCvId().toHexString());
		employersRepository.save(employer);
		AddCVDto res = modelMapper.map(employer, AddCVDto.class);
		res.setLogin(login);
		return res;
	}

	@Override
	public void removeCvFromCollection(String employerId, String collectionName, String cvId) {
		CV cv = cvRepository.findById(cvId).orElseThrow(() -> new CVNotFoundException());
		Employer employer = findEmployerById(employerId);
		employer.getCvCollections().get(collectionName).remove(cv.getCvId().toHexString());
		employersRepository.save(employer);
	}

	private Employer findEmployerById(String employerId) {
		return employersRepository.findById(employerId).orElseThrow(() -> new EmployerNotFoundException());
	}

}
