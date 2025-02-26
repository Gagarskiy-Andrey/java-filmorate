package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.Add;
import ru.yandex.practicum.filmorate.validators.Update;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @NotNull(groups = {Update.class}, message = "Id отсутствует")
    private Long id;
    @NotBlank(groups = {Add.class, Update.class}, message = "Email can not be blank")
    @Email(groups = {Add.class, Update.class}, message = "Email invalid")
    private String email;
    @NotBlank(groups = {Add.class, Update.class}, message = "Login can not be blank")
    @Pattern(groups = {Add.class, Update.class}, regexp = "^[^\\s]*$", message = "Строка не должна содержать пробелы")
    private String login;
    private String name;
    private LocalDate birthday;

    Set<Long> friends = new HashSet<>();

    public User() {
    }

    public User(Long id, String email, String login, String name, LocalDate birthday, Set<Long> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = friends != null ? friends : new HashSet<>();
    }

    @AssertTrue(groups = {Add.class, Update.class}, message = "Birthday date invalid")
    public boolean isValideReleaseDate() {
        return birthday.isBefore(LocalDate.now());
    }
}
