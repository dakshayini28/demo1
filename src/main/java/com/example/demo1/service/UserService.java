package com.example.demo1.service;

import com.example.demo1.entity.ConnectionEntity;
import com.example.demo1.entity.UserEntity;
import com.example.demo1.repository.UserRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserRepo repo;
    private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    public List<UserEntity> getAllUsers(){
        return repo.findAll();
    }
    public void addUser(UserEntity u){
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        u.setRoles(List.of("ADMIN"));
        repo.save(u);
    }
    public void update(int id,HashMap<String,String> h){
        Optional<UserEntity>o=repo.findById(id);
        if (o.isPresent()) {
            UserEntity c = o.get();
            for (Map.Entry<String, String> entry : h.entrySet()) {
                String field = entry.getKey();
                String value = entry.getValue();
                switch (field) {
                    case "username":
                        c.setUserName(value);
                        break;
                    case "mobile":
                        c.setMobile(value);
                        break;
                    case "email":
                        c.setEmail(value);
                        break;
                    case "password":
                        c.setPassword(passwordEncoder.encode(value));
                        break;
                    default:
                        throw new RuntimeException("Invalid field: " + field);
                }
            }
            repo.save(c);
        } else {
            throw new RuntimeException("Id may not be present");
        }
    }
    public void delete(int id){
        Optional<UserEntity> o=repo.findById(id);
        if(o.isEmpty()){
            throw new RuntimeException("Id does not exist");
        }else{
            UserEntity u=o.get();
            repo.delete(u);
        }
    }
}
