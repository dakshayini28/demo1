package com.example.demo1.controller;

import com.example.demo1.entity.ConnectionEntity;
import com.example.demo1.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ConnectionController {

    @Autowired
    ConnectionService connectionService;

    @GetMapping("/connection")
    public ResponseEntity<List<ConnectionEntity>> getConnections() {
        try {
            List<ConnectionEntity> connections = connectionService.getAll();
            return ResponseEntity.ok(connections);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/addconnection")
    public ResponseEntity<String> addConnection(@RequestParam int user_id,@RequestBody ConnectionEntity con) {
        try {
            connectionService.add(con,user_id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Connection created");
        } catch (Exception e) {
            if(e.getMessage().contains("This user"))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user doesnt exist");

            return ResponseEntity.status(HttpStatus.CONFLICT).body("Connection name already exists");
        }
    }

    @DeleteMapping("/deleteconnection")
    public ResponseEntity<String> deleteConnection(@RequestParam int id) {
        try {
            connectionService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Connection Deleted");
        } catch (Exception e) {
            if(e.getMessage().contains("Connection not found with id:"))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Connection id is not present");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete connection");
        }
    }

    @PutMapping("/updateconnection")
    public ResponseEntity<String> updateConnection(
            @RequestParam int id,
            @RequestBody HashMap<String, String> newVal) {
        try {
            connectionService.update(id, newVal);
            return ResponseEntity.ok().body("Connection updated");
        } catch (Exception e) {
            if(e.getMessage().contains("Id may"))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id is not present");
            if(e.getMessage().contains("Invalid"))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check fields");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update connection");
        }
    }
}
