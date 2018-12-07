package com.github.richardjwild.blather.persistence.mongodb;

import com.github.richardjwild.blather.persistence.FollowersDao;
import com.github.richardjwild.blather.persistence.UserDao;
import com.github.richardjwild.blather.user.User;
import com.github.richardjwild.blather.user.UserRepository;

import java.util.Optional;

public class MongoDbUserRepository implements UserRepository {
    private final UserDao userDao;
    private final FollowersDao followersDao;

    public MongoDbUserRepository(UserDao userDao, FollowersDao followersDao) {
        this.userDao = userDao;
        this.followersDao = followersDao;
    }

    @Override
    public Optional<User> find(String name) {
        String username = userDao.findUser(name);
        if (username == null) return Optional.empty();
        throw new RuntimeException();
    }

    @Override
    public void save(User user) {

    }
}
