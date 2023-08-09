package io.lippia.api.lowcode;

import static io.lippia.api.lowcode.Path.getDefaultPathTo;
import static io.lippia.api.lowcode.Path.read;

public abstract class FileEventDefinitionTypeParser extends DefinitionTypeParser {

    public static class Read extends FileEventDefinitionTypeParser {
        @Override
        public String parse(Object... entries) {
            String currentFile = (String) entries[0];
            currentFile = currentFile.substring(5, currentFile.length() - 1);
            return read(getDefaultPathTo(currentFile));
        }
    }

}
