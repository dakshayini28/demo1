package com.example.demo1.controller;

import com.example.demo1.entity.UserEntity;
import com.example.demo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/get")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody UserEntity u){
        try{
            userService.addUser(u);
            return ResponseEntity.status(HttpStatus.CREATED).body("Added");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User may exist");
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateData(@RequestParam int id, @RequestBody HashMap<String,String> h){
        try {
            userService.update(id, h);
            return ResponseEntity.ok().body("User updated");
        } catch (Exception e) {
            if(e.getMessage().contains("Id may"))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id is not present");
            if(e.getMessage().contains("Invalid"))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check fields");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update User");
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam int id){
        try{
            userService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted User");
        } catch (Exception e) {
            if(e.getMessage().contains("Id"))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID doesnt exist");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
    }
}
