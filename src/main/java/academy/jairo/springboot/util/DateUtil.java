package academy.jairo.springboot.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtil {
    public String formatLocalDate(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mn").format(localDateTime);
    }
}
