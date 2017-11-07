package ch.ese.team6.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlingController {

	@ResponseStatus(HttpStatus.FORBIDDEN)
	public String denyAccess() {
		return "error/deny";
	}
}
