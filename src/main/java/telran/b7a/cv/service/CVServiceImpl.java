package telran.b7a.cv.service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
import telran.b7a.cv.exceptions.WrongCityException;
import telran.b7a.cv.models.CV;
import telran.b7a.employeeAccountig.dao.EmployeeAcconutingMongoRepository;
import telran.b7a.employeeAccountig.dto.exceptions.EmployeeNotFoundException;
import telran.b7a.employeeAccountig.model.Employee;

@Service
public class CVServiceImpl<T> implements CVService {

	@Value("${API_KEY}")
	String API_KEY;
	@Value("${BASE_URL}")
	String BASE_URL;

	CVRepository cvRepository;
	EmployeeAcconutingMongoRepository employeeRepository;
	MongoTemplate mongoTemplate;
	ModelMapper modelMapper;

	@Autowired
	public CVServiceImpl(CVRepository cvRepository, ModelMapper modelMapper,
			EmployeeAcconutingMongoRepository employeeRepository, MongoTemplate mongoTemplate) {
		this.cvRepository = cvRepository;
		this.modelMapper = modelMapper;
		this.employeeRepository = employeeRepository;
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public CVDto addCV(NewCVDto newCV, String login) {
		CV cv = modelMapper.map(newCV, CV.class);
		Double[] coordinates = getCoordinatesByCity(newCV.getAddress());
		Double lon = coordinates[0];
		Double lat = coordinates[1];
		cv.setCoordinates(new Point(lon, lat));
		Employee employee = employeeRepository.findById(login).orElseThrow(() -> new EmployeeNotFoundException());
		String cvId = cvRepository.save(cv).getCvId().toHexString();
		employee.getCv_id().add(cvId);
		employeeRepository.save(employee);
		return modelMapper.map(cv, CVDto.class);
	}

	@Override
	public CVDto getCV(String cvId, String role) {
		CV cv = findCVbyId(cvId);
		CVDto response = modelMapper.map(cv, CVDto.class);
		Set<String> hideFields = cv.getHideFields();
		if (!hideFields.isEmpty() && role.equalsIgnoreCase("Role_Employer")) {
			response = setAnonymousFields(hideFields, response);
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
		response = setAnonymousFields(anonymousFields, response);
		return response;
	}

	@Override
	public List<CVDto> getCVs(List<String> cvsId) {
		return cvRepository.findBycvIdIn(cvsId).map(cv -> modelMapper.map(cv, CVDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<CVDto> getCVsByParamaters(CVSearchDto paramaters) {
		Query query = createQuery(paramaters);
		List<CV> cvs = mongoTemplate.find(query, CV.class);
		return cvs.stream().map(cv -> modelMapper.map(cv, CVDto.class)).collect(Collectors.toList());
	}

	private Query createQuery(CVSearchDto paramaters) {
		Query query = new Query();
		if (paramaters.getLocation() != null) {
			Double[] coordinates = getCoordinatesByCity(paramaters.getLocation());
			Double lon = coordinates[0];
			Double lat = coordinates[1];
			Point point = new Point(lon, lat);
			Distance distance = new Distance(paramaters.getDistance(), Metrics.KILOMETERS);
			query.addCriteria(
					Criteria.where("coordinates").nearSphere(point).maxDistance(distance.getNormalizedValue()));
		}
		if (paramaters.getPosition() != null) {
			query.addCriteria(Criteria.where("position").regex(paramaters.getPosition(), "i"));
		}
		if (paramaters.getSkills() != null) {
			query.addCriteria(Criteria.where("skills").all(paramaters.getSkills()));
		}
		if (paramaters.getMinSalary() != 0) {
			query.addCriteria(Criteria.where("salary").gte(paramaters.getMinSalary()).lt(paramaters.getMaxSalary()));
		}
		if (paramaters.getVerifiedLevel() != 0) {
			query.addCriteria(Criteria.where("verificationLevel").is(paramaters.getVerifiedLevel()));
		}
		if (paramaters.isRelocated()) {
			// TODO
		}
		return query;
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
		ResponseEntity<List> responseEntity = null;
		responseEntity = restTemplate.exchange(requestEntity, List.class);
		HashMap<String, Double> data;
		try {
			data = (HashMap<String, Double>) responseEntity.getBody().get(0);
		} catch (Exception e) {
			throw new WrongCityException(city);
		}
		Double[] coordinates = { data.get("lon"), data.get("lat") };
		return coordinates;
	}

	private CVDto setAnonymousFields(Set<String> anonymousFields, CVDto response) {
		Class<? extends CVDto> clazz = response.getClass();
		for (String fieldName : anonymousFields) {
			Field field = null;
			try {
				field = clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException | SecurityException e1) {
				e1.printStackTrace();
			}
			field.setAccessible(true);
			try {
				field.set(response, null);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return response;
	}

}
