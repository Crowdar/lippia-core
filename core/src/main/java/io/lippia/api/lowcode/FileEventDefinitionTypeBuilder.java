package io.lippia.api.lowcode;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class FileEventDefinitionTypeBuilder implements IHierarchicalEventTypeBuilder<FileEventDefinitionTypeParser> {
    @Override
    public FileEventDefinitionTypeParser build(Object... entries) {
        FileEventDefinitionTypeFactory factory = FileEventDefinitionTypeFactory.find(entries[0].toString());
        return (factory == null) ? null : factory.getDefinitionType();
    }

    enum FileEventDefinitionTypeFactory {

        READ(FileEventDefinitionTypeParser.Read::new,   "^read\\(\\S+\\)$");

        private final Supplier<FileEventDefinitionTypeParser> constructor;
        private final Predicate<String> typeMatcher;

        FileEventDefinitionTypeFactory(Supplier<FileEventDefinitionTypeParser> constructor, String regexp) {
            this(constructor, RegExp.matches(regexp));
        }

        FileEventDefinitionTypeFactory(Supplier<FileEventDefinitionTypeParser> constructor, Predicate<String> typeMatcher) {
            this.constructor = constructor;
            this.typeMatcher = typeMatcher;
        }

        static FileEventDefinitionTypeFactory find(String key) {
            FileEventDefinitionTypeFactory _definition = null;
            for (FileEventDefinitionTypeFactory definition : FileEventDefinitionTypeFactory.values()) {
                if (definition.typeMatcher.test(key)) _definition = definition;
            }

            return _definition;
        }

        FileEventDefinitionTypeParser getDefinitionType() {
            return constructor.get();
        }

    }
}
