package telran.b7a.cv.service;

import java.util.List;
import java.util.Set;

import telran.b7a.cv.dto.CVDto;
import telran.b7a.cv.dto.CVSearchDto;
import telran.b7a.cv.dto.NewCVDto;

public interface CVService {

	CVDto addCV(NewCVDto newCV, String login);

	CVDto getCV(String cvId, String role);
	
	List<CVDto> getCVs(List<String> cvsId);

	CVDto updateCV(String cvId, NewCVDto newDataCV);

	void removeCV(String cvId, String login);

	CVDto anonymiseCV(String cvId, Set<String> anonymousFields);
	
	List<CVDto> getCVsByParamaters(CVSearchDto paramaters);
}
