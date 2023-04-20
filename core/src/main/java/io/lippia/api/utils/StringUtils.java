package io.lippia.api.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StringUtils {

    public static class Pair {

        public final String left;
        public final String right;

        public Pair(String left, String right) {
            this.left = left;
            this.right = right;
        }

        @Override // only needed for unit tests, so no validation and null checks
        public boolean equals(Object obj) {
            Pair o = (Pair) obj;
            return left.equals(o.left) && right.equals(o.right);
        }

        @Override
        public String toString() {
            return left + ":" + right;
        }

    }

    public static Pair pair(String left, String right) {
        return new Pair(left, right);
    }


}