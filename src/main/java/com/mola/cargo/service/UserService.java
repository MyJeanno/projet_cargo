package com.mola.cargo.service;

import com.mola.cargo.model.User;
import com.mola.cargo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> showUsers(){
        return userRepository.findAll();
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public User showOneUser(Long id){
        Optional<User> optional = userRepository.findById(id);
        User user = null;
        if(optional.isPresent()){
            user = optional.get();
        }else {
            throw new RuntimeException("id introuvable");
        }
        return user;
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
}
