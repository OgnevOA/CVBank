package telran.b7a.cv.service;

import telran.b7a.cv.dto.CVDto;
import telran.b7a.cv.dto.CVSearchDto;
import telran.b7a.cv.dto.NewCVDto;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CVService {

    CVDto addCV(NewCVDto newCV, String login);

    CVDto getCV(String cvId, String role);

    List<CVDto> getCVs(Collection<String> cvsId);

    List<CVDto> getPublishedCVs();

    List<CVDto> getPublishedCVsDateExpired();

    CVDto updateCV(String cvId, NewCVDto newDataCV);

    CVDto publishCV(String cvId);

    void removeCV(String cvId, String login);

    CVDto anonymiseCV(String cvId, Set<String> anonymousFields);

    List<CVDto> getCVsByParamaters(CVSearchDto paramaters);
}
