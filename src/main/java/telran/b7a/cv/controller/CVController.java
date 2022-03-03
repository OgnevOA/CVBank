package telran.b7a.cv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.b7a.cv.dto.AnonymiseCVDto;
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
	public CVDto addCV(@RequestBody NewCVDto newCV) {
		return cvService.addCV(newCV);
	}

	@GetMapping("/{cvId}")
	public CVDto getCV(@PathVariable String cvId) {
		return cvService.getCV(cvId);
	}

	@PutMapping("/update/{cvId}")
	public CVDto updateCV(@PathVariable String cvId, @RequestBody NewCVDto newDataCV) {
		return cvService.updateCV(cvId, newDataCV);
	}

	@DeleteMapping("/delete/{cvId}")
	public void removeCV(@PathVariable String cvId) {
		cvService.removeCV(cvId);
	}

	@PutMapping("/anonymise/{cvId}")
	public CVDto anonymiseCV(@PathVariable String cvId, @RequestBody AnonymiseCVDto anonymousFields) {
		return cvService.anonymiseCV(cvId, anonymousFields);
	}

}
