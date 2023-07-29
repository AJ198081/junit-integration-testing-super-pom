package dev.aj.repository.implementation;

import dev.aj.domain.model.User;
import dev.aj.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {

    static Map<UUID, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        UUID id = UUID.randomUUID();
        User savedUser = new User(id, user);
        users.put(id, savedUser);
        return savedUser;
    }

    @Override
    public User findById(UUID id) {

        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NoSuchElementException(String.format("Unable to find User Id '%s' in our records"));
        }
    }

}
