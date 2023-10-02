* Para poder ejecutar Lippia necesitamos setear properties ubicadas en src/main/resources/config.properties. A continuacion, se enumeran dichas properties.

    Properties de proyecto.
	- crowdar.projectType= proyecto a ejecutar. (Ejemplo: WEB_CHROME) OBLIGATORIA
	- crowdar.projectType.driverCapabilities.jsonFile= ruta donde esta ubicado el json de las capabilities. OBLIGATORIA para proyectos web y mobile.

    Properties de proyecto opcionales.
	- crowdar.setupStrategy= estrategia de driver a utilizar. En caso de proyectos web con hub, utilizar: web.SeleniumGridStrategy. Default: NoneStrategy. OPCIONAL.
	- crowdar.driverHub= URL en donde esta alojado el hub para correr desde un servidor. En caso de ejecutar localmente, no es necesaria. Default: null. OPCIONAL.
	- crowdar.report.disable_screenshot_on_failure= true para NO mostrar imagen de error en reporte. Default: false. Default para API, WIN32 y DATABASE: true. OPCIONAL.
	- crowdar.report.stackTraceDetail= Stacktrace para proyectos que no son web o mobile. Default: false. Default para API, WIN32 y DATABASE: true. OPCIONAL.

    Properties de URL
    - base.api.url= URL comun a usar en un proyecto API. Ejemplo: http://api.pagos/ OPCIONAL.
    - URL= URL base para proyectos web. Ejemplo: https://github.com/Crowdar. OPCIONAL
    - db.url= URL de la base de datos. OPCIONAL.

    Properties de reporte Extent.
    - crowdar.extent.host.name= nombre del host en el que se ejecuta. OBLIGATORIO.
    - crowdar.extent.environment= nombre del ambiente en que se ejecuta. Ejemplo: Linux - Jenkins. OBLIGATORIO.
    - crowdar.extent.user.name= nombre del ejecutor. OBLIGATORIO.
    - crowdar.extent.report.path= ubicación del reporte dentro de la carpeta target. OPCIONAL.
    - crowdar.extent.report.name= nombre del archivo del reporte html. OPCIONAL.
    - crowdar.extent.report.document.title= titulo del reporte una vez dentro. OPCIONAL.
    - crowdar.extent.report.encoding= tipo de encoding. Default: UTF-8. OPCIONAL.
    - crowdar.extent.report.protocol= tipo de protocolo. http o https. Default: http. OPCIONAL.
    - crowdar.extent.report.theme= tipo de estilo: dark o standard. Default: standard. OPCIONAL.
    - crowdar.extent.report.timestampformat= tipo de formato en textos como duracion de ejecucion del caso. OPCIONAL.
    - crowdar.report.screenshotOnSuccess= mostrar screenshot en step pass. Default: false. OPCIONAL.

    Properties de timeout.
    - crowdar.wait.fluent.frecuency= frecuencia de espera para fluent. Default: 500
    - crowdar.wait.fluent.timeout= tiempo de espera para fluent. Default: 60
    - crowdar.wait.file.download.timeout= tiempo de espera para descargas. Default: 10
    - crowdar.wait.timeout= tiempo de espera general. Default: 20
    - crowdar.wait.script.timeout= tiempo de espera para finalizacion de un script. Default: 55
    - crowdar.wait.impicit.timeout= tiempo de espera para timeout implicitos. Default: 2
    - crowdar.wait.element.timeout= tiempo de espera para elementos. Default: 30
    - crowdar.wait.appStart.timeout= tiempo de espera para que la app inicie (mobile). Default: 70
    - crowdar.pattern.simpleDate= formato para fecha. Default: MM/dd/yyyy
    - crowdar.pattern.completeDate= formato para fecha y hora. Default: MM/dd/yyyy hh:mm aa

    Properties para casos de prueba de validacion de emails.
	- email.protocol= protocolo para sesion de email. Valores posibles: pop3s - imap - smtp. OPCIONAL.
    - email.user= email para autenticarse. OPCIONAL.
    - email.password= password para autenticarse. OPCIONAL.

    - crowdar.download.folder= path en donde se ubicaran los archivos descargados durante la ejecucion del test. OPCIONAL.


