package io.lippia.api.lowcode;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class ParametersDefinitionTypeBuilder implements IHierarchicalEventTypeBuilder<ParametersDefinitionTypeParser> {
    @Override
    public ParametersDefinitionTypeParser build(Object... entries) {
        ParametersDefinitionTypeFactory factory = ParametersDefinitionTypeFactory.find(entries[0].toString());
        return (factory == null) ? null : factory.getDefinitionType();
    }

    public enum ParametersDefinitionTypeFactory {
        VARIABLE(ParametersDefinitionTypeParser.Variable::new,   ".*?\\$\\(var.\\w+\\).*?"),
        OLD_VARIABLE(ParametersDefinitionTypeParser.OldVariable::new, "\\{{2}\\w+}{2}"),
        ENV_PROPERTY(ParametersDefinitionTypeParser.EnvironmentProperty::new, ".*?\\$\\(env.\\w+\\).*?"),
        FILE_PROPERTY(ParametersDefinitionTypeParser.FileProperty::new, ".*?\\$\\(pro.\\w+\\).*?"),
        XLSX_PROPERTY(ParametersDefinitionTypeParser.XLSXProperty::new, ".*?\\$\\(\\w+\\.xlsx@\\w+\\).*?"),
        CSV_PROPERTY(ParametersDefinitionTypeParser.CSVProperty::new, ".*?\\$\\(csv.\\w+\\).*?"),
        RDB_PROPERTY(ParametersDefinitionTypeParser.RDBProperty::new, ".*?\\$\\(rdb.\\w+\\).*?"),
        NRDB_PROPERTY(ParametersDefinitionTypeParser.NRDBProperty::new, ".*?\\$\\(nrdb.\\w+\\).*?");

        private final Supplier<ParametersDefinitionTypeParser> constructor;
        private final Predicate<String> typeMatcher;

        ParametersDefinitionTypeFactory(Supplier<ParametersDefinitionTypeParser> constructor, String regexp) {
            this(constructor, RegExp.matches(regexp));
        }

        ParametersDefinitionTypeFactory(Supplier<ParametersDefinitionTypeParser> constructor, Predicate<String> typeMatcher) {
            this.constructor = constructor;
            this.typeMatcher = typeMatcher;
        }

        static ParametersDefinitionTypeFactory find(String key) {
            ParametersDefinitionTypeFactory _definition = null;
            for (ParametersDefinitionTypeFactory definition : ParametersDefinitionTypeFactory.values()) {
                if (definition.typeMatcher.test(key)) _definition = definition;
            }

            return _definition;
        }

        ParametersDefinitionTypeParser getDefinitionType() {
            return constructor.get();
        }
    }
}
