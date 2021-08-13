package tech.skullprogrammer.projectmaker.model.fm;

import java.util.ArrayList;
import java.util.List;

public class EntityClass extends AbstractClass{

    private List<SearchType> uniqueSearchs = new ArrayList<>();
    private SearchType listSearch;
    private List<EntityProperty> properties;

    public List<SearchType> getUniqueSearchs() {
        return uniqueSearchs;
    }

    public void setUniqueSearchs(List<SearchType> uniqueSearchs) {
        this.uniqueSearchs = uniqueSearchs;
    }
    
    public void addUniqueSearch(SearchType uniqueSearch) {
        this.uniqueSearchs.add(uniqueSearch);        
    }
    
    public SearchType getListSearch() {
        return listSearch;
    }

    public void setListSearch(SearchType listSearch) {
        this.listSearch = listSearch;
    }

}
