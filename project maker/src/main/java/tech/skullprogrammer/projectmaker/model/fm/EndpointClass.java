package tech.skullprogrammer.projectmaker.model.fm;

import java.util.List;

public class EndpointClass extends AbstractClass{
    private List<String> imports;    
    private String mainPackage;
    private DAOClass associatedDAO;
    private EntityClass associatedEntity;
    private DTOClass associatedDTO;

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public String getMainPackage() {
        return mainPackage;
    }

    public void setMainPackage(String mainPackage) {
        this.mainPackage = mainPackage;
    }
    
    public DAOClass getAssociatedDAO() {
        return associatedDAO;
    }

    public void setAssociatedDAO(DAOClass associatedDAO) {
        this.associatedDAO = associatedDAO;
    }

    public DTOClass getAssociatedDTO() {
        return associatedDTO;
    }

    public void setAssociatedDTO(DTOClass associatedDTO) {
        this.associatedDTO = associatedDTO;
    }

    
    public EntityClass getAssociatedEntity() {
        return associatedEntity;
    }

    public void setAssociatedEntity(EntityClass associatedEntity) {
        this.associatedEntity = associatedEntity;
    }

}
