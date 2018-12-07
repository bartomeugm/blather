package com.github.richardjwild.blather.persistence.mongodb;

import com.github.richardjwild.blather.persistence.FollowersDao;
import com.github.richardjwild.blather.persistence.UserDao;
import com.github.richardjwild.blather.user.User;
import com.github.richardjwild.blather.user.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MongoDbUserRepositoryShould {
    private UserRepository userRepository;
    private UserDao userDao;
    private FollowersDao followersDao;

    @Before
    public void setUp() {
        userDao = mock(MongoDbUserDao.class);
        followersDao = mock(MongoDbFollowersDao.class);
        userRepository = new MongoDbUserRepository(userDao, followersDao);
    }

    @Test
    public void return_empty_when_user_not_found() {
        Optional<User> result = userRepository.find("will_not_be_found");

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void tell_dao_to_save_user() {
        String userName = "will_be_found";
        User expectedUser = new User(userName);

        expectedUser.follow(new User(""));
        userRepository.save(expectedUser);

        verify(userDao).saveUser(expectedUser.name());
        verify(followersDao).saveFollowees(expectedUser.name(), expectedUser.followees());
    }

    @Test
    public void retrieve_user_when_it_exists() {
        String userName = "will_be_found";
        User expectedUser = new User(userName);
        when(userDao.findUser(userName)).thenReturn(userName);

        userRepository.find(expectedUser.name());

        verify(userDao).findUser(expectedUser.name());
        verify(followersDao).getFollowees(expectedUser.name());
    }
}
