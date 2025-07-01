package com.example.demo1.service;

import com.example.demo1.entity.DbDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueryService {
    public List<Map<String, Object>>  getData(String sql, DbDetails db) {
        List<Map<String, Object>> metaList = new ArrayList<>();

        try {
            Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
            Statement stmt = con.createStatement();
            boolean hasResult= stmt.execute(sql);
            System.out.println("whats this "+hasResult);
            if(hasResult){
                ResultSet rs= stmt.getResultSet();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    Map<String, Object> colMeta = new LinkedHashMap<>();
                    colMeta.put("columnName", metaData.getColumnName(i));
                    colMeta.put("Alias name",metaData.getColumnLabel(i));
                    colMeta.put("dataType", metaData.getColumnTypeName(i));
                    colMeta.put("Is AutoIncrement",metaData.isAutoIncrement(i));
                    colMeta.put("Is null",metaData.isNullable(i));
                    colMeta.put("precision", metaData.getPrecision(i));
                    colMeta.put("scale", metaData.getScale(i));
                    metaList.add(colMeta);
                }
                con.close();
            } else {
                int rowCount = stmt.getUpdateCount();
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return metaList;
    }
    public List<Map<String, String>> getSqlData(String sql, DbDetails db) {
        List<Map<String, String>> resultList = new ArrayList<>();

        try (
                Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
                Statement stmt = con.createStatement()
        ) {
            sql = sql.trim().replaceAll(";+$", "");
            if (!sql.toLowerCase().contains("limit")) {
                sql += " LIMIT 100";
            }

            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = meta.getColumnLabel(i);
                    String value = rs.getString(i);
                    row.put(columnName, value);
                }
                resultList.add(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return resultList;
    }

}
