# Project Maker

Project Maker is a java library that allow to automatically generate a working project starting from a template project, a configuration file and the json representation of the beans.

Project Maker allows you to create beans and annotate them with JPA annotations, analyzing the associations between the different beans and the type of properties. Starting from the Entities, DAOs are created to manage persistence. Finally, the DTOs are created. Then the endpoints are created, if the project is of the WEB / REST type, and the routing manager.

Project maker uses FreeMarker for the generation of templates, this allows the extensibility of the system to new types of projects.

## Installation

The library is not yet available on maven central, for this reason at this initial stage it is necessary to download the project and generate the jar if necessary. Alternatively, it is possible to directly depend on the project.

## Usage
For some easy examples of use it is possible to analyze the package "example"

```java
```

## Configuration
To use the Project Maker it is necessary to configure various aspects, which concern Json, project characteristics, templates

### Json ###

To create the beans, you need to specify the characteristics and links with other beans through a JSON schema.

For example as follows, we have two json files called Account and User .

#### Account ####
```json
{
  "type":"object",
  "properties": {
    "id_ID": {
      "type": "string",
      "format": "utc-millisec"
    },
    "name": {
      "type": "string"
    },
    "creation_DATE": {
      "type": "string",
      "format": "date-time"
    },
    "dateAndHoursLastOperation_TIMESTAMP": {
      "type": "string",
      "format": "date-time"
    },
    "user":
    {
        "type": "object",
        "$ref": "User.json"
    }
  }
} 
```
#### User ####
```json
{
    "type": "object",
    "properties":
    {
      "id_ID": {
      "type": "string",
      "format": "utc-millisec"
    },
        "name":
        {
            "type": "string"
        },
        "Ssn_UNIQUE":
        {
            "type": "string",
            "uniqueItems": true
        },
        "account":
        {
            "type": "array",
            "items":
            {
                "$ref": "Account.json"
            }
        }
    }
}
```
To facilitate the mapping through JPA annotations, it is possible to use 4 types of suffixes for the attributes:
1) **_ID**  to map an id
2) **_UNIQUE** to map an attribute like unique
3) **_DATE** to map an attribute with the DATE type
4) **_TIMESTAMP** to map an attribute with the type

### config.properties ###

The possible parameters that can be customized for the project are the following:

##### config generic info #####

* package.root.name = _(String) root package for the generated project_
* package.model.name = _(String) name of the model package_
* package.persistence.name = _(String) name of the persistence package_
* package.endpoint.name = _(String) name of the control package_

##### config DAO generation #####

* datamodel.dao.parametrized = _(Boolean) if generated DAO must be parametrized for the entity
* datamodel.dao.extension.name = _(String) name of Class extended by the DAO_
* datamodel.dao.extension.package = _(String) name of package of the Class extended by the DAO_
* datamodel.dao.interface.parametrized = _(Boolean) if DAO implements a parametrized interface_
* datamodel.dao.interface.extension.name = _(String) name of Class implemented by the DAO_
* datamodel.dao.interface.extension.package = _(String) name of package of the Class implemented by the DAO_
* datamodel.dao.exception.name = _(String) name of Exception thrown by the DAO_
* datamodel.dao.exception.package = _(String) name of package of the Exception thrown by the DAO_
* datamodel.dto.package = _(String) name of package for the generated DTO's_
* datamodel.router.package =  _(String) name of package of the Router (REST Applications)_
* datamodel.router.name = _(String) name of Router Class_

##### config DB (use config_db.properties file) #####

* db.project = _(Boolean) if project use a DB (if true, config_db.properties is used)_

##### config Folders #####

* json.folder.path = _(String) folder in which are placed json schemas_
* template.path = _(String) folder in which is placed project template_
* template.output.name =  _(String) generated project name_
* template.output.description = _(String) description for generated project_
* output.path = _(String) output folder for the generated project_

### config_db.properties ###

The possible parameters that can be customized for the DB are the following:

* db.type = _(String) db type used, used for sub protocol in JDBC URI_
* db.host = _(String) db host (ex. localhost)_
* db.port = _(String) db port (ex. 5432)_
* db.name = _(String) db name_
* db.username = _(String) db username_
* db.password = _(String) db password_
* db.tmpDb = _(String) db used for connection for subsidiary operations_
* db.driver = _(String) db driver name (ex. org.postgresql.Driver)_
* db.schemaGeneratorClass = _(String) if used, complete name of schema generator class in the generated project (ex. org.example.Generator)_
* db.dialect = _(String) db dialect (ex. org.hibernate.dialect.PostgreSQLDialect)_

### FreeMarker Templates ###

Project Maker uses the FreeMarker framework, for this purpose, apart from the beans, all components are built starting from freemarker templates.

Project Maker provides a number of generic templates that can be used to build almost any project.

However, some components are too specific to be easily generalized, so the following components have a template built specifically for the project to be generated:

* Endpoints
* Router

It is possible to build your own versions of the templates in question, knowing that the data model is the following. 

#### For Endopoints ####

##### EndpointClass #####
```
endpoint
 -  (String) packageName
 -  (String) name
 -* (String) imports
 -  (String) rootPackage
 -  (String) requestPath
 -  (DAOClass) associatedDAO
 -  (EntityClass) associatedEntity
 -  (DTOClass) associatedDTO
```
##### DAOClass #####
```
 - (String) packageName
 - (String) name    
 - (String) interfaceName
 - (String) interfaceExtensionName
 - (String) interfaceExtensionPackage
 - (String) extensionName
 - (String) extensionPackage
 - (String) exceptionName
 - (String) exceptionPackage
 - (boolean) parametrized
```
##### EntityClass #####
```
 -  (String) packageName
 -  (String) name
 -* (SearchType) uniqueSearchs
```
##### SearchType #####
```
 - (String) name
 - (String) type
 - (String) orderParameter
```
##### DTOClass #####
```
 -  (String) packageName
 -  (String) name
 -* (String> imports    
 -* (PropertyClass) properties
```
##### PropertyClass #####
```
 - (String) name
 - (String) type
 - (String) parametrizedType
```

#### For Router ####
##### RouterClass #####
```
 -  (String) packageName
 -  (String) name
 -  (String) rootPackage;
 -* (EndpointClass) endpoints;
```
## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

Project Maker is licensed under the GNU GPL License.

See the LICENSE file for more details!
