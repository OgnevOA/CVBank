package telran.b7a.cv.service;

import java.util.List;
import java.util.Set;

import telran.b7a.cv.dto.CVDto;
import telran.b7a.cv.dto.NewCVDto;

public interface CVService {

	CVDto addCV(NewCVDto newCV);

	CVDto getCV(String cvId);
	
	List<CVDto> getCVs(List<String> cvsId);

	CVDto updateCV(String cvId, NewCVDto newDataCV);

	void removeCV(String cvId);

	CVDto anonymiseCV(String cvId, Set<String> anonymousFields);
}