* 	dar ejemplos de distintas cinfiguraciones.
	api:
		- config.properties
			- crowdar.projectType=API
			- base.api.url=${base.api.url}
			
			(contar un poco que es lo que hace cada propety)

	
ProjectTypes:

 		Permite definir el tipo de driver con el que vamos a operar.
 		Se cuenta con ciertos templates predefinidos con las principales conbinaciones y uno generico para poder configurar a demanda.
 		En todos los casos es necesario definir la property crowdar.projectType.driverCapabilities.jsonFile en el cual definiremos la capabilities
 		con las que peticionaremos el driver.

 	WEB_CHROME
        crowdar.projectType=WEB_CHROME
        crowdar.projectType.driverCapabilities.jsonFile=src/main/resources/browsers/chromeCapabilities.json
        crowdar.setupStrategy=web.DownloadLatestStrategy

 	WEB_FIREFOX
        crowdar.projectType=WEB_FIREFOX
        crowdar.projectType.driverCapabilities.jsonFile=src/main/resources/browsers/firefoxCapabilities.json
        crowdar.setupStrategy=web.DownloadLatestStrategy

 	WEB_EDGE
        crowdar.projectType=WEB_EDGE
        crowdar.projectType.driverCapabilities.jsonFile=src/main/resources/browsers/edgeCapabilities.json
        crowdar.setupStrategy=web.DownloadLatestStrategy

 	WEB_IE
        crowdar.projectType=WEB_IE
        crowdar.projectType.driverCapabilities.jsonFile=src/main/resources/browsers/ieCapabilities.json
        crowdar.setupStrategy=web.DownloadLatestStrategy

 	WEB_SAFARI
        crowdar.projectType=WEB_SAFARI
        crowdar.projectType.driverCapabilities.jsonFile=src/main/resources/browsers/safariCapabilities.json
        crowdar.setupStrategy=web.DownloadLatestStrategy

 	MOBILE_ANDROID
        crowdar.projectType=MOBILE_ANDROID
        crowdar.projectType.driverCapabilities.jsonFile=src/main/resources/browsers/androidCapabilities.json

 	MOBILE_IOS
        crowdar.projectType=MOBILE_IOS
        crowdar.projectType.driverCapabilities.jsonFile=src/main/resources/browsers/iosCapabilities.json

 	API
        crowdar.projectType=API

 	DATABASE
        crowdar.projectType=DATABASE

 	WIN32
        crowdar.projectType=WIN32

 	GENERIC
 		Para poder utilizar este tipo de proyecto es necesario definir 2 properties adicionales en el config.properties del proyecto.
 		Las properties son crowdar.localDriverType y crowdar.remoteDriverType.

 		Ejemplo:
 		    + crowdar.projectType=GENERIC
 		    + crowdar.projectType.driverCapabilities.jsonFile=src/main/resources/browsers/genericCapabilities.json
 		    + crowdar.setupStrategy=web.DownloadLatestStrategy
	 		+ crowdar.projectType.localDriverType=org.openqa.selenium.chrome.ChromeDriver
	 		+ crowdar.projectType.remoteDriverType=org.openqa.selenium.remote.RemoteWebDriver
 
   ---------------------

El root del framework tiene todas las librerias que son utilizadas dentro del core para correr cualquier proyecto cliente

# CrowdarCoreFramework
El core del framework tiene todos las librerias propias de crowdar por ejemplo todas las Paginas base de cucumber, todos los artefactos necesarios para correr los test.

---

# LIPPIA configuration
Primer paso a dar para construir un projecto Lippia, en un proyecto cliente se deben configurar el pom.xml en el proyecto cliente y  heredar del root de la siguiente manera

`<parent>
        <groupId>com.crowdar</groupId>
        <artifactId>Crowd-Root-Framework</artifactId>
        <version>3.0.0</version>
</parent>`


y del core se agrega en la seccion de dependencias de la siguiente manera 

`<dependency>
            <groupId>com.crowdar</groupId>
            <artifactId>Crowd-Core-Framework</artifactId>
            <version>3.0.0</version>
</dependency>`

se debe configurar el repositorio de Lippia en la seccion de repositotios del pom.xml de la siguiente manera

`<repository>
           <id>crowdarRepo</id>
           <name>crowdar-repository-s3</name>
           <url>https://nexus-v3-repositories.automation.crowdaronline.com/repository/maven-s3/</url>
</repository>`

