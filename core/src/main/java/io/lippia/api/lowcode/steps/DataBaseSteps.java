package io.lippia.api.lowcode.steps;

import com.crowdar.core.MyThreadLocal;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import com.crowdar.core.EnvironmentManager;
import io.lippia.api.lowcode.Engine;
import io.lippia.api.lowcode.database.DbUtils;
import io.lippia.api.service.CommonService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataBaseSteps {
    private JdbcTemplate jdbc;
    Map<String, Object> rows;
    DbUtils db;
    Map<String, String> parameters = new HashMap<>();

    @Given("^create connection database '(.*)'$")
    @And("^crear conexion a la base de datos '(.*)'$")
    public void setDatabaseConfig(String base) {
        String uri = EnvironmentManager.getProperty("uri.".concat(base));
        String username = EnvironmentManager.getProperty("username.".concat(base));
        String password = EnvironmentManager.getProperty("password.".concat(base));
        String driver = EnvironmentManager.getProperty("driver.".concat(base));
        db = new DbUtils(username, password, uri, driver);
    }


    @When("^execute query '(.*)'$")
    @And("^ejecutar query '(.*)'$")
    public void execute(String queryFile) throws IOException {
        if (parameters.isEmpty()) {
            rows = db.executeQuery(queryFile);
        } else {
            rows = db.executeQueryWithParameters(queryFile, parameters);
            parameters.clear();
        }
        MyThreadLocal.setData("resultsDB", rows);
    }

    @Then("^validate field '(.*)' = (.*)$")
    @And("^validar campo '(.*)' = (.*)$")
    public void validateField(String campo, Object valor) throws Exception {
        Object result = null;
        try {
            if (rows.get(campo) != null) {
                result = rows.get(campo);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage() + " - Valide que el campo " + campo + " sea el correcto");
        }
        if(result.getClass().equals(String.class)){
          result = "\"".concat((String) result).concat("\"");
        }
        Object obj = Engine.evaluateExpression(result.toString());
        Assert.assertEquals(obj, CommonService.getValueOf(valor));
    }

    @When("^add query parameter '(.*)' = (.*)$")
    @And("^agregar parametro de query '(.*)' = (.*)$")
    public void addParameter(String campo, String parametro) throws Exception {
        parameters.put(campo, CommonService.getValueOf(parametro).toString());
    }


}
