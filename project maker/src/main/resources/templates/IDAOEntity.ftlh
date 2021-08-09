package ${dao.packageName};

import ${entity.packageName}.${entity.name};
<#if  dao.interfaceExtensionPackage?has_content && dao.interfaceExtensionName?has_content>import ${dao.packageName}.${dao.interfaceExtensionPackage}.${dao.interfaceExtensionName};</#if>
<#if  dao.exceptionPackage?has_content && dao.exceptionName?has_content>import ${dao.packageName}.${dao.exceptionPackage}.${dao.exceptionName};</#if>

public interface ${dao.interfaceName} <#if dao.interfaceExtensionName?has_content>extends ${dao.interfaceExtensionName} <#if dao.parametrized><${entity.name}></#if> </#if>{


<#list entity.uniqueSearchs as uniqueSearch>
    <#if uniqueSearch??>    
    public ${entity.name} findBy${uniqueSearch.name}(${uniqueSearch.type} ${uniqueSearch.name}) <#if dao.exceptionName?has_content>throws ${dao.exceptionName}</#if>;
    </#if>
</#list>

    <#if entity.listSearch??>
    public ${entity.name} findBy${entity.listSearch.name}(${entity.listSearch.type} ${entity.listSearch.name}) <#if dao.exceptionName?has_content>throws ${dao.exceptionName}</#if>;
    </#if>

}
