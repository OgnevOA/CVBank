package telran.b7a.cv.service;

import java.util.List;

import telran.b7a.cv.dto.AnonymiseCVDto;
import telran.b7a.cv.dto.CVDto;
import telran.b7a.cv.dto.NewCVDto;

public interface CVService {

	CVDto addCV(NewCVDto newCV);

	CVDto getCV(String cvId);
	
	List<CVDto> getCVs(List<String> cvsId);

	CVDto updateCV(String cvId, NewCVDto newDataCV);

	void removeCV(String cvId);

	CVDto anonymiseCV(String cvId, AnonymiseCVDto anonymousFields);
}
