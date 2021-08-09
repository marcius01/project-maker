package ${dao.packageName};

import ${entity.packageName}.${entity.name};
import java.util.List;
<#if  dao.extensionPackage?has_content && dao.extensionName?has_content>import ${dao.packageName}.${dao.extensionPackage}.${dao.extensionName};</#if>
<#if  dao.exceptionPackage?has_content && dao.exceptionName?has_content>import ${dao.packageName}.${dao.exceptionPackage}.${dao.exceptionName};</#if>
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ${dao.name} <#if dao.extensionName?has_content>extends ${dao.extensionName} <#if dao.parametrized><${entity.name}></#if> </#if> implements ${dao.interfaceName} {

    private static Logger logger = LoggerFactory.getLogger(${dao.name}.class);
<#if dao.parametrized>
    public ${dao.name}() {
        super(${entity.name}.class);
    }
</#if>

<#list entity.uniqueSearchs as uniqueSearch>
    <#if uniqueSearch?has_content>
    @Override
    public ${entity.name} findBy${uniqueSearch.name}(${uniqueSearch.type} ${uniqueSearch.name}) <#if dao.exceptionName?has_content>throws ${dao.exceptionName}</#if>{
        if(logger.isDebugEnabled()) logger.debug("${uniqueSearch.name}: " + ${uniqueSearch.name});
        Criteria criteria = getSession().createCriteria(${entity.name}.class);
        criteria.add(Restrictions.eq("${uniqueSearch.name}", ${uniqueSearch.name}));
        <#if uniqueSearch.orderProperty??>
        criteria.addOrder(Order.asc("${uniqueSearch.orderProperty}"));
        </#if>
        List<${entity.name}> list = criteria.list();                
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    </#if>
</#list>

    <#if entity.listSearch?has_content>
    @Override
    public ${entity.name} findBy${entity.listSearch.name}(${entity.listSearch.type} ${entity.listSearch.name}) <#if dao.exceptionName?has_content>throws ${dao.exceptionName}</#if>{
        if(logger.isDebugEnabled()) logger.debug("${entity.listSearch.name}: " + ${entity.listSearch.name});
        Criteria criteria = getSession().createCriteria(${entity.name}.class);
        criteria.add(Restrictions.eq("${entity.listSearch.name}", ${entity.listSearch.name}));
        <#if entity.listSearch.orderProperty?has_content>
        criteria.addOrder(Order.asc("${entity.listSearch.orderProperty}"));
        </#if>
        return criteria.list();   

    }
    </#if>

}
