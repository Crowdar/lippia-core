# Changelog 
All notable changes to this project will be documented in this file.

## 3.2.3.4 (28/01/2021)
*BUGFIX index out of bound exception JsonStringFormatter

## 3.2.3.4 (22/01/2021)
* Methodservicea abstraction to configure specifics strategies between with or with out ssl verification.
* MethodServiceEnum to set that strategy
* add methodServiceEnum as part of EndpointConfiguration.
* MethodService junit coverage

## 3.2.3.3 (15/01/2021)
* Adding firefox support to dockerfile
* improve locator manager by adding String ... args to be replaced in properties.  "%s" in propertie values it will be replaced by recivided args


##  completar las versiones faltantes.


## 3.2.2 (08/09/2020)
* Remove firebase, angular dependencies
* Remove Database and Query classes
* Fix step CommonSteps in api
* Add functionality to allow sending files in body request

## 3.2.1 (24/08/2020)
* Fix problem with threads in ActionManager with method clean() and usage in BasicHook
* Add dismissMobileDriver in DriverManager

## 3.2.0 (10/08/2020)
* LocatorManager refactor
  - catch exceptions
  - split and find locator file (max 1 folder in locators folder)
  - getProperty method, if is another locator file loaded, load the requested
* Add LocatorTypesEnum
* Add getName() method in ProjectTypeEnum with name of driver instance + call it in DriverManager.getName()
* Method loadLocators in the constructor PageBase and getLocator that returns By searching in LocatorTypesEnum
* Refactor PageBase, PageBaseMobile and PageBaseWeb in methods to support parameter String locatorName and deprecated in old methods.
* Replace in places that were using getRootLogger() for Logger.getLogger(class).
* Api fix in CommonSteps (Delete ':')
* Remove unused methods in Utils class.
* Add ActionManager, WebActionManager and MobileActionManager with all static methods and using locator manager. (This replaces PageBase).
* Fix fluent time in Constants and remove constants without use.

## 3.1.2 (04/08/2020)
* Move dragAndDrop method from PageBase to PageBaseWeb: these methods is specific for web project.
* Add waitForElementClickable to PageBase.
* Add scroll methods in PageBaseMobile: scroll android with text, accessibility id, resource id and class name; scroll ios.

## 3.1.1 (27/07/2020)
* Add to CommonSteps 
  - step that allows to validate response with different method (Then expected response is obtained in <entity> with the method <method>)
  - step to validate api text response (Then text <text> was obtained in response)
* Refactor in JsonUtils, now methods: getJson(), getJSONFromFile() and getJSONFromPath() thorws IOException
* Update throw of getJSONFromFile() in MethodsService and EmailPropertiesEnum (catch that and assert fail)
* Replace in Request: Object urlParameters and headers with Map (update MethodsService and RestClient for this change).
* Catch invocation target exception in CommonSteps (validate response fields) and fail that test in this case.
* RestClient instance in MethodsService instead of RestClient.java.
* Add logs in RestClient response.
* Add functionally download attachment file in EmailService
* Add validates list of text present in PDFManager
    
## 3.1.0.1 (15/07/2020)
* change groupId from com.crowdar to io.lippia
* bitbucket pipeline configuration
  - when push to staging branch
    - deploy artifacts in private nexus repository when push over staging branch
  - when push to master branch  
    - deploy artifacts in OSSRH (maven central)
    - docker image build and push to docker hub
    - push code to github
  
## 3.1.0 (02/06/2020)

* extent dependencies extraction from core
* related to abstraction to improove driver capabilities in execution time.
* add strategy to initialize driver in hook.
* fix injector instances in parallel context.
* update distribution management for mvn deploy and SNAPSHOTS
* adding hoooks tags feature
* MEGA CLEAN UP!!!!!
* rename correctly the Secuencial tesng runner
* remove null validation for driver instance process
* clean ThreadCache by the correct way
* comment logs generated to detect concurrency problems
* remove all sharedDriver references
* remove timezone sended by capabilities

## 3.0.0.3 (01/06/2020)

* Se agrega metodo splitIntoMap a MapUtils.
* Se agregan metodos de validacion de response en CommonSteps api con entity configuration global (se pasa desde el cliente en config properties)
* Se agregan implementacion de validateFields en MethodsService para implementar, en caso de ser necesario, en el cliente.

## 3.0.0.2 (30/05/2020)

* Se agrega ValidateUtils (validacion de 2 objetos o 2 listas).
* Se agrega CommonSteps para API con steps en comun como: validar response vacio, validar status, etc.
* Fix en API con casos de errores 500 y errores 400.
* Refactor en MethodsService (API). Se crea metodo getCompleteUrl en Request (url + endpoint).
* Elimino package driver.factory
* WriteListOutputs se genera con "," separator para poder importarlo en excel como csv.

## 3.0.0.1 (28/05/2020)

* Se agrega DateUtils.
* Se agrega MapUtils.
* Fix projectTypeEnum para API y Database en seteo de properties (string en lugar de boolean).
* Se agrega metodo writeListOutputs en FileUtils para que a partir de 2 listas, se escriba el output en archivos txt (esperado vs actual) en target/output/

## 3.0.0 (07/05/2020)

* Se agrega NoneStrategy como default strategy en caso de que el cliente no le asigne ningun valor.
* Se ponen las properties crowdar.report.disable_screenshot_on_failure y crowdar.report.stackTraceDetail como true por default para: API y Database.
* Se eliminan las clases AutomationConfiguration, BrowserConfiguration, ChromeUtils y MobilePlatformConfiguration
* LocalWebExecutionStrategy deprecated
* ExtentReportManager -> las properties "crowdar.extent.report.path" y "crowdar.extent.report.name" ya no son obligatorias, en caso de estar vacias, se usan valores default.
* Es obligatorio el uso de capabilities como json para proyectos mobile y web.
* Update readme.

## 2.0.5
 
* Agrego metodo parseArrayToList a Utils en donde convierte un array de Object a List de Object.
* Fix en api para retornar response de array. Por ejemplo, desde el proyecto hijo: get(json, Clase[].class) cuando se espera un array de respuesta.
* Agrego clase ObjectManager que permite agregar objectos de cualquier tipo y reutilizarlos en otros steps como validaciones o reemplazo de datos. 

## 2.0.4

WEB:

* Agrego getJsonFromPath en JsonUtils para obtener un json string desde el path string
* Agrego clase TestMain para hacer pruebas en java de manera rápida .
* Agrego imap, pop3s y smtp jsons default para configurar properties de email (si el cliente agrega uno, se pisa) .
* EmailUtils pasa a llamarse EmailService . Correcciones en EmailService para obtener las properties host y port desde la sesion .
* Se modifica EmailPropertiesEnum con la nueva configuracion de las properties a partir del json de properties de email

API:

* Se agrega a Utils getRandom, getRandomLetters y removeFirstAndLastChar
* Se agrega a JsonUtils isJSONValid y se corrige el uso de ObjectMapper. Se modifica el serialize para que no convierta el json object en una List de array
* En Request.java se utiliza removeFirstAndLastChar y se agrega soporte del body tipo txt
* Comportamiento de getRestClient ahora lo tiene MethodsService en lugar del constructor de RestClient.java
* En ApiManager.java se agrega método getListResponseFromJsonFile para permitir devolver lista de response
* Se agrega en RestClient que soporte, desde el header, cualquier tipo de response (no permitía devolver texto por ejemplo)
