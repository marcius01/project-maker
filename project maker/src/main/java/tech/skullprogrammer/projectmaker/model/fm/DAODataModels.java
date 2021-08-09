package tech.skullprogrammer.projectmaker.model.fm;

import java.util.Map;


public class DAODataModels {
    
    Map<String, Map<String, Object>> daosRoots;
    Map<String, Object> configurationRoot;

    public DAODataModels(Map<String, Map<String, Object>> daosRoots, Map<String, Object> configurationRoot) {
        this.daosRoots = daosRoots;
        this.configurationRoot = configurationRoot;
    }

    public Map<String, Map<String, Object>> getDaosRoots() {
        return daosRoots;
    }

    public Map<String, Object> getConfigurationRoot() {
        return configurationRoot;
    }
    
    

}
