package telran.b7a.cv.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.b7a.cv.dao.CVRepository;
import telran.b7a.cv.dto.AnonymiseCVDto;
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
		CV cv = cvRepository.findById(cvId).orElseThrow(() -> new CVNotFoundException(cvId));
		return modelMapper.map(cv, CVDto.class);
	}

	@Override
	public CVDto updateCV(String cvId, NewCVDto newDataCV) {
		CV cv = cvRepository.findById(cvId).orElseThrow(() -> new CVNotFoundException(cvId));
		CV cvNew = modelMapper.map(newDataCV, CV.class);
		cvNew.setCvId(cv.getCvId());
		cvRepository.save(cvNew);
		return modelMapper.map(cvNew, CVDto.class);
	}

	@Override
	public void removeCV(String cvId) {
		CV cv = cvRepository.findById(cvId).orElseThrow(() -> new CVNotFoundException(cvId));
		cvRepository.delete(cv);

	}

	@Override
	public CVDto anonymiseCV(String cvId, AnonymiseCVDto anonymousFields) {
		CV cv = cvRepository.findById(cvId).orElseThrow(() -> new CVNotFoundException(cvId));
		CVDto response = modelMapper.map(cv, CVDto.class);
		for (String field : anonymousFields.getHideFields()) {
			response = setNull(response, field);
		}
		return response;
	}

	private CVDto setNull(CVDto response, String field) {
		switch (field) {
		case "Name and last name":
			response.setFirstName(null);
			response.setLastName(null);
			return response;
		case "phone":
			response.setPhone(null);
		case "links":
			response.setLinks(null);
		case "experience":
			response.setExperience(null);
		default:
			return response;
		}

	}

}
