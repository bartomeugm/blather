package com.github.richardjwild.blather.persistence.mongodb;

import com.github.richardjwild.blather.persistence.FollowersDao;
import com.github.richardjwild.blather.user.User;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class MongoDbFollowersDao implements FollowersDao {
    private MongoCollection<Document> users;

    public MongoDbFollowersDao(MongoCollection<Document> users) {
        this.users = users;
    }

    @Override
    public void saveFollowees(String follower, Set<User> followees) {
        List<String> followeesNames = new ArrayList<>();
        followees.forEach(user -> followeesNames.add(user.name()));

        users.findOneAndUpdate(
                eq("name",follower),
                set("followees", followeesNames)
        );
    }

    @Override
    public Set<String> getFollowees(String follower) {
        Document user = users.find(eq("name", follower)).first();
        if (user != null) {
            List<String> followees = (List<String>) user.getOrDefault("followees", Collections.EMPTY_LIST);
            return new HashSet<>(followees);
        }
        return Collections.EMPTY_SET;
    }
}
