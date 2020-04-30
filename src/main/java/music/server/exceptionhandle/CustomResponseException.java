package music.server.exceptionhandle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ControllerAdvice
@RestController
public class CustomResponseException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<HashMap<String, String>> message = convertToMessage(ex.getBindingResult().getFieldErrors());

        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), message);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameIsAlreadyTakenException.class)
    public final ResponseEntity<Object> handleUsernameIsAlreadyTakenException(UsernameIsAlreadyTakenException ex,
            WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<Object> handleUnauthorizedException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized. Username/password incorrected.");
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public final ResponseEntity<Object> handleInternalAuthenticationServiceException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized. Username/password incorrected.");
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    public List<HashMap<String, String>> convertToMessage(List<FieldError> list) {
        List<HashMap<String, String>> message = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            HashMap<String, String> tempMap = new HashMap<String, String>();
            tempMap.put("key", list.get(i).getField());
            tempMap.put("value", list.get(i).getDefaultMessage());
            message.add(tempMap);
        }
        return message;
    }

    @ExceptionHandler(ApiMissingException.class)
    public final ResponseEntity<Object> handleApiMissingException(ApiMissingException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public final ResponseEntity<Object> handlePasswordNotMatchException(PasswordNotMatchException ex,
            WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}