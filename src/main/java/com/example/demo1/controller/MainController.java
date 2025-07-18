package com.example.demo1.controller;

import com.example.demo1.entity.ConnectionEntity;
import com.example.demo1.entity.DbDetails;
import com.example.demo1.repository.ConnectionRepo;
import com.example.demo1.service.MainService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@Tag(name="connection usage",description = "test connections")
@RestController
//@RequestMapping("/query")
public class MainController {

    @Autowired
    MainService m;

    @Autowired
    ConnectionRepo repo;

    @GetMapping("/connect/test")
    public ResponseEntity<String> testConnection(@RequestParam int id) {
        try{
            Optional<ConnectionEntity> optionalConnection = repo.findById(id);
            if (optionalConnection.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Connection ID not present");
            }
            ConnectionEntity connection = optionalConnection.get();
            DbDetails db = new DbDetails(connection.getUrl(), connection.getUsername(), connection.getPassword());
            if (m.isConnected(db)) {
                return ResponseEntity.ok("Connected successfully");
            }
        }catch(RuntimeException e){
            if (e.getMessage().contains("credentials")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Check Credentials of connection");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Connection failed: ");
    }

    @GetMapping("/catalogs")
    public ResponseEntity<?> getDatabases(@RequestParam int id) {
        try{
            Optional<ConnectionEntity> optionalConnection = repo.findById(id);
            if (optionalConnection.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Connection ID not present");
            }
            ConnectionEntity connection = optionalConnection.get();
            DbDetails db = new DbDetails(connection.getUrl(), connection.getUsername(), connection.getPassword());
            if(m.isConnected(db))
                return ResponseEntity.ok(m.databases());
        }
        catch(RuntimeException e){
            if(e.getMessage().contains("Check"))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Check connection Credentials");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
    }
    @GetMapping("/schemas")
    public ResponseEntity<?> getSchemas(@RequestParam int id, @RequestParam String dbName) {
        try {
            Optional<ConnectionEntity> optionalConnection = repo.findById(id);
            if (optionalConnection.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Connection ID not present");
            }

            ConnectionEntity connection = optionalConnection.get();
            DbDetails db = new DbDetails(connection.getUrl(), connection.getUsername(), connection.getPassword());

            if (m.isConnected(db)) {
                return ResponseEntity.ok(m.schemas(dbName));
            }

        } catch (RuntimeException e) {
            if (e.getMessage().contains("Check"))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Check connection Credentials");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
    }

    @GetMapping("/tables")
    public ResponseEntity<?> getTables(
                                  @RequestParam int id,
                                  @RequestParam String database) {
        try{
            Optional<ConnectionEntity> optionalConnection = repo.findById(id);
            if (optionalConnection.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Connection ID not present");
            }
            ConnectionEntity connection = optionalConnection.get();
            DbDetails db = new DbDetails(connection.getUrl(), connection.getUsername(), connection.getPassword());
            if(m.isConnected(db))
                return ResponseEntity.ok(m.tables(database));
        }
        catch(RuntimeException e){
            if(e.getMessage().contains("Check if")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Check if database exist");
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Check connection credentials");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Connection Error");

    }

    @GetMapping("/columns")
    public ResponseEntity<?> getColumns(@RequestParam int id,
                                        @RequestParam String database, @RequestParam String table) {
        try{
            Optional<ConnectionEntity> optionalConnection = repo.findById(id);
            if (optionalConnection.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Connection ID not present");
            }
            ConnectionEntity connection = optionalConnection.get();
            DbDetails db = new DbDetails(connection.getUrl(), connection.getUsername(), connection.getPassword());
            if(m.isConnected(db))
                return ResponseEntity.ok().body(m.columns(database, table));
        }catch(RuntimeException e){
            if(e.getMessage().contains("Check if")){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Check if database and tables exist");
            }
            if(e.getMessage().contains("credentials")){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Check connection credentials");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Connection Error");
    }
}
