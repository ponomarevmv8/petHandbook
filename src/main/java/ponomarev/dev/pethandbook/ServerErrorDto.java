package ponomarev.dev.pethandbook;

import java.time.LocalDateTime;

public record ServerErrorDto(
        String message,
        String detail,
        LocalDateTime dateTime
) {
}
