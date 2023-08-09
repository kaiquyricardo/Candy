package com.github.kaiquy.candy.data.user;

import com.github.kaiquy.candy.CandyPlugin;
import com.github.kaiquy.candy.database.MySQLProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserStorage {
    private final MySQLProvider service = CandyPlugin.getInstance().getMySQLProvider();

    public void insert(User user) {
        try (Connection connection = service.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO `user_diabetes` VALUES(?,?);")) {
                statement.setString(1, user.getName());
                statement.setInt(2, user.getDiabetes());
                statement.executeUpdate();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void update(User user) {
        try (Connection connection = service.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE `user_diabetes` SET diabetes=? WHERE user_name=?;")) {
                statement.setInt(1, user.getDiabetes());
                statement.setString(2, user.getName());
                statement.executeUpdate();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public User find(String id) {
        try (Connection connection = service.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM `user_diabetes` WHERE user_name=?;")) {
                statement.setString(1, id);
                try (ResultSet set = statement.executeQuery()) {
                    if (!set.next()) return null;

                    final String name = set.getString("user_name");
                    final int diabetes = set.getInt("diabetes");

                    return new User(name, diabetes);
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}