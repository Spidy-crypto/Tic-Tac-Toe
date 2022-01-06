package com.example.demo.service;

import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

     PasswordEncoder encoder;

    public UserService(){
        this.encoder = new BCryptPasswordEncoder();
    }

    public User getUser(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public  boolean addUser(User user){
        try {
            String encoded = encoder.encode(user.getPassword());
            user.setPassword(encoded);
            userRepository.save(user);
            return true;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
