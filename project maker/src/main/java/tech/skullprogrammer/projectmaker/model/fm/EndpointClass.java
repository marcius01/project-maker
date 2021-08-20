package tech.skullprogrammer.projectmaker.model.fm;

import java.util.List;

public class EndpointClass extends AbstractClass{
    private List<String> imports;    
    private String rootPackage;
    private DAOClass associatedDAO;
    private EntityClass associatedEntity;
    private DTOClass associatedDTO;
    private String requestPath;

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public String getRootPackage() {
        return rootPackage;
    }

    public void setRootPackage(String rootPackage) {
        this.rootPackage = rootPackage;
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

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

}
