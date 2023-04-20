package io.lippia.api.lowcode.database;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DbUtils {

    private final JdbcTemplate jdbc;

    public DbUtils(String username, String password, String uri, String driver) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(uri);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        jdbc = new JdbcTemplate(dataSource);
    }

    public Object readValue(String query) {
        return jdbc.queryForObject(query, Object.class);
    }

    public Map<String, Object> readRow(String query) {
        return jdbc.queryForMap(query);
    }

    public List<Map<String, Object>> readRows(String query) {
        return jdbc.queryForList(query);
    }

    public void insertRow(final String sql){
        System.out.println("cld");
        jdbc.batchUpdate(new String[]{sql});
    }

    public void deleteRow(String sql) {
        int resultado = jdbc.update(sql);
    }



    public Map<String, Object> executeQuery(String queryFile) throws IOException {
        String query = SqlFileReader.getQueryString(queryFile);
        return executeQuery(jdbc,query);
    }

    public Map<String, Object> executeQueryWithParameters(String queryFile, Map<String,String> parameters) throws IOException {
        String query = SqlFileReader.getQueryString(queryFile);
        String queryWithParameters = null;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String campo = String.format("{{%s}}", entry.getKey());
            String valor = entry.getValue();
            System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());

            if(queryWithParameters != null) {
                query = queryWithParameters;
            }
            String temp = query.replace(campo, valor);
            queryWithParameters = query.replace(query, temp);
        }
        return executeQuery(jdbc,queryWithParameters);
    }

    private Map<String,Object> executeQuery(JdbcTemplate jdbc,String query){
        Map<String,Object> result = new HashMap<>();
        if(query.startsWith("UPDATE") || query.startsWith("DELETE") || query.startsWith("INSERT")|| query.startsWith("SET")){
            int status = jdbc.update(query);
            result.put("status", status);
            return result;
        }else {
            return jdbc.queryForMap(query);
        }
    }
}