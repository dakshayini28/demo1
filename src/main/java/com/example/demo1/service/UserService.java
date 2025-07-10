package com.example.demo1.service;

import com.example.demo1.dto.UserDto;
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
    public void addUser(UserDto u){
        UserEntity user=new UserEntity();
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        user.setPassword(u.getPassword());
        user.setEmail(u.getEmail());
        user.setMobile(u.getMobile());
        user.setUserName(u.getUserName());
        repo.save(user);
    }
    public void update(int id, UserDto dto) {
        Optional<UserEntity> o = repo.findById(id);
        if (o.isPresent()) {
            UserEntity c = o.get();
            if (dto.getUserName() != null) {
                c.setUserName(dto.getUserName());
            }
            if (dto.getMobile() != null) {
                c.setMobile(dto.getMobile());
            }
            if (dto.getEmail() != null) {
                c.setEmail(dto.getEmail());
            }
            if (dto.getPassword() != null) {
                c.setPassword(passwordEncoder.encode(dto.getPassword()));
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
