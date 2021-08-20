package tech.skullprogrammer.projectmaker.model.fm;

import java.util.Map;

public class FMDataModels {

    private final Map<String, Map<String, Object>> daosRoots;
    private final Map<String, Map<String, Object>> dtosRoots;
    private final Map<String, Object> configurationRoot;
    private final Map<String, Map<String, Object>> endpoints;
    private final Map<String, Object> routerRoot;
    private final Map<String, Object> projectRoot;

    public FMDataModels(Map<String, Map<String, Object>> daosRoots, Map<String, Map<String, Object>> dtosRoots,
            Map<String, Object> configurationRoot, Map<String, Map<String, Object>> endpoints,
            Map<String, Object> routerRoot, Map<String, Object> projectRoot) {
        this.daosRoots = daosRoots;
        this.dtosRoots = dtosRoots;
        this.configurationRoot = configurationRoot;
        this.endpoints = endpoints;
        this.routerRoot = routerRoot;
        this.projectRoot = projectRoot;
    }

    public Map<String, Map<String, Object>> getDaosRoots() {
        return daosRoots;
    }

    public Map<String, Map<String, Object>> getDtosRoots() {
        return dtosRoots;
    }

    public Map<String, Map<String, Object>> getEndpoints() {
        return endpoints;
    }
    
    public Map<String, Object> getConfigurationRoot() {
        return configurationRoot;
    }

    public Map<String, Object> getRouterRoot() {
        return routerRoot;
    }

    public Map<String, Object> getProjectRoot() {
        return projectRoot;
    }
    
}
