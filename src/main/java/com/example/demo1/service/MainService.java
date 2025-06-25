package com.example.demo1.service;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MainService {
    Connection con;
    public boolean isConnected() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/studentdb",
                    "root", "dakshu");
            if (con != null) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return false;
    }
    public List<String> databases(){
        String sql="show databases";
        List<String> databaseList =new ArrayList<>();
        if(isConnected()){
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String dbName = rs.getString(1);
                    databaseList.add(dbName);
                }
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return databaseList;
    }

    public List<String> tables(String db){
        String sql = "SHOW TABLES IN " + db;
        System.out.println("database selected"+db);
        List<String> al=new ArrayList<>();
        if(isConnected()){
            try(Statement stmt= con.createStatement();
            ResultSet rs= stmt.executeQuery(sql)){
                while(rs.next()){
                    al.add(rs.getString(1));
                }
                con.close();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        return al;
    }
    public HashMap<String,String> columns(String database, String table){
        String sql="show columns in "+database+"."+table;
        HashMap<String,String> h=new HashMap<>();
        if(isConnected()){
            try(Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(sql)){
                while(rs.next()){
                    h.put(rs.getString("Field"),rs.getString("Type"));
//                    System.out.println(rs.getString("Field"));
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return h;
    }

}
