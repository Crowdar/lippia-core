package io.lippia.api.lowcode;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class PrimitiveDefinitionTypeBuilder implements IHierarchicalEventTypeBuilder<PrimitiveDefinitionTypeParser> {

    @Override
    public PrimitiveDefinitionTypeParser build(Object... entries) {
        PrimitiveDefinitionTypeFactory factory = PrimitiveDefinitionTypeFactory.find(entries[0].toString());
        return (factory == null) ? null : factory.getDefinitionType();
    }

    public enum PrimitiveDefinitionTypeFactory {
        BYTE(PrimitiveDefinitionTypeParser.Byte::new,         "^0x\\d+$"),
        INTEGER(PrimitiveDefinitionTypeParser.Integer::new,   "^[+-]?\\d+$"),
        FLOAT(PrimitiveDefinitionTypeParser.Float::new,       "^[+-]?(\\d+(\\.\\d+)?|\\.\\d+)([eE][+-]?\\d+)?[fF]$"),
        DOUBLE(PrimitiveDefinitionTypeParser.Double::new,     "^[+-]?(\\d+(\\.\\d+)?|\\.\\d+)([eE][+-]?\\d+)?[dD]$"),
        LONG(PrimitiveDefinitionTypeParser.Long::new,         "^[+-]?\\d+[lL]$"),
        BOOLEAN(PrimitiveDefinitionTypeParser.Boolean::new,   "^true|false$"),
        ARRAY(PrimitiveDefinitionTypeParser.Array::new,       "^\\[(.|\n)*]$"),
        OBJECT(PrimitiveDefinitionTypeParser.Object::new,     "^\\{[^{].*[^}]}$"),
        STRING(PrimitiveDefinitionTypeParser.String::new,     "^\".*\"$");

        private final Supplier<PrimitiveDefinitionTypeParser> constructor;
        private final Predicate<String> typeMatcher;

        PrimitiveDefinitionTypeFactory(Supplier<PrimitiveDefinitionTypeParser> constructor, String regexp) {
            this(constructor, RegExp.matches(regexp));
        }

        PrimitiveDefinitionTypeFactory(Supplier<PrimitiveDefinitionTypeParser> constructor, Predicate<String> typeMatcher) {
            this.constructor = constructor;
            this.typeMatcher = typeMatcher;
        }

        static PrimitiveDefinitionTypeFactory find(String key) {
            PrimitiveDefinitionTypeFactory _definition = null;
            for (PrimitiveDefinitionTypeFactory definition : PrimitiveDefinitionTypeFactory.values()) {
                if (definition.typeMatcher.test(key)) _definition = definition;
            }

            return _definition;
        }

        PrimitiveDefinitionTypeParser getDefinitionType() {
            return constructor.get();
        }
    }
}
