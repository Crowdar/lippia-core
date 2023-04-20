package io.lippia.api.lowcode.recognition.parser;

import java.io.File;

public enum Types {

    HEADERS {
        @Override
        public String getSimplifyPath(String defaultPath) {
            return defaultPath.concat("headers").concat(File.separator);
        }
    },

    BODIES {
        @Override
        public String getSimplifyPath(String defaultPath) {
            return defaultPath.concat("bodies").concat(File.separator);
        }
    },

    SCHEMAS {
        @Override
        public String getSimplifyPath(String defaultPath) {
            return defaultPath.concat("schemas").concat(File.separator);
        }
    };

    public abstract String getSimplifyPath(String defaultPath);

}