tendria que quedar el pom.xml de la siguiente manera : 
**https://github.com/Crowdar/lippia-web-sample-project/blob/master/pom.xml**
--

# Main configuration file : 

El archivo de configuracion principal es el config.properties, hay una clase llamada PropertyManager que nos permite obtener en los proyectos clientes
las propiedades desde config.properties este esta ubicado en el path
src\main\resources\config.properties 
la mayoria de las propiedades esta sobre escrita por variables maven definidas en los perfiles
**Ejemplo de config.properties**
https://github.com/Crowdar/lippia-web-sample-project/blob/master/src/main/resources/config.properties
--

# Maven Profiles

Los perfiles maven nos permiten definir la parametria, para configurar perfiles en proyectos clientes se tiene que definir en el pom.xml del projecto la siguiente seccion
`<profiles>
 <profile>
            <id>TestParallelJenkins</id>
            <activation>
            </activation>
            <properties>
         ...
            </properties>
        </profile>
</profiles>`
--

# Maven resources filters

hay una configuration en el root que lo que hace es mediante perfiles reemplazar cualquier variable del tipo ${variable} en los archivos ubicados
en el path src\main\resources\ que terminen en .properties este configuracion es 
 ` <filters>            
       <filter>src/main/resources/config.properties</filter>
  </filters>
  <resources>
       <resource>
           <filtering>true</filtering>
           <directory>src/main/resources</directory>
       </resource>
  </resources>`

Para mas informacion sobre Maven referirse a https://maven.apache.org/
---

# Cucumber and TestNG integration
Para ejecutar un proyecto de Lippia se requiere el runner de TestNG y Cucumber 
Para que un cliente tenga un configuracion deberia  tener lo siguiente 
una clase runner que extienda de com.crowdar.bdd.cukes.TestNgRunner para una configuracion secuencial 

`public class TestsRunner extends TestNgRunner {
	
}`

Configuacion en paralelo :
una clase runner que extienda de com.crowdar.bdd.cukes.TestNGParallelRunner para una configuracion en paralelo a partir de la version 1.4 del core y del root
por ejemplo 

public class ParallelTestRunner extends TestNGParallelRunner { 

}

tener una testng.xml apuntando a esa clase por ejemplo 

`<suite name="BDD Test Suite" verbose="1" parallel="methods" data-provider-thread-count="10" thread-count="10" configfailurepolicy="continue">
    <test name="Test 1" annotations="JDK" preserve-order="true">
        <classes>
            <class name="ParallelTestRunner"/>
        </classes>
    </test>
</suite> `

en este caso esta corriendo en paralelo  10 hilos para eso tenemos esta configuracion 
data-provider-thread-count="10" thread-count="10"

Para informacion adicional sobre TestNG referirse a https://testng.org/doc/
--
## Configuracion de cucumber :

 el proyecto cliente debe tener si o si un archivo properties del tipo 
 src/main/resources/cucumber.properties , las variables del tipo ${cucumber.option} es sobre escrita por la propiedad crowdar.cucumber.option en los perfiles 
 maven del proyecto como por ejemplo 
```<crowdar.cucumber.option>src/test/resources/features --glue com/crowdar/core --glue com/crowdar/bdd/cukes --glue ar/com/ --glue ar//hook --tags ${crowdar.cucumber.filter} --tags ~@Ignore --plugin com.crowdar.report.CucumberExtentReport  --plugin pretty</crowdar.cucumber.option>```
 
**Ejemplo de cucumber.properties**

https://github.com/Crowdar/lippia-web-sample-project/blob/master/src/main/resources/cucumber.properties


Para mas informacion sobre cucumber referirse a https://cucumber.io/docs

---

## EJEMPLOS
por favor revisar los siguientes ejemplos de proyectos  para tener mas información

+ https://github.com/Crowdar/lippia-web-sample-project
+ https://github.com/Crowdar/lippia-mobile-sample-project
+ https://github.com/Crowdar/Lippia-API-sample-project
+ https://github.com/Crowdar/lippia-low-code-sample-project


--- 

# DOCKER
para correr Lippia en un entorno de CI/CD, debe utilizarse la imagen Docker que se encuentra en DockerHub 

+ https://hub.docker.com/r/crowdar/lippia








