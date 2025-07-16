package com.example.demo1.service;

import com.example.demo1.entity.DbDetails;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Service
public class MainService {
    private Connection con;
    private String dbType;

    public boolean isConnected(DbDetails body) {
        try {
            String url = body.getUrl().toLowerCase();
            if (url.contains("mysql")) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                dbType = "mysql";
            } else if (url.contains("postgresql")) {
                Class.forName("org.postgresql.Driver");
                dbType = "postgresql";
            } else if (url.contains("oracle")) {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                dbType = "oracle";
            } else if (url.contains("sqlserver")) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                dbType = "sqlserver";
            } else {
                throw new RuntimeException("Unsupported database type in URL: " + body.getUrl());
            }
            System.out.println("Whats happening");
            con = DriverManager.getConnection(body.getUrl(), body.getUsername(), body.getPassword());
            System.out.println("Whats con"+con);

            return con != null;

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found: " + e.getMessage());
        }catch (Exception e) {
            throw new RuntimeException("Connection failedsss: " + e.getMessage());
        }
    }

    private String getDatabasesQuery() {
        return switch (dbType) {
            case "mysql" -> "SHOW DATABASES";
            case "postgresql" -> "SELECT datname FROM pg_database WHERE datistemplate = false";
            case "sqlserver" -> "SELECT name FROM sys.databases";
            case "oracle" -> "SELECT username FROM all_users";
            default -> throw new RuntimeException("Unsupported DB for database query");
        };
    }

    private String getSchemasQuery() {
        return switch (dbType) {
            case "mysql" -> "SELECT schema_name FROM information_schema.schemata";
            case "postgresql" -> "SELECT schema_name FROM information_schema.schemata";
            case "sqlserver" -> "SELECT name FROM sys.schemas";
            case "oracle" -> "SELECT username AS schema_name FROM all_users";
            default -> throw new RuntimeException("Unsupported DB for schema query");
        };
    }
    public List<Map<String, Object>> schemas() {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(getSchemasQuery())) {
            while (rs.next()) {
                Map<String, Object> schema = new HashMap<>();
                schema.put("name", rs.getString(1));
                schema.put("type", "schema");
                schema.put("properties", new HashMap<>());
                list.add(schema);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching schemas: " + e.getMessage());
        }
        return list;
    }

    private String getTablesQuery(String dbName) {
        return switch (dbType) {
            case "mysql" -> "SHOW TABLES IN `" + dbName + "`";
            case "postgresql" -> "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";
            case "sqlserver" -> "SELECT TABLE_NAME FROM " + dbName + ".INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE'";
            case "oracle" -> "SELECT table_name FROM all_tables WHERE owner = '" + dbName.toUpperCase() + "'";
            default -> throw new RuntimeException("Unsupported DB for table query");
        };
    }

    private String getSampleRowQuery(String database, String table) {
        return switch (dbType) {
            case "mysql" -> "SELECT * FROM `" + database + "`.`" + table + "` LIMIT 1";
            case "postgresql" -> "SELECT * FROM \"" + database + "\".\"" + table + "\" LIMIT 1";
            case "sqlserver" -> "SELECT TOP 1 * FROM " + database + ".dbo.[" + table + "]";
            case "oracle" -> "SELECT * FROM " + database + "." + table + " WHERE ROWNUM = 1";
            default -> throw new RuntimeException("Unsupported DB for column metadata");
        };
    }

    public List<Map<String, Object>> databases() {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(getDatabasesQuery())) {
            while (rs.next()) {
                Map<String, Object> db = new HashMap<>();
                db.put("name", rs.getString(1));
                db.put("type", "database");
                db.put("properties", new HashMap<>());
                list.add(db);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching databases: " + e.getMessage());
        }
        return list;
    }

    public List<Map<String, Object>> tables(String dbName) {
        List<Map<String, Object>> list = new ArrayList<>();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(getTablesQuery(dbName))) {
            while (rs.next()) {
                Map<String, Object> table = new HashMap<>();
                table.put("name", rs.getString(1));
                table.put("type", "table");
                table.put("properties", new HashMap<>());
                list.add(table);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching tables: " + e.getMessage());
        }
        return list;
    }

    public List<Map<String, Object>> columns(String database, String table) {
        List<Map<String, Object>> columns = new ArrayList<>();
        Set<String> primaryKeys = getPrimaryKeys(database, table);

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(getSampleRowQuery(database, table))) {

            ResultSetMetaData meta = rs.getMetaData();
            int count = meta.getColumnCount();

            for (int i = 1; i <= count; i++) {
                String colName = meta.getColumnName(i);
                Map<String, Object> col = new LinkedHashMap<>();
                col.put("name", colName);
                col.put("type", "column");

                Map<String, Object> props = new LinkedHashMap<>();
                props.put("datatype", meta.getColumnTypeName(i));
                props.put("length", meta.getPrecision(i));
                props.put("precision", meta.getPrecision(i));
                props.put("primaryKey", primaryKeys.contains(colName));
                props.put("notNull", meta.isNullable(i) == ResultSetMetaData.columnNoNulls);

                col.put("properties", props);
                columns.add(col);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching column metadata: " + e.getMessage());
        }
        return columns;
    }

    private Set<String> getPrimaryKeys(String database, String table) {
        Set<String> keys = new HashSet<>();
        try {
            DatabaseMetaData dbMeta = con.getMetaData();
            ResultSet rs = dbMeta.getPrimaryKeys(null, null, table);
            while (rs.next()) {
                keys.add(rs.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching primary keys: " + e.getMessage());
        }
        return keys;
    }
}
