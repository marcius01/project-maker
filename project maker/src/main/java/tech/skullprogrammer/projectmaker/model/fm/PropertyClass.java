package tech.skullprogrammer.projectmaker.model.fm;


public class PropertyClass {
    private String type;
    private String name;
    private String parametrizedType;

    public PropertyClass(String type, String name, String parametrizedType) {
        this.type = type;
        this.name = name;
        this.parametrizedType = parametrizedType;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getParametrizedType() {
        return parametrizedType;
    }
        
}
