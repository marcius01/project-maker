package ${router.packageName};

<#list router.endpoints as endpoint>
import ${endpoint.packageName}.${endpoint.name};
</#list>
import ${router.rootPackage}.filter.FiltroHibernate;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class ${router.name} extends Application {

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        FiltroHibernate filtroHibernate = new FiltroHibernate(getContext());
        filtroHibernate.setNext(router);
        <#list router.endpoints as endpoint>
        router.attach("${endpoint.requestPath}", ${endpoint.name}.class);
        </#list>
        return filtroHibernate;
    }

}

