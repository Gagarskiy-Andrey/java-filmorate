package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.Add;
import ru.yandex.practicum.filmorate.validators.Update;

import java.time.LocalDate;

@Data
public class User {
    @NotNull(groups = {Update.class}, message = "Id отсутствует")
    Long id;
    @NotBlank(groups = {Add.class, Update.class}, message = "Email can not be blank")
    @Email(groups = {Add.class, Update.class}, message = "Email invalid")
    String email;
    @NotBlank(groups = {Add.class, Update.class}, message = "Login can not be blank")
    @Pattern(groups = {Add.class, Update.class}, regexp = "^[^\\s]*$", message = "Строка не должна содержать пробелы")
    String login;
    String name;
    LocalDate birthday;

    @AssertTrue(groups = {Add.class, Update.class}, message = "Birthday date invalid")
    public boolean isValideReleaseDate() {
        return birthday.isBefore(LocalDate.now());
    }
}
