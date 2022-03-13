package telran.b7a.cv.service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import telran.b7a.cv.dao.CVRepository;
import telran.b7a.cv.dto.CVDto;
import telran.b7a.cv.dto.CVSearchDto;
import telran.b7a.cv.dto.NewCVDto;
import telran.b7a.cv.exceptions.CVNotFoundException;
import telran.b7a.cv.models.CV;
import telran.b7a.employeeAccountig.dao.EmployeeAcconutingMongoRepository;
import telran.b7a.employeeAccountig.dto.exceptions.EmployeeNotFoundException;
import telran.b7a.employeeAccountig.model.Employee;

@Service
public class CVServiceImpl implements CVService {

	@Value("${API_KEY}")
	String API_KEY;
	@Value("${BASE_URL}")
	String BASE_URL;

	CVRepository cvRepository;
	EmployeeAcconutingMongoRepository employeeRepository;
	ModelMapper modelMapper;

	@Autowired
	public CVServiceImpl(CVRepository cvRepository, ModelMapper modelMapper,
			EmployeeAcconutingMongoRepository employeeRepository) {
		this.cvRepository = cvRepository;
		this.modelMapper = modelMapper;
		this.employeeRepository = employeeRepository;
	}

	@Override
	public CVDto addCV(NewCVDto newCV, String login) {
		CV cv = modelMapper.map(newCV, CV.class);
		Double[] coordinates = getCoordinatesByCity(newCV.getLocation());
		Double lon = coordinates[0];
		Double lat = coordinates[1];
		cv.setCoordinates(new Point(lon, lat));
		Employee employee = employeeRepository.findById(login).orElseThrow(() -> new EmployeeNotFoundException());
		ObjectId cvId = cvRepository.save(cv).getCvId();
		employee.getCv_id().add(cvId.toHexString());
		employeeRepository.save(employee);
		return modelMapper.map(cv, CVDto.class);
	}

	@Override
	public CVDto getCV(String cvId, String role) {
		CV cv = findCVbyId(cvId);
		CVDto response = modelMapper.map(cv, CVDto.class);
		Set<String> hideFields = cv.getHideFields();
		if (!hideFields.isEmpty() && role.equalsIgnoreCase("Role_Employer")) {
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
	public void removeCV(String cvId, String login) {
		CV cv = findCVbyId(cvId);
		Employee employee = employeeRepository.findById(login).orElseThrow(() -> new EmployeeNotFoundException());
		employee.getCv_id().remove(cvId);
		employeeRepository.save(employee);
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

	@Override
	public List<CVDto> getCVsByParamaters(CVSearchDto paramaters) {
		Double[] coordinates = getCoordinatesByCity(paramaters.getLocation());
		Point location = new Point(coordinates[0], coordinates[1]);
		Distance radius = new Distance(paramaters.getDistance(), Metrics.KILOMETERS);
		return cvRepository.findByCoordinatesNear(location, radius)
				.filter(cv -> cv.getPosition().contains(paramaters.getPosition()))
				.filter(cv -> cv.isRelocated() == paramaters.isReadyRelocate())
				.filter(cv -> cv.getSkills().containsAll(paramaters.getSkills()))
				.filter(cv -> cv.getOther().getSalaryExpectations() >= paramaters.getSalaryFrom()
						&& cv.getOther().getSalaryExpectations() < paramaters.getSalaryTo())
				.map(cv -> modelMapper.map(cv, CVDto.class)).collect(Collectors.toList());
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Double[] getCoordinatesByCity(String city) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL).queryParam("q", city)
				.queryParam("appid", API_KEY);
		RestTemplate restTemplate = new RestTemplate();
		RequestEntity<String> requestEntity = new RequestEntity<>(HttpMethod.GET, builder.build().toUri());
		ResponseEntity<List> responseEntity = restTemplate.exchange(requestEntity, List.class);
		HashMap<String, Double> data = (HashMap<String, Double>) responseEntity.getBody().get(0);
		Double[] coordinates = { data.get("lon"), data.get("lat") };
		return coordinates;
	}

}
