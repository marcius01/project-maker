package tech.skullprogrammer.projectmaker.model.fm;

import java.util.List;

public class DTOClass extends AbstractClass{

    private List<String> imports;    
    private List<PropertyClass> properties;

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public List<PropertyClass> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyClass> properties) {
        this.properties = properties;
    }

}
