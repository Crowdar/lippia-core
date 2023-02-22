package io.lippia.api.lowcode.steps;

import com.crowdar.core.PropertyManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.lippia.api.lowcode.EnvironmentManager;
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
    public void setDatabaseConfig(String base) {
        String uri = EnvironmentManager.getProperty("uri.".concat(base));
        String username = PropertyManager.getProperty("username");
        String password = EnvironmentManager.getProperty("password");
        String driver = "oracle.jdbc.driver.OracleDriver";
        db = new DbUtils(username, password, uri, driver);
    }

    @When("^execute query '(.*)'$")
    public void execute(String queryFile) throws IOException {
        if (parameters.isEmpty()) {
            rows = db.executeQuery(queryFile);
        } else {
            rows = db.executeQueryWithParameters(queryFile, parameters);
            parameters.clear();
        }
    }

    @Then("^validate field '(.*)' = (.*)$")
    public void validateField(String campo, String valor) throws Exception {
        Object result = null;
        try {
            if (rows.get(campo) != null) {
                result = rows.get(campo);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage() + " - Valide que el campo " + campo + " sea el correcto");
        }
        Assert.assertEquals(result.toString(), CommonService.getValueOf(valor));
    }

    @When("^add query parameter '(.*)' = (.*)$")
    public void addParameter(String campo, String parametro) throws Exception {
        parameters.put(campo,CommonService.getValueOf(parametro).toString());
    }



}
