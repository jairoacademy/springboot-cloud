package academy.jairo.springboot.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class AppValidationExceptionDetails extends AppExceptionDetails {
    private final String fieldNames;
    private final String fieldMessage;
}
