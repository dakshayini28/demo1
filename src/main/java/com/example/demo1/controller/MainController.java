package com.example.demo1.controller;

import com.example.demo1.entity.DbDetails;
import com.example.demo1.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class MainController {

    @Autowired
    MainService m;

    @GetMapping("connect/test")
    public ResponseEntity<String> testConnection(@RequestParam String url,
                                                 @RequestParam String username,
                                                 @RequestParam String password) {
        DbDetails db = new DbDetails(url, username, password);
        if (m.isConnected(db)) {
            return ResponseEntity.ok("Connected successfully");
        } else {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/catalogs")
    public List<String> getDatabases(@RequestParam String url,
                                     @RequestParam String username,
                                     @RequestParam String password) {
        DbDetails db = new DbDetails(url, username, password);
        return m.databases(db);
    }

    @GetMapping("/tables")
    public List<String> getTables(
                                  @RequestParam String url,
                                  @RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam String database) {
        DbDetails db = new DbDetails(url, username, password);
        return m.tables(database, db);
    }

    @GetMapping("/columns")
    public HashMap<String, String> getColumns(
                                              @RequestParam String url,
                                              @RequestParam String username,
                                              @RequestParam String password,
                                              @RequestParam String database,
                                              @RequestParam String table) {
        DbDetails db = new DbDetails(url, username, password);
        return m.columns(database, table, db);
    }
}
