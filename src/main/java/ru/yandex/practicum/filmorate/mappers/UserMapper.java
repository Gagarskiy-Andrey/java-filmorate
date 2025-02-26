package ru.yandex.practicum.filmorate.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

@Component
public class UserMapper {
    public User mapToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("user_id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setBirthday(resultSet.getDate("birth_day").toLocalDate());
        user.setFriends(new HashSet<>());
        return user;
    }

    public void setUserParameters(PreparedStatement ps, User user) throws SQLException {
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getLogin());
        ps.setString(3, user.getName());
        ps.setString(4, String.valueOf(Date.valueOf(user.getBirthday())));
    }
}
