package com.custom.user_registration.controller;

import com.custom.user_registration.entity.User;
import com.custom.user_registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> usersList = new ArrayList<>();
            userRepository.findAll().forEach(usersList::add);

            if (usersList.isEmpty()){
                return ResponseEntity.status(204).build();
            }

            return ResponseEntity.status(200).body(usersList);
        } catch (Exception ex) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/getUserById/{idUser}")
    public ResponseEntity<User> getUserById(@PathVariable Long idUser) {
        Optional<User> userData = userRepository.findById(idUser);

        if (userData.isPresent()) {
            return ResponseEntity.status(200).body(userData.get());
        }

        return ResponseEntity.status(204).build();
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User userObj = userRepository.save(user);

        return ResponseEntity.status(200).body(userObj);
    }

    @PutMapping("/updateUser/{idUser}")
    public ResponseEntity<User> updateUser(@PathVariable Long idUser, @RequestBody User userBody) {
        try {
            Optional<User> userData = userRepository.findById(idUser);

            if (userData.isPresent()) {
                User existingUser = userData.get();

                existingUser.setName(userBody.getName());
                existingUser.setEmail(userBody.getEmail());
                existingUser.setPassword(userBody.getPassword());

                User updatedUser = userRepository.save(existingUser);
                return ResponseEntity.status(200).body(updatedUser);
            }

            return ResponseEntity.status(404).build();
        } catch (Exception ex) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/deleteUser/{idUser}")
    public ResponseEntity<User> deleteUser(@PathVariable Long idUser){
        try {
            Optional<User> userData = userRepository.findById(idUser);

            if (userData.isPresent()) {
                userRepository.deleteById(idUser);
                return ResponseEntity.status(204).build();
            }

            return ResponseEntity.status(404).build();
        } catch (Exception ex) {
            return ResponseEntity.status(500).build();
        }
    }
}
