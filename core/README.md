---------------------  

 ProjectTypes:

 		Permite definir en tipo de driver con el que vamos a operar.
 		Se cuenta con ciertos templates predefinidos con las principales conbinaciones y uno generico para poder configurar a demanda.
 		En todos los casos es necesario definir la property crowdar.projectType.driverCapabilities.jsonFile en el cual definiremos la capabilities
 		con las que peticionaremos el driver.

 	WEB_CHROME 
 		local: org.openqa.selenium.chrome.ChromeDriver
 		remote: org.openqa.selenium.remote.RemoteWebDriver  
 		 
 	WEB_FIREFOX
 		local: org.openqa.selenium.firefox.FirefoxDriver
 		remote: org.openqa.selenium.remote.RemoteWebDriver 
 		
 	WEB_EDGE
 		local: org.openqa.selenium.edge.EdgeDriver
 		remote: org.openqa.selenium.remote.RemoteWebDriver
 	
 	WEB_IE
 		local: org.openqa.selenium.ie.InternetExplorerDriver
 		remote: org.openqa.selenium.remote.RemoteWebDriver
 		
 	WEB_SAFARI
 		local: org.openqa.selenium.safari.SafariDriver
 		remote: org.openqa.selenium.remote.RemoteWebDriver
 		
 	MOBILE_ANDROID
 		local: no soportado
 		remote: io.appium.java_client.android.AndroidDriver
 		
 	MOBILE_IOS
 		local: no soportado
 		remote: io.appium.java_client.ios.IOSDriver
 	
 	GENERIC
 		Para poder utilizar este tipo de proyecto es necesario definir 2 properties en el config.properties del proyecto.
 		Las properties son crowdar.localDriverType y crowdar.remoteDriverType.
 		 
 		Ejemplo: 
	 		+ crowdar.projectType.localDriverType=org.openqa.selenium.chrome.ChromeDriver
	 		+ crowdar.projectType.remoteDriverType=org.openqa.selenium.remote.RemoteWebDriver	
 
   ---------------------

Definiremos algunas configuraciones basicas a modo de ejemplo relacionadas al tipo de proyecto;

 crowdar.projectType=WEB_FIREFOX
 crowdar.projectType.driverCapabilities.jsonFile=src/main/resources/browsers/firefoxCapabilities.json
 
 	ó
 
 crowdar.projectType=WEB_CHROME
 crowdar.projectType.driverCapabilities.jsonFile=src/main/resources/browsers/chromeCapabilities.json
 
  ó
  
 crowdar.projectType=GENERIC
 crowdar.projectType.driverCapabilities.jsonFile=src/main/resources/browsers/chromeCapabilities.json
 crowdar.projectType.localDriverType=org.openqa.selenium.chrome.ChromeDriver
 crowdar.projectType.remoteDriverType=org.openqa.selenium.remote.RemoteWebDriver
 
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
        <version>1.6.1</version>
</parent>`


y del core se agrega en la seccion de dependencias de la siguiente manera 

`<dependency>
            <groupId>com.crowdar</groupId>
            <artifactId>Crowd-Core-Framework</artifactId>
            <version>1.6.2</version>
</dependency>`

se debe configurar el repositorio de Lippia en la seccion de repositotios del pom.xml de la siguiente manera

`<repository>
           <id>crowdarRepo</id>
           <name>crowdar-repository-s3</name>
           <url>http://nexus-v3-repositories.automation.crowdaronline.com/repository/maven-s3/</url>
</repository>`

tendria que quedar el pom.xml de la siguiente manera : 
**https://bitbucket.org/crowdarautomation/cucumberexampleproject/src/master/pom.xml**
--

# Main configuration file : 

El archivo de configuracion principal es el config.properties, hay una clase llamada PropertyManager que nos permite obtener en los proyectos clientes
las propiedades desde config.properties este esta ubicado en el path
src\main\resources\config.properties 
la mayoria de las propiedades esta sobre escrita por variables maven definidas en los perfiles
**Ejemplo de config.properties**
https://bitbucket.org/crowdarautomation/cucumberexampleproject/src/master/src/main/resources/config.properties

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


---

# Cucumber and TestNG integration
Para que un cliente tenga un configuracion Incayuyo cucumber deberia  tener lo siguiente 
una clase runner que extienda de com.crowdar.bdd.cukes.TestNgRunner para una configuracion secuencial 

`public class SupervielleTestsRunner extends TestNgRunner {
	
}`

Configuacion en paralelo :
una clase runner que extienda de com.crowdar.bdd.cukes.TestNGParallelRunner para una configuracion en paralelo a partir de la version 1.4 del core y del root
por ejemplo 

public class SupervielleParallelTestRunner extends TestNGParallelRunner { 

}

tener una testng.xml apuntando a esa clase por ejemplo 

`<suite name="BDD Test Suite" verbose="1" parallel="methods" data-provider-thread-count="10" thread-count="10" configfailurepolicy="continue">
    <test name="Test 1" annotations="JDK" preserve-order="true">
        <classes>
            <class name="SupervielleParallelTestRunner"/>
        </classes>
    </test>
</suite> `

en este caso esta corriendo en paralelo  10 hilos para eso tenemos esta configuracion 
data-provider-thread-count="10" thread-count="10"
--
## Configuracion de cucumber :

 el proyecto cliente debe tener si o si un archivo properties del tipo 
 src/main/resources/cucumber.properties , las variables del tipo ${cucumber.option} es sobre escrita por la propiedad crowdar.cucumber.option en los perfiles 
 maven del proyecto como por ejemplo 
```<crowdar.cucumber.option>src/test/resources/features --glue com/crowdar/core --glue com/crowdar/bdd/cukes --glue ar/com/supervielle --glue ar/supervielle/hook --tags ${crowdar.cucumber.filter} --tags ~@Ignore --plugin com.crowdar.report.CucumberExtentReport  --plugin pretty</crowdar.cucumber.option>```
 
**Ejemplo de cucumber.properties**

https://bitbucket.org/crowdarautomation/cucumberexampleproject/src/master/src/main/resources/cucumber.properties

---

## EJEMPLOS
por favor revisar los siguientes ejemplos de proyectos Incayuyo-cucumber para tener mas información
**WEB**

+ https://bitbucket.org/crowdarautomation/cucumberexampleproject/src/master/
+ https://bitbucket.org/crowdarautomation/supervielleautomation/src/master/

**MOBILE**

+ https://bitbucket.org/crowdarautomation/appiumcucumberexampleproject/src/master/

--- 

# DOCKER
para correr Lippia con Selenium grid hay un archivo docker-compose.yml donde esta la configuracion para contenedores docker necesarios para correr un hub y un nodo 
los pasos serian : 

## Requisitos :
+instalado JDK 8,
+docker 1.7 o superior
+docker-composite compatible
+maven 3.3

## 1) Descargar : https://bitbucket.org/crowdarautomation/cucumberexampleproject/src/master/docker-compose.yml en una carpeta LippiaGridStack por ejemplo
## 2) ir a la carpeta que contiene el docker-compose.yml  y ejecutar 
         ``` sudo docker-compose up --scale chrome=10 -d ```
---

## DRIVER MANAGER : Properties necesarias para consumir el core

crowdar.projectType=WEB_CHROME  
crowdar.setupStrategy=web.LocalWebExecutionStrategy  
crowdar.driverHub=http://localhost:4444/grid/hub  






