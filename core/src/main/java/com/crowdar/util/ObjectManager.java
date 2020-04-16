package api.DataAccess;

import com.crowdar.core.MyThreadLocal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseManager {

    private static final String QUERYS_RESULT = "DatabseManager.Querys";

    private static <T> List<T> getQuerysResult() {
        List<T> querysResult = (List<T>) MyThreadLocal.getData(QUERYS_RESULT);
        if (querysResult == null) {
            querysResult = new ArrayList();
            MyThreadLocal.setData(QUERYS_RESULT, querysResult);
        }
        return querysResult;
    }

    public static Object getQueryResult(int position) {
        return getQuerysResult().get(position);
    }

    public static Object getQueryResult(Class queryResult) {
        return getQuerysResult().stream()
                .filter(query -> query.getClass().equals(queryResult))
                .findFirst().orElse(null);
    }

    public static List<Object> getQueryResults(Class queryResult) {
        return getQuerysResult().stream()
                .filter(query -> query.getClass().equals(queryResult))
                .collect(Collectors.toList());
    }

    public static void addQueryResult(Object query) {
        getQuerysResult().add(query);
    }

    public static void cleanQueryResults() {
        MyThreadLocal.setData(QUERYS_RESULT, null);
    }
}
