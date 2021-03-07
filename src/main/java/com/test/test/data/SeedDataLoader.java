package com.test.test.data;

import com.test.test.dao.UserDao;
import com.test.test.dao.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedDataLoader {

    private static final Logger log = LoggerFactory.getLogger(SeedDataLoader.class);

    @Bean
    public CommandLineRunner initDatabase(UserServiceImpl repository) {

        return args -> {
            if(repository.getAll().isEmpty()) {
                log.info("Preloading " + repository.save(new UserDao("Frank", "Eddy", "eddy@gmail.com", "08099934567", "Project123", "User")));
                log.info("Preloading " + repository.save(new UserDao("Adetunji", "Akinde", "adetunjiakinde@gmail.com", "08060774043", "Project123", "Admin")));
                log.info("Preloading " + repository.save(new UserDao("Adewale", "Adeniji", "adewale@gmail.com", "0808990987", "adewale007", "User")));
                log.info("Preloading " + repository.save(new UserDao("Jude", "Elvis", "judeelvis@gmail.com", "08066634567", "elvis_1", "User")));
                log.info("Preloading " + repository.save(new UserDao("Biden", "Joe", "joebiden@gmail.com", "0808990680", "biden_usa", "Admin")));
            }
        };
    }
}
