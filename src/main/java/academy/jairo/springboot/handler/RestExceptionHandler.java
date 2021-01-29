package academy.jairo.springboot.handler;

import academy.jairo.springboot.exception.AppBadRequestException;
import academy.jairo.springboot.exception.AppBadRequestExceptionDetails;
import academy.jairo.springboot.exception.AppExceptionDetails;
import academy.jairo.springboot.exception.AppValidationExceptionDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Consistent Exception Pattern
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AppBadRequestException.class)
    public ResponseEntity<AppBadRequestExceptionDetails> handleAppBadRequestException(
            AppBadRequestException ex) {
        return new ResponseEntity<>(
                AppBadRequestExceptionDetails.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .title("Bad Request Exception")
                    .details(ex.getMessage())
                    .developerMessage(ex.getClass().getName())
                    .build(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String fieldNames = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                AppValidationExceptionDetails.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .title("Bad Request Exception: invalid fieldNames")
                    .details("Check the field(s) error")
                    .developerMessage(ex.getClass().getName())
                    .fieldNames(fieldNames)
                    .fieldMessage(fieldMessage)
                    .build(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        AppExceptionDetails appExceptionDetails = AppExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .title(ex.getCause().getMessage())
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .build();

        return new ResponseEntity<>(appExceptionDetails, headers, status);
    }
}
