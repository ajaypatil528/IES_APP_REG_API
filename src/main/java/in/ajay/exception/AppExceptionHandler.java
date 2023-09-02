package in.ajay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {
	
	@ExceptionHandler(value = SsaWebException.class)
	public ResponseEntity<AppException> handleSsaWebException(SsaWebException ex){
		AppException appExcp = new AppException();
		appExcp.setExCode("EX0001");
		appExcp.setExDesc(ex.getMessage());
		return new ResponseEntity<AppException>(appExcp, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
