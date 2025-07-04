package com.example.demo1.controller;

import com.example.demo1.entity.ConnectionEntity;
import com.example.demo1.entity.DbDetails;
import com.example.demo1.repository.ConnectionRepo;
import com.example.demo1.service.ConnectionService;
import com.example.demo1.service.MainService;
import com.example.demo1.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/query")
public class QueryController {
    @Autowired
    ConnectionRepo repo;

    @Autowired
    MainService m;

    @Autowired
    QueryService q;
    @GetMapping("/getRecords")
    public ResponseEntity<?>  getRecords(@RequestParam int id,@RequestParam String sql){
        try{
            Optional<ConnectionEntity> optionalConnection = repo.findById(id);
            if (optionalConnection.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Connection ID not present");
            }
            ConnectionEntity connection = optionalConnection.get();
            DbDetails db = new DbDetails(connection.getUrl(), connection.getUsername(), connection.getPassword());
            if(m.isConnected(db)){
                return ResponseEntity.ok(q.getData(sql,db));
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Connection ID not present");
        }
        catch(RuntimeException e){
            if(e.getMessage().contains("SQL")){
                return ResponseEntity.ok(e.getMessage());
            }
            if(e.getMessage().contains("Check if")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Check if database exist");
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Check connection credentials");
        }
    }

    @GetMapping("/getData")
    public ResponseEntity<?> getData(@RequestParam int id,@RequestParam String sql){
        try{
            Optional<ConnectionEntity> optionalConnection = repo.findById(id);
            if (optionalConnection.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Connection ID not present");
            }
            ConnectionEntity connection = optionalConnection.get();
            DbDetails db = new DbDetails(connection.getUrl(), connection.getUsername(), connection.getPassword());
            if(m.isConnected(db)){
                return ResponseEntity.ok(q.getSqlData(sql,db));
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Connection ID not present");
        }
        catch(RuntimeException e){
            if(e.getMessage().contains("SQL")){
                return ResponseEntity.ok(e.getMessage());
            }
            if(e.getMessage().contains("Check if")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Check if database exist");
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Check connection credentials");
        }

    }
}
