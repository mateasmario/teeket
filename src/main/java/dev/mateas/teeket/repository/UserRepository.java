package dev.mateas.teeket.repository;

import dev.mateas.teeket.entity.authentication.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByUsername(String username);
}
