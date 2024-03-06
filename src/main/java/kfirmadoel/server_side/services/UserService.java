package kfirmadoel.server_side.services;

import org.springframework.stereotype.Service;

import kfirmadoel.server_side.documents.User;
import kfirmadoel.server_side.repositories.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(String email, String password, String firstName, String secondName) {
        User newUser = new User(email, password, firstName, secondName);
        userRepository.save(newUser);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUserPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(newPassword);
            userRepository.save(user);
        } else {
            // Handle case where user with given email doesn't exist
        }
    }

    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
        } else {
            // Handle case where user with given email doesn't exist
        }
    }

    public void createUser(User user) {
        userRepository.save(user);
    }
}
