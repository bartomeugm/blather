package com.github.richardjwild.blather.persistence.mongodb;

import com.github.richardjwild.blather.persistence.FollowersDao;
import com.github.richardjwild.blather.user.User;

import java.util.Set;

public class MongoDbFollowersDao implements FollowersDao {
    @Override
    public void saveFollowees(String follower, Set<User> followees) {

    }

    @Override
    public Set<String> getFollowees(String follower) {
        return null;
    }
}
