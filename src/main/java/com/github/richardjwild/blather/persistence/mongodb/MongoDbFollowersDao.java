package com.github.richardjwild.blather.persistence.mongodb;

import com.github.richardjwild.blather.persistence.FollowersDao;
import com.github.richardjwild.blather.user.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbFollowersDao implements FollowersDao {
    private MongoCollection<Document> users;

    public MongoDbFollowersDao(MongoCollection<Document> users) {
        this.users = users;
    }

    @Override
    public void saveFollowees(String follower, Set<User> followees) {
        users.findOneAndUpdate(
                eq("name",)
        )
    }

    @Override
    public Set<String> getFollowees(String follower) {
        Document user = users.find(eq("name", follower)).first();
        List<String> followees = (List<String>) user.getOrDefault("followees", Collections.emptyList());
        return new HashSet<>(followees);
    }

    public static void main(String[] args) {
        MongoClient client = MongoClients.create();
        MongoDatabase database = client.getDatabase("blather");
        MongoCollection<Document> users = database.getCollection("users");

        MongoDbFollowersDao followersDao = new MongoDbFollowersDao(users);

        followersDao.saveFollowees("Richard", new HashSet<>(){{
            add(new User("Sarah"));
            add(new User("Jolene"));
        }});

        Set<String> followeesNames = followersDao.getFollowees("Richard");

        followeesNames.forEach(System.out::println);
    }
}
