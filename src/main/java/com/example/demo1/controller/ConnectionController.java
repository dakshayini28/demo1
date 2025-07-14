package com.example.demo1.controller;

import com.example.demo1.dto.ConnectionDto;
import com.example.demo1.entity.ConnectionEntity;
import com.example.demo1.entity.UserEntity;
import com.example.demo1.repository.ConnectionRepo;
import com.example.demo1.repository.UserRepo;
import com.example.demo1.service.ConnectionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/connect")
@Tag(name="connection API",description = "connection-CRUD")
public class ConnectionController {

    @Autowired
    ConnectionService connectionService;
    @Autowired
    UserRepo repo;
    @Autowired
    ConnectionRepo repo1;
    @GetMapping("/getConnections")
    public ResponseEntity<List<ConnectionEntity>> getConnections() {
        try {
            List<ConnectionEntity> connections = connectionService.getAll();
            return ResponseEntity.ok(connections);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/addConnection")
    public ResponseEntity<String> addConnection(@RequestBody ConnectionDto con) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserEntity user = repo.findByUserName(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            int user_id = user.getUserId();
            connectionService.add(con,user_id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Connection created");
        } catch (Exception e) {
            if(e.getMessage().contains("This user"))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user doesnt exist");

            return ResponseEntity.status(HttpStatus.CONFLICT).body("Connection name already exists");
        }
    }

    @DeleteMapping("/deleteConnection")
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

    @PutMapping("/updateConnection")
    public ResponseEntity<String> updateConnection(
            @RequestParam int id,
            @RequestBody ConnectionDto dto) {
        try {
            connectionService.update(id, dto);
            return ResponseEntity.ok("Connection updated");
        } catch (Exception e) {
            if (e.getMessage().contains("authorized"))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to update this connection");
            if (e.getMessage().contains("not found"))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id is not present");
            if (e.getMessage().contains("Invalid"))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check fields");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update connection");
        }
    }

    @GetMapping("/connectionBasic")
    public ResponseEntity<?> getMinimalConnections() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = repo.findByUserName(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        int user_id = user.getUserId();
        List<Object[]> rawList = repo1.findIdAndNameByUsername(user_id);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rawList) {
            Map<String, Object> map = new HashMap<>();
            map.put("connectionId", row[0]);
            map.put("connectionName", row[1]);
            result.add(map);
        }
        return ResponseEntity.ok(result);
    }
    @GetMapping("/searchConnection")
    public ResponseEntity<?> searchConnection(@RequestParam String name){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = repo.findByUserName(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        try{
            return ResponseEntity.ok(connectionService.searchConnection(name,user.getUserId()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/recentConnections")
    public ResponseEntity<?> recentConnections(@RequestParam int page,@RequestParam int offset){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = repo.findByUserName(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        try{
            return ResponseEntity.ok(connectionService.recentConnections(user.getUserId(),page,offset));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
