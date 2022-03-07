package telran.b7a.cv.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.b7a.cv.dao.CVRepository;
import telran.b7a.cv.dto.CVDto;
import telran.b7a.cv.dto.NewCVDto;
import telran.b7a.cv.exceptions.CVNotFoundException;
import telran.b7a.cv.models.CV;

@Service
public class CVServiceImpl implements CVService {

	CVRepository cvRepository;
	ModelMapper modelMapper;

	@Autowired
	public CVServiceImpl(CVRepository cvRepository, ModelMapper modelMapper) {
		this.cvRepository = cvRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public CVDto addCV(NewCVDto newCV) {
		CV cv = modelMapper.map(newCV, CV.class);
		cvRepository.save(cv);
		return modelMapper.map(cv, CVDto.class);
	}

	@Override
	public CVDto getCV(String cvId) {
		CV cv = findCVbyId(cvId);
		CVDto response = modelMapper.map(cv, CVDto.class);
		Set<String> hideFields = cv.getHideFields();
		if (!hideFields.isEmpty()) {
			for (String field : hideFields) {
				response = setNull(response, field);
			}
		}
		return response;
	}

	@Override
	public CVDto updateCV(String cvId, NewCVDto newDataCV) {
		CV cv = findCVbyId(cvId);
		CV cvNew = modelMapper.map(newDataCV, CV.class);
		cvNew.setCvId(cv.getCvId());
		cvRepository.save(cvNew);
		return modelMapper.map(cvNew, CVDto.class);
	}

	@Override
	public void removeCV(String cvId) {
		CV cv = findCVbyId(cvId);
		cvRepository.delete(cv);

	}

	@Override
	public CVDto anonymiseCV(String cvId, Set<String> anonymousFields) {
		CV cv = findCVbyId(cvId);
		cv.setHideFields(anonymousFields);
		cvRepository.save(cv);
		CVDto response = modelMapper.map(cv, CVDto.class);
		for (String field : anonymousFields) {
			response = setNull(response, field);
		}
		return response;
	}
	
	@Override
	public List<CVDto> getCVs(List<String> cvsId) {
		return cvRepository.findBycvIdIn(cvsId).map(cv -> modelMapper.map(cv, CVDto.class))
				.collect(Collectors.toList());
	}

	private CVDto setNull(CVDto response, String field) {
		if (field.equalsIgnoreCase("firstName")) {
			response.setFirstName(null);
			return response;
		}
		if (field.equalsIgnoreCase("lastName")) {
			response.setLastName(null);
			return response;
		}
		
		if (field.equalsIgnoreCase("phone")) {
			response.setPhone(null);
			return response;
		}
		if (field.equalsIgnoreCase("links")) {
			response.setLinks(null);
			return response;
		}
		if (field.equalsIgnoreCase("experience")) {
			response.setExperience(null);
			return response;
		}
		return response;

	}

	private CV findCVbyId(String cvId) {
		return cvRepository.findById(cvId).orElseThrow(() -> new CVNotFoundException(cvId));
	}
}
