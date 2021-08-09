package tech.skullprogrammer.projectmaker.model;

public class ConfigurationDB {

    private String dbType;
    private String dbHost;
    private String dbPort;
    private String dbTmpDb;
    private String dbDriver;
    private String dbSchemaGeneratorClass;
    private String dbName;
    private String dbUsername;
    private String dbPassword;
    private String dbDialect;

    public ConfigurationDB(String dbType, String dbHost, String dbPort, String dbTmpDb, String dbDriver, String dbSchemaGeneratorClass,
            String dbName, String dbUsername, String dbPassword, String dbDialect) {
        this.dbType = dbType;
        this.dbHost = dbHost;
        this.dbPort = dbPort;
        this.dbTmpDb = dbTmpDb;
        this.dbDriver = dbDriver;
        this.dbSchemaGeneratorClass = dbSchemaGeneratorClass;
        this.dbName = dbName;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.dbPassword = dbPassword;
        this.dbDialect = dbDialect;
    }

    public String getDbType() {
        return dbType;
    }
    
    public String getDbHost() {
        return dbHost;
    }

    public String getDbPort() {
        return dbPort;
    }

    public String getDbTmpDb() {
        return dbTmpDb;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public String getDbSchemaGeneratorClass() {
        return dbSchemaGeneratorClass;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbDialect() {
        return dbDialect;
    }    
    
}
