package telran.b7a.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import telran.b7a.employer.exceptions.EmployerExistException;
import telran.b7a.employer.exceptions.EmployerNotFoundException;
import telran.b7a.employer.exceptions.LoginAlreadyUsedException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(EmployerExistException.class)
    public ResponseEntity<Object> handleEmployerExistException() {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Employer already exist");
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmployerNotFoundException.class)
    public ResponseEntity<Object> handleEmployerNotFoundException() {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Employer not found");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoginAlreadyUsedException.class)
    public ResponseEntity<Object> handleLoginAlreadyUsedException() {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Login already use");
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleNotValidEmailException(MethodArgumentNotValidException exception) {
        Map<String, Object> body = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(e -> {
            String fieldName = e.getField();
            String errorMessage = e.getDefaultMessage();
            body.put(fieldName, errorMessage);
        });
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
    }
}
