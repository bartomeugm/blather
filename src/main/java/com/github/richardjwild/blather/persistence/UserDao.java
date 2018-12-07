package com.github.richardjwild.blather.persistence;

public interface UserDao {
    void saveUser(String name);

    String findUser(String name);
}
