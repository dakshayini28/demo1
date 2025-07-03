package com.example.demo1.entity;

import com.example.demo1.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserEntityTest {
    @Autowired
    UserRepo repo;
    @Test
    public void printUser(){
        Optional<UserEntity> l=repo.findById(1);
        l.ifPresent(System.out::println);
    }
    @Test
    public void printByEmail(){
        List<UserEntity> l=repo.findByEmail("dakshayinialuri@gmail.com");
        System.out.println(l);
    }
    @Test
    public void printByLike(){
        List<UserEntity> u=repo.findByUserNameLike("%a%");
        System.out.println(u);
    }
}