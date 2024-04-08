### Listeners
_Please be aware that the use of listeners can be challenging for users who are just starting out with Lippia Low Code, so we do not recommend it until you have mastered most of the existing steps._

Most of the events that trigger the core-level steps come from a pillar called the **EventDispatcher**, which is responsible for recognizing expressions and executing logic based on matches.

One of the most significant advantages of using this powerful feature is that you can create an expression to trigger an event, and this will be automatically applied to everything that happens in the core.  
It applies to saving the result in: a Gherkin-level variable, setting values in the request body, headers, schemas, and can even be combined with other values.

#### Guide
Often, we will need to create events such as a UUID generator, Strings, Integers, etc.  
It is not necessary to create new steps for this purpose (although it could be done); Lippia Low Code has an architecture that allows you to create listeners to recognize and trigger this event, which can then be set in a Gherkin-level variable (if that is your current need).

Let's imagine for a moment that you need a step like:

```gherkin
* define uuid = $(random.uuid)
```

Well, it's not necessary to create a step to do that; we could simply create an event that triggers when the expression **$(random.uuid)** is recognized.

#### Building our first event

*RandomValuesDefinitionTypeBuilder.class*
```java
import io.lippia.api.lowcode.IHierarchicalEventTypeBuilder;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class RandomValuesDefinitionTypeBuilder implements IHierarchicalEventTypeBuilder<RandomValuesDefinitionTypeParser> {

    @Override
    public RandomValuesDefinitionTypeParser build(Object... objects) {
        RandomValuesDefinitionTypeFactory factory = RandomValuesDefinitionTypeFactory.find(objects[0].toString());
        return (factory == null) ? null : factory.getDefinitionType();
    }

    public enum RandomValuesDefinitionTypeFactory {
        UUID(RandomValuesDefinitionTypeParser.Uuid::new,      ".*?\\$\\(random.uuid\\).*?"),
        STRING(RandomValuesDefinitionTypeParser.String::new,  ".*?\\$\\(random.string\\).*?");
        
        private final Supplier<RandomValuesDefinitionTypeParser> constructor;
        private final Predicate<String> typeMatcher;

        RandomValuesDefinitionTypeFactory(Supplier<RandomValuesDefinitionTypeParser> constructor, String regexp) {
            this(constructor, RegExp.matches(regexp));
        }

        RandomValuesDefinitionTypeFactory(Supplier<RandomValuesDefinitionTypeParser> constructor, Predicate<String> typeMatcher) {
            this.constructor = constructor;
            this.typeMatcher = typeMatcher;
        }

        static RandomValuesDefinitionTypeFactory find(String key) {
            RandomValuesDefinitionTypeFactory _definition = null;
            for (RandomValuesDefinitionTypeFactory definition : RandomValuesDefinitionTypeFactory.values()) {
                if (definition.typeMatcher.test(key)) _definition = definition;
            }

            return _definition;
        }

        RandomValuesDefinitionTypeParser getDefinitionType() {
            return constructor.get();
        }
    }
}
```

*RandomValuesDefinitionTypeParser.class*
```java
import io.lippia.api.lowcode.DefinitionTypeParser;

public abstract class RandomValuesDefinitionTypeParser extends DefinitionTypeParser {
    public static class Uuid extends RandomValuesDefinitionTypeParser {

        @Override
        public Object parse(Object... objects) {
            return null;
        }
    }

    public static class String extends RandomValuesDefinitionTypeParser {

        @Override
        public Object parse(Object... objects) {
            return null;
        }
    }
}
```

#### Implementation

```java
EventDispatcher.dispatch(RandomValuesDefinitionTypeBuilder::new);
```

Please note that using this dispatch will save your custom event to the queue of pre-developed listeners in the low code core.