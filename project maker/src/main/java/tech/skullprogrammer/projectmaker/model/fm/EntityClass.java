package tech.skullprogrammer.projectmaker.model.fm;

import java.util.List;


public class EntityClass {

    private String packageName;
    private String name;
    private SearchType uniqueSearch;
    private SearchType listSearch;
    private List<EntityProperty> properties;

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

    public SearchType getUniqueSearch() {
        return uniqueSearch;
    }

    public void setUniqueSearch(SearchType uniqueSearch) {
        this.uniqueSearch = uniqueSearch;
    }

    public SearchType getListSearch() {
        return listSearch;
    }

    public void setListSearch(SearchType listSearch) {
        this.listSearch = listSearch;
    }
    
    
}
