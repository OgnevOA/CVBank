package telran.b7a.cv.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
@NoArgsConstructor
public class WrongCityException extends RuntimeException {/**
	 * 
	 */
	private static final long serialVersionUID = -6878819084308767014L;
	public WrongCityException(String city) {
		super("City " + city + " not exist");
	}
}
