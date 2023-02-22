package io.lippia.api.lowcode.internal;

import java.net.URI;
import java.net.URISyntaxException;

public class URIParser {

    private URIParser() {}

    /** analizar la forma de obtener la property cucumber.features de las options de cucumber
     * se hardcodea el path base, respecto a lo de arriba, tener en cuenta que puede obtenerse
     * mediante los internals de cucumber, o directamente desde el arquetipo
     *
     * (podrïamos construir un fallback y tomar como respaldo los internals de cucumber)
     */
    private static URI getFeaturesRootPath() throws URISyntaxException {
        return new URI("file:src/test/resources/features/");
    }

    public static URI getFeaturePath(String featureName) throws URISyntaxException {
        return new URI(getFeaturesRootPath().toString().concat(featureName));
    }

}