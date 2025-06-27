- Git-integrated Spring Boot project
- Clean packaging structure (controller, service, config, etc.)
- Working JDBC connection to **MSSQL Server**--(MySql)
- Functional APIs:
    - `GET /connect/test` → To check if the connection is active
    - `GET /catalogs` → Get all the list of Databases for a provided MSSQL Server details
    - `GET /schemas` → Get all the list of Schemas for a provided Database --
    - `GET /tables` → Get all the list of Tables for provided Database and Schema
    - `GET /columns` → Get all the list of Columns with Type for provided Database, Schema, Table


Enhance your project from tomorrow by saving the connections
Connection here means the configuration provided to connect to a datasource (url, username and password)
User should be able to
1. Create connection with name, jdbc url, username, password
2. Edit the connection
3. Delete connection
4. Get list of connections available
Also change the current APIs(test, catalogs, schemas, tables, columns) to expect the connection id instead of complete configuration.
