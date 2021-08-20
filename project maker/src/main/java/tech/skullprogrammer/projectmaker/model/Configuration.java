package tech.skullprogrammer.projectmaker.model;

public class Configuration {

    private String packageRootName;
    private String packageModelName;
    private String packagePersistenceName;
    private String templatePath;
    private String templateOutputName;
    private String outputPath;
    private String jsonFolderPath;
    private Boolean daoParametrized;
    private String extensionName;
    private String extensionPackage;
    private Boolean daoInterfaceParametrized;
    private String daoInterfaceExtensionName;
    private String daoInterfaceExtensionPackage;
    private String daoExceptionName;
    private String daoExceptionPackage;
    private String dtoPackage;
    private String endpointPackageName;
    private String routerPackageName;
    private String routerName;
    private String templateOutputDescription;
    private ConfigurationDB configurationDB;

    public Configuration(String packageRootName, String packageModelName, String packagePersistenceName, String templatePath, String templateOutputName,
            String outputPath, String jsonFolderPath, Boolean daoParametrized, String extensionName, String extensionPackage, Boolean daoInterfaceParametrized,
            String daoInterfaceExtensionName, String daoInterfaceExtensionPackage, String daoExceptionName, String daoExceptionPackage, 
            String dtoPackage, String endpointPackageName, String routerPackageName,  String routerName, String templateOutputDescription, ConfigurationDB configurationDB) {
        this.packageRootName = packageRootName;
        this.packageModelName = packageModelName;
        this.packagePersistenceName = packagePersistenceName;
        this.templatePath = templatePath;
        this.templateOutputName = templateOutputName;
        this.outputPath = outputPath;
        this.jsonFolderPath = jsonFolderPath;
        this.daoParametrized = daoParametrized;
        this.extensionName = extensionName;
        this.extensionPackage = extensionPackage;
        this.daoInterfaceParametrized = daoInterfaceParametrized;
        this.daoInterfaceExtensionName = daoInterfaceExtensionName;
        this.daoInterfaceExtensionPackage = daoInterfaceExtensionPackage;
        this.daoExceptionName = daoExceptionName;
        this.daoExceptionPackage = daoExceptionPackage;
        this.dtoPackage = dtoPackage;
        this.endpointPackageName = endpointPackageName;
        this.routerPackageName = routerPackageName;
        this.routerName = routerName;
        this.templateOutputDescription = templateOutputDescription;
        this.configurationDB = configurationDB;
    }

    public String getPackageRootName() {
        return packageRootName;
    }

    public void setPackageRootName(String packageRootName) {
        this.packageRootName = packageRootName;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTemplateOutputName() {
        return templateOutputName;
    }

    public void setTemplateOutputName(String templateOutputName) {
        this.templateOutputName = templateOutputName;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getJsonFolderPath() {
        return jsonFolderPath;
    }

    public void setJsonFolderPath(String jsonFolderPath) {
        this.jsonFolderPath = jsonFolderPath;
    }

    public String getPackageModelName() {
        return packageModelName;
    }

    public void setPackageModelName(String packageModelName) {
        this.packageModelName = packageModelName;
    }

    public String getPackagePersistenceName() {
        return packagePersistenceName;
    }

    public void setPackagePersistenceName(String packagePersistenceName) {
        this.packagePersistenceName = packagePersistenceName;
    }

    public Boolean getDaoParametrized() {
        return daoParametrized;
    }

    public void setDaoParametrized(Boolean daoParametrized) {
        this.daoParametrized = daoParametrized;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public void setExtensionName(String extensionName) {
        this.extensionName = extensionName;
    }

    public String getExtensionPackage() {
        return extensionPackage;
    }

    public void setExtensionPackage(String extensionPackage) {
        this.extensionPackage = extensionPackage;
    }

    public Boolean getDaoInterfaceParametrized() {
        return daoInterfaceParametrized;
    }

    public void setDaoInterfaceParametrized(Boolean daoInterfaceParametrized) {
        this.daoInterfaceParametrized = daoInterfaceParametrized;
    }

    public String getDaoInterfaceExtensionName() {
        return daoInterfaceExtensionName;
    }

    public void setDaoInterfaceExtensionName(String daoInterfaceExtensionName) {
        this.daoInterfaceExtensionName = daoInterfaceExtensionName;
    }

    public String getDaoInterfaceExtensionPackage() {
        return daoInterfaceExtensionPackage;
    }

    public void setDaoInterfaceExtensionPackage(String daoInterfaceExtensionPackage) {
        this.daoInterfaceExtensionPackage = daoInterfaceExtensionPackage;
    }

    public String getDaoExceptionName() {
        return daoExceptionName;
    }

    public void setDaoExceptionName(String daoExceptionName) {
        this.daoExceptionName = daoExceptionName;
    }

    public String getDaoExceptionPackage() {
        return daoExceptionPackage;
    }

    public void setDaoExceptionPackage(String daoExceptionPackage) {
        this.daoExceptionPackage = daoExceptionPackage;
    }

    public String getDtoPackage() {
        return dtoPackage;
    }

    public void setDtoPackage(String dtoPackage) {
        this.dtoPackage = dtoPackage;
    }
    
    public ConfigurationDB getConfigurationDB() {
        return configurationDB;
    }

    public void setConfigurationDB(ConfigurationDB configurationDB) {
        this.configurationDB = configurationDB;
    }

    public String getEndpointPackageName() {
        return endpointPackageName;
    }

    public void setEndpointPackageName(String endpointPackageName) {
        this.endpointPackageName = endpointPackageName;
    }

    public String getRouterPackageName() {
        return routerPackageName;
    }

    public void setRouterPackageName(String routerPackageName) {
        this.routerPackageName = routerPackageName;
    }

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public String getTemplateOutputDescription() {
        return templateOutputDescription;
    }

    public void setTemplateOutputDescription(String templateOutputDescription) {
        this.templateOutputDescription = templateOutputDescription;
    }
    
}
