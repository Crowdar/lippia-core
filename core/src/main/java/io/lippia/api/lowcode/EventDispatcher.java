package io.lippia.api.lowcode;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.lippia.api.lowcode.exception.LippiaException;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public final class EventDispatcher {
    private static final List<IHierarchicalEventTypeBuilder<? extends DefinitionTypeParser>> BUILDERS = new LinkedList<>();

    public static void dispatch(Supplier<IHierarchicalEventTypeBuilder<? extends DefinitionTypeParser>> builder) {
        if (builder == null) return;

        BUILDERS.add(builder.get());
    }

    public static Object trigger(Object... entries) {
        boolean hasCoincidence = false;

        Object entry = entries[0];
        for (IHierarchicalEventTypeBuilder<? extends DefinitionTypeParser> builder: BUILDERS) {
            DefinitionTypeParser parser = builder.build(entry);

            if (parser == null) {
                hasCoincidence = false;
                continue;
            }

            hasCoincidence = true;

            try {
                entry = parser.parse(entry);
            } catch (JsonProcessingException e) {
                throw new LippiaException(e.getMessage()); // improve msg
            }
        }

        if (hasCoincidence) entry = trigger(entry);

        return entry;
    }

}
