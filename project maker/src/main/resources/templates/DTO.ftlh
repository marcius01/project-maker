package ${dto.packageName};

<#list dto.imports as import>
import ${import};
</#list>

public class ${dto.name} {
    
    <#list dto.properties as property>
        private ${property.type?no_esc} ${property.name};
    </#list>

    <#list dto.properties as property>
        public ${property.type?no_esc} get${property.name?cap_first}() {
            return ${property.name};
        }

        public void set${property.name?cap_first}(${property.type?no_esc} ${property.name}) {
            this.${property.name} = ${property.name};
        }
    </#list>

}