package ${pojo.package};

import java.util.List;
<#--import javax.persistence.CascadeType;-->
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
<#--import javax.persistence.OneToMany;-->

@Entity
public class ${pojo.name} {
    
    private Long id;
    <#list propeties as property>
        private ${property.type} ${property.name};
    </#list>
    
    <#--private List<Risposta> risposte;-->

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    <#list propeties as property>
        <#if (${preperty.unique})>
            @Column(unique = true) 
        </#if>
        <#if (${preperty.date})>
            @Temporal(TemporalType.TIMESTAMP)
        </#if>        
        public ${property.type} ${property.methodGet} {
            return ${property.name};
        }

        public void ${property.methodSet} {
            return ${property.name};
        }
    </#list>

<#--
    @OneToMany(mappedBy = "azienda", cascade = CascadeType.ALL)
    public List<Risposta> getRisposte() {
        return risposte;
    }

    public void setRisposte(List<Risposta> risposte) {
        this.risposte = risposte;
    }
-->
}