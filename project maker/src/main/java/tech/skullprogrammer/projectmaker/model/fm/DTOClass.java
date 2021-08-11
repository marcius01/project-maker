package tech.skullprogrammer.projectmaker.model.fm;

import java.util.List;

public class DTOClass {

    private String packageName;
    private List<String> imports;    
    private String name;
    private List<PropertyClass> properties;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PropertyClass> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyClass> properties) {
        this.properties = properties;
    }

}
