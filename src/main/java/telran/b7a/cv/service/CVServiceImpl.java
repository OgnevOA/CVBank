package telran.b7a.cv.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import telran.b7a.cv.dao.CVRepository;
import telran.b7a.cv.dto.CVDto;
import telran.b7a.cv.dto.CVSearchDto;
import telran.b7a.cv.dto.NewCVDto;
import telran.b7a.cv.dto.exceptions.CVNotFoundException;
import telran.b7a.cv.models.CV;
import telran.b7a.employee.dao.EmployeeMongoRepository;
import telran.b7a.employee.dto.exceptions.EmployeeNotFoundException;
import telran.b7a.employee.model.Employee;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CVServiceImpl implements CVService {


    CVRepository cvRepository;
    EmployeeMongoRepository employeeRepository;
    WeatherService weatherService;
    MongoTemplate mongoTemplate;
    ModelMapper modelMapper;

    @Autowired
    public CVServiceImpl(CVRepository cvRepository,
                         ModelMapper modelMapper,
                         EmployeeMongoRepository employeeRepository,
                         WeatherService weatherService, MongoTemplate mongoTemplate) {
        this.cvRepository = cvRepository;
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
        this.weatherService = weatherService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public CVDto addCV(NewCVDto newCV, String login) {
        CV cv = modelMapper.map(newCV, CV.class);
        Double[] coordinates = weatherService.getCoordinatesByCity(newCV.getAddress());
        Double lon = coordinates[0];
        Double lat = coordinates[1];
        cv.setCoordinates(new Point(lon, lat));
        Employee employee = employeeRepository.findById(login).orElseThrow(EmployeeNotFoundException::new);
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
    public CVDto publishCV(String cvId) {
        CV cv = findCVbyId(cvId);
        cv.setPublished(true);
        cv.setDatePublished(LocalDate.now().plusDays(14));
        cvRepository.save(cv);
        return modelMapper.map(cv, CVDto.class);
    }

    @Override
    public void removeCV(String cvId, String login) {
        CV cv = findCVbyId(cvId);
        Employee employee = employeeRepository.findById(login).orElseThrow(EmployeeNotFoundException::new);
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
    public List<CVDto> getCVs(Collection<String> cvsId) {
        return cvRepository.findBycvIdIn(cvsId)
                .map(cv -> modelMapper.map(cv, CVDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CVDto> getPublishedCVs() {
        return cvRepository.findByisPublishedTrue()
                .map(cv -> modelMapper.map(cv, CVDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CVDto> getPublishedCVsDateExpired() {
        LocalDate date = LocalDate.now();
        return cvRepository.findBydatePublished(date)
                .map(cv -> modelMapper.map(cv, CVDto.class))
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
            Double[] coordinates = weatherService.getCoordinatesByCity(paramaters.getLocation());
            Double lon = coordinates[0];
            Double lat = coordinates[1];
            Point point = new Point(lon, lat);
            Distance distance = new Distance(paramaters.getDistance(), Metrics.KILOMETERS);
            query.addCriteria(Criteria.where("coordinates").nearSphere(point).maxDistance(distance.getNormalizedValue()));
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
            query.addCriteria(Criteria.where("isRelocated").ne(null));
        }
        return query;
    }

    private CV findCVbyId(String cvId) {
        return cvRepository.findById(cvId).orElseThrow(CVNotFoundException::new);
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
