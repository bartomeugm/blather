package com.github.richardjwild.blather.persistence.mongodb;

import com.github.richardjwild.blather.persistence.UserDao;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbUserDao implements UserDao {
    private final MongoCollection<Document> users;

    public MongoDbUserDao(MongoCollection<Document> users) {

        this.users = users;
    }

    @Override
    public void saveUser(String name) {
        Document user = new Document()
                .append("name", name);
        users.insertOne(user);
    }

    @Override
    public String findUser(String name) {
        Document user = users.find(eq("name", name)).first();
        if (user == null) return null;
        return user.getString("name");
    }
}
