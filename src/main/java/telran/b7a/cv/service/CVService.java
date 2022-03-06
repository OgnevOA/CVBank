package telran.b7a.cv.service;

import telran.b7a.cv.dto.AnonymiseCVDto;
import telran.b7a.cv.dto.CVDto;
import telran.b7a.cv.dto.NewCVDto;

public interface CVService {

	CVDto addCV(NewCVDto newCV);

	CVDto getCV(String cvId);

	CVDto updateCV(String cvId, NewCVDto newDataCV);

	void removeCV(String cvId);

	CVDto anonymiseCV(String cvId, AnonymiseCVDto anonymousFields);
}
