package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.validators.Add;
import ru.yandex.practicum.filmorate.validators.Update;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    @NotNull(groups = {Update.class}, message = "Id отсутствует")
    Long id;
    @NotBlank(groups = {Add.class, Update.class}, message = "Name can not be blank")
    String name;
    @Size(groups = {Add.class, Update.class}, max = 200, message = "Max size is 200")
    String description;
    LocalDate releaseDate;
    @Positive(groups = {Add.class, Update.class}, message = "duration must be positive")
    Integer duration;

    @AssertTrue(groups = {Add.class, Update.class}, message = "Release date invalid")
    public boolean isValideReleaseDate() {
        return releaseDate.isAfter(LocalDate.of(1895, 12, 28));
    }
}
