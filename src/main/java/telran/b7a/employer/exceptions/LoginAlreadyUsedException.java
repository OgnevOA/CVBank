package telran.b7a.employer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@ResponseStatus(code = HttpStatus.CONFLICT)
@NoArgsConstructor
public class LoginAlreadyUsedException extends RuntimeException {/**
	 * 
	 */
	private static final long serialVersionUID = -6878819084308767014L;
	public LoginAlreadyUsedException(String newLogin) {
		super("Login " + newLogin + " already used");
	}
}
