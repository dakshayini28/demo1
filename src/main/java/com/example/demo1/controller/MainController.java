package com.example.demo1.controller;

import com.example.demo1.entity.ConnectionEntity;
import com.example.demo1.entity.DbDetails;
import com.example.demo1.repository.ConnectionRepo;
import com.example.demo1.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {

    @Autowired
    MainService m;

    @Autowired
    ConnectionRepo repo;

    @GetMapping("/connect/test")
    public ResponseEntity<String> testConnection(@RequestParam int id) {
        try{
            ConnectionEntity c=repo.findById(id).get();
            DbDetails db = new DbDetails(c.getUrl(),c.getUsername(), c.getPassword());
            if (m.isConnected(db)) {
                return ResponseEntity.ok("Connected successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
            }
        }catch(RuntimeException e){
            if (e.getMessage().contains("credentials")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Check Credentials of connection");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id not present");
        }

    }

    @GetMapping("/catalogs")
    public ResponseEntity<?> getDatabases(@RequestParam int id) {
        try{
            ConnectionEntity c=repo.findById(id).get();
            DbDetails db = new DbDetails(c.getUrl(),c.getUsername(), c.getPassword());
            if(m.isConnected(db))
                return ResponseEntity.ok(m.databases(db));
        }
        catch(RuntimeException e){
//            System.out.println(e);
            if(e.getMessage().contains("No value present")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Connection Id not present");
            }
            if(e.getMessage().contains("Check"))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Check connection Credentials");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
    }

    @GetMapping("/tables")
    public ResponseEntity<?> getTables(
                                  @RequestParam int id,
                                  @RequestParam String database) {
        try{
            ConnectionEntity c=repo.findById(id).get();
            DbDetails db = new DbDetails(c.getUrl(),c.getUsername(), c.getPassword());
            if(m.isConnected(db))
                return ResponseEntity.ok(m.databases(db));
        }
        catch(RuntimeException e){
            if(e.getMessage().contains("Check if")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Check if database exist");
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Check connection credentials");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID doesnt exist");

    }

    @GetMapping("/columns")
    public ResponseEntity<?> getColumns(@RequestParam int id,
                                        @RequestParam String database, @RequestParam String table) {
        try{
            ConnectionEntity c=repo.findById(id).get();
            DbDetails db = new DbDetails(c.getUrl(),c.getUsername(), c.getPassword());
            if(m.isConnected(db))
                return ResponseEntity.ok().body(m.columns(database, table, db));
        }catch(RuntimeException e){
            if(e.getMessage().contains("Check if")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Check if database and tables exist");
            }
            if(e.getMessage().contains("credentials")){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Check connection credentials");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID doesnt exist");
    }
}
