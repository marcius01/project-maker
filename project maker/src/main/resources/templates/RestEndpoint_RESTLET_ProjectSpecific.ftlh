package ${endpoint.packageName};

import java.util.List;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
<#-- Lines below are specific to the project -->
import ${endpoint.rootPackage}.utility.RestUtility;
<#-- 
import it.unibas.sondaggi.rest.persistenza.hibernate.DAOAziendaHibernate;
import it.unibas.sondaggi.rest.persistenza.DAOException;
import it.unibas.sondaggi.rest.persistenza.IDAOAzienda;
-->
<#list endpoint.imports as import>
import ${import};
</#list>


public class ${endpoint.name} extends ServerResource {

    private ${endpoint.associatedDAO.interfaceName} ${endpoint.associatedDAO.name?uncap_first} = new ${endpoint.associatedDAO.name}()   ;
    private Mapper mapper = new DozerBeanMapper();
    private static Logger logger = LoggerFactory.getLogger(${endpoint.name}.class);

    @Get
    public Representation get${endpoint.associatedDTO.name}() {
        try {            
            List<${endpoint.associatedEntity.name}> result = ${endpoint.associatedDAO.name?uncap_first}.findAll(); 
            List<${endpoint.associatedDTO.name}> resultDTO = RestUtility.map(mapper, result, ${endpoint.associatedDTO.name}.class);
            return RestUtility.printResponse(resultDTO);
        } catch (DAOException ex) {
            logger.error("Exception: " + ex);
            return RestUtility.printError("Problems with the operation");
        }
    }

}