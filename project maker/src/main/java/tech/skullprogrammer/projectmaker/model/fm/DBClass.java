package tech.skullprogrammer.projectmaker.model.fm;

public class DBClass {

    private String type;
    private String host;
    private String port;
    private String tmpDb;
    private String driver;
    private String schemaGeneratorClass;
    private String name;
    private String username;
    private String password;
    private String dialect;

    public DBClass(String type, String host, String port, String tmpDb, String driver, String schemaGeneratorClass, 
            String name, String username, String password, String dialect) {
        this.type = type;
        this.host = host;
        this.port = port;
        this.tmpDb = tmpDb;
        this.driver = driver;
        this.schemaGeneratorClass = schemaGeneratorClass;
        this.name = name;
        this.username = username;
        this.password = password;
        this.dialect = dialect;
    }

    public String getType() {
        return type;
    }
    
    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getTmpDb() {
        return tmpDb;
    }

    public String getDriver() {
        return driver;
    }

    public String getSchemaGeneratorClass() {
        return schemaGeneratorClass;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDialect() {
        return dialect;
    }
    
}
