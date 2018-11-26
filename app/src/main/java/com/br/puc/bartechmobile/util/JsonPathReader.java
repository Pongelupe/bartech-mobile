package com.br.puc.bartechmobile.util;

import com.jayway.jsonpath.JsonPath;

public class JsonPathReader {

    public static <T> T readFromJson(String json, String identifier, Class<T> clazz) {
        if (!clazz.equals(String.class))
            return JsonPath.parse(json).read(identifier, clazz);
        else
            return JsonPath.parse(json).read(identifier);
    }

}
