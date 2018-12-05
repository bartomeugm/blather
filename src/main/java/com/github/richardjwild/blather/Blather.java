package com.github.richardjwild.blather;

import com.github.richardjwild.blather.application.Application;
import com.github.richardjwild.blather.application.ApplicationBuilder;
import com.github.richardjwild.blather.io.ConsoleInput;
import com.github.richardjwild.blather.io.ConsoleOutput;
import com.github.richardjwild.blather.persistence.*;
import com.github.richardjwild.blather.time.SystemClock;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

import static com.github.richardjwild.blather.persistence.DataSourceHelper.newMySQLDataSource;
import static com.github.richardjwild.blather.persistence.DataSourceHelper.transactionManager;

public class Blather {

    public static void main(String[] args) {
        DataSource dataSource = newMySQLDataSource();
        DataSourceTransactionManager transactionManager = transactionManager(dataSource);
        Application application = ApplicationBuilder.build(new ConsoleInput(),
                new ConsoleOutput(),
                new SystemClock(),
                new MySqlUserRepository(new UserDao(dataSource), new FollowersDao(dataSource, transactionManager)),
                new MySqlMessageRepository(new MessageDao(dataSource)));
        application.run();
    }

}
