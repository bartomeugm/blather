package com.github.richardjwild.blather.persistence.mongodb;

import com.github.richardjwild.blather.persistence.FollowersDao;
import com.github.richardjwild.blather.persistence.UserDao;
import com.github.richardjwild.blather.user.User;
import com.github.richardjwild.blather.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<String> followeesNames = followersDao.getFollowees(name);

        Set<User> followees = followeesNames.stream()
                .map(User::new)
                .collect(Collectors.toSet());

        User user = new User(username, followees);
        return Optional.of(user);
    }

    @Override
    public void save(User user) {
        String name = user.name();
        Set<User> followees = user.followees();

        userDao.saveUser(name);
        followersDao.saveFollowees(name, followees);
    }
}
