package io.lippia.api.lowcode;

@FunctionalInterface
public interface IHierarchicalEventTypeBuilder<T extends DefinitionTypeParser> {
    T build(Object... entries);
}
