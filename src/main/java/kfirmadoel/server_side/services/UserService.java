package kfirmadoel.server_side.services;

import org.springframework.beans.factory.annotation.Autowired;

import kfirmadoel.server_side.documents.users;
import kfirmadoel.server_side.repositories.UserRepository;

public class UserService {
    
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(String email, String password, String firstName, String secondName) {
        users newUser = new users(email, password, firstName, secondName);
        userRepository.save(newUser);
    }

    public users getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUserPassword(String email, String newPassword) {
        users user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(newPassword);
            userRepository.save(user);
        } else {
            // Handle case where user with given email doesn't exist
        }
    }

    public void deleteUser(String email) {
        users user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
        } else {
            // Handle case where user with given email doesn't exist
        }
    }
}
