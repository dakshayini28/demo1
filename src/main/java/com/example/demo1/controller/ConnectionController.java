package com.example.demo1.controller;

import com.example.demo1.entity.ConnectionDetails;
import com.example.demo1.entity.DbDetails;
import com.example.demo1.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class ConnectionController {
    @Autowired
    private ConnectionService connectionService;
    @GetMapping("/connections")
    public HashMap<Integer,String> getConnections(){
        return connectionService.getConnections();
    }
    @PostMapping("/addConnection")
    public ResponseEntity<String> addConnection(@RequestBody ConnectionDetails con){
        try {
            connectionService.addConnection(con.getName(), con.getUrl(), con.getUsername(), con.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body("Connection created");
        } catch (RuntimeException e) {
            if (e.getMessage().contains("already exists")) {
                return ResponseEntity.ok("Connection with this name already exists");
            }
            return ResponseEntity.ok("error occured");
        }
    }
    @DeleteMapping("/deleteConnection")
    public ResponseEntity<String> deleteConnection(@RequestParam int id){
        try {
            connectionService.deleteConnection(id);
            return ResponseEntity.ok("deleted");
        }catch(Exception e){
            if(e.getMessage().contains("Connection with given ID does not exist.")) return ResponseEntity.ok("Id doesnt exist");
            return ResponseEntity.ok("Some error occured");
        }
    }
    @PutMapping("/updateConnection")
    public ResponseEntity<String> updateConnection(@RequestParam int id,@RequestBody ConnectionDetails con){
        try{
            connectionService.updateConnection(id,con.getName(),con.getUrl(),con.getUsername(),con.getPassword());
            return ResponseEntity.ok("updated");
        }catch (Exception e){
            if(e.getMessage().contains("Connection Id not found")) return ResponseEntity.ok("Id doesnt exist");
            return ResponseEntity.ok("Some error occured");
        }
    }
}
