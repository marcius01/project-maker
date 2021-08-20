package tech.skullprogrammer.projectmaker.model.fm;

import java.util.List;


public class RouterClass extends AbstractClass{

    private String rootPackage;
    private List<EndpointClass> endpoints;

    public String getRootPackage() {
        return rootPackage;
    }

    public void setRootPackage(String rootPackage) {
        this.rootPackage = rootPackage;
    }

    public List<EndpointClass> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<EndpointClass> endpoints) {
        this.endpoints = endpoints;
    }
    
    
    
}
