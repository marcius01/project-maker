package tech.skullprogrammer.projectmaker.model.fm;

public class DAOClass {

    private String packageName;
    private String name;
    private String interfaceName;
    private String interfaceExtensionName;
    private String interfaceExtensionPackage;
    private String extensionName;
    private String extensionPackage;
    private String exceptionName;
    private String exceptionPackage;
    private boolean parametrized;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceExtensionName() {
        return interfaceExtensionName;
    }

    public void setInterfaceExtensionName(String interfaceExtensionName) {
        this.interfaceExtensionName = interfaceExtensionName;
    }

    public String getInterfaceExtensionPackage() {
        return interfaceExtensionPackage;
    }

    public void setInterfaceExtensionPackage(String interfaceExtensionPackage) {
        this.interfaceExtensionPackage = interfaceExtensionPackage;
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

    public boolean isParametrized() {
        return parametrized;
    }

    public void setParametrized(boolean parametrized) {
        this.parametrized = parametrized;
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public String getExceptionPackage() {
        return exceptionPackage;
    }

    public void setExceptionPackage(String exceptionPackage) {
        this.exceptionPackage = exceptionPackage;
    }

}
