package telran.b7a.cv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import telran.b7a.cv.dto.CVDto;
import telran.b7a.cv.dto.CVSearchDto;
import telran.b7a.cv.dto.NewCVDto;
import telran.b7a.cv.service.CVService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController

@RequestMapping("cvbank/cv")
@CrossOrigin(origins = "*",
        methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.POST, RequestMethod.PUT},
        allowedHeaders = "*", exposedHeaders = "*")
public class CVController {

    CVService cvService;

    @Autowired
    public CVController(CVService cvService) {
        this.cvService = cvService;
    }

    @PostMapping
    public CVDto addCV(@RequestBody NewCVDto newCV, Authentication authentication) {
        return cvService.addCV(newCV, authentication.getName());
    }

    @PostMapping("/cvs")
    public List<CVDto> getCVsByIDs(@RequestBody Collection<String> cvsId) {
        return cvService.getCVs(cvsId);

    }

    @PostMapping("/cvs/aggregate")
    public List<CVDto> getCVsByParameters(@RequestBody CVSearchDto paramaters) {
        return cvService.getCVsByParamaters(paramaters);
    }

    @GetMapping("/{cvId}")
    public CVDto getCV(@PathVariable String cvId, Authentication authentication) {
        String role = authentication.getAuthorities().stream().findFirst().orElse(null).getAuthority();
        return cvService.getCV(cvId, role);
    }

    @GetMapping("/cvs/published")
    public List<CVDto> getPublishedCVs() {
        return cvService.getPublishedCVs();
    }

    @GetMapping("/cvs/publishExpired")
    public List<CVDto> getPublishExpired() {
        return cvService.getPublishedCVsDateExpired();
    }

    @PutMapping("/anonymizer/{cvId}")
    public CVDto anonymiseCV(@PathVariable String cvId, @RequestBody Set<String> anonymousFields) {
        return cvService.anonymiseCV(cvId, anonymousFields);
    }

    @PutMapping("/{cvId}")
    public CVDto updateCV(@PathVariable String cvId, @RequestBody NewCVDto newDataCV) {
        return cvService.updateCV(cvId, newDataCV);
    }

    @PutMapping("/publish/{cvId}")
    public CVDto publishCV(@PathVariable String cvId) {
        return cvService.publishCV(cvId);
    }

    @DeleteMapping("/{cvId}")
    public void removeCV(@PathVariable String cvId, Authentication authentication) {
        cvService.removeCV(cvId, authentication.getName());
    }

}
