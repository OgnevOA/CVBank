package telran.b7a.employer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
@NoArgsConstructor
public class EmployerNotFoundException extends RuntimeException {/**
	 * 
	 */
	private static final long serialVersionUID = 5635370307230854544L;
	public EmployerNotFoundException(String companyName) {
		super("Company " + companyName + " not exist");
	}


}
