package telran.b7a.cv.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.b7a.cv.dto.CVDto;
import telran.b7a.cv.dto.NewCVDto;
import telran.b7a.cv.service.CVService;

@RestController
@RequestMapping("cvbank/employee/cv")
public class CVController {

	CVService cvService;

	@Autowired
	public CVController(CVService cvService) {
		this.cvService = cvService;
	}

	@PostMapping("/add")
	public CVDto addCV(@RequestBody NewCVDto newCV, Authentication authentication) {
		return cvService.addCV(newCV, authentication.getName());
	}

	@PostMapping("/cvs")
	public List<CVDto> getCVsByIDs(@RequestBody List<String> cvsId) {
		return cvService.getCVs(cvsId);

	}

	@GetMapping("/{cvId}")
	public CVDto getCV(@PathVariable String cvId) {
		return cvService.getCV(cvId);
	}

	@PutMapping("/anonymise/{cvId}")
	public CVDto anonymiseCV(@PathVariable String cvId, @RequestBody Set<String> anonymousFields) {
		return cvService.anonymiseCV(cvId, anonymousFields);
	}

	@PutMapping("/update/{cvId}")
	public CVDto updateCV(@PathVariable String cvId, @RequestBody NewCVDto newDataCV) {
		return cvService.updateCV(cvId, newDataCV);
	}

	@DeleteMapping("/delete/{cvId}")
	public void removeCV(@PathVariable String cvId, Authentication authentication) {
		cvService.removeCV(cvId, authentication.getName());
	}

}
