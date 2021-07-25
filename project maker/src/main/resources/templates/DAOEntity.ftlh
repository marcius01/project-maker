package ${dao.packageName};

import ${entity.packageName}.${entity.name};
import ${dao.packageName}.${dao.interfaceName};
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ${dao.name} extends DAOGenericoHibernate<${entity.name}> implements ${dao.interfaceName} {

    private static Logger logger = LoggerFactory.getLogger(${dao.name}.class);

    public ${dao.name}() {
        super(${entity.name}.class);
    }

    <#if entity.uniqueSearch??>
    @Override
    public ${entity.name} findBy${entity.uniqueSearch.name}(${entity.uniqueSearch.type} ${entity.uniqueSearch.name}) {
        if(logger.isDebugEnabled()) logger.debug("${entity.uniqueSearch.name}: " + ${entity.uniqueSearch.name});
        Criteria criteria = getSession().createCriteria(${entity.name}.class);
        criteria.add(Restrictions.eq("${entity.uniqueSearch.name}", ${entity.uniqueSearch.name}));
        <#if entity.uniqueSearch.orderProperty??>
        criteria.addOrder(Order.asc("${entity.uniqueSearch.orderProperty}"));
        </#if>
        List<${entity.name}> list = criteria.list();                
        if (${entity.name}.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    </#if>

    <#if entity.listSearch??>
    @Override
    public ${entity.name} findBy${entity.listSearch.name}(${entity.listSearch.type} ${entity.listSearch.name}) {
        if(logger.isDebugEnabled()) logger.debug("${entity.listSearch.name}: " + ${entity.listSearch.name});
        Criteria criteria = getSession().createCriteria(${entity.name}.class);
        criteria.add(Restrictions.eq("${entity.listSearch.name}", ${entity.listSearch.name}));
        <#if entity.listSearch.orderProperty??>
        criteria.addOrder(Order.asc("${entity.listSearch.orderProperty}"));
        </#if>
        return criteria.list();   

    }
    </#if>

}
