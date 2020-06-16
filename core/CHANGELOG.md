# Changelog 
All notable changes to this project will be documented in this file.

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
