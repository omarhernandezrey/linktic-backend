package com.linktic.inventory.web;

import java.util.*;

public final class JsonApi {
    private JsonApi() {}

    public static Map<String,Object> resource(String type, String id, Map<String,Object> attributes){
        Map<String,Object> data = new LinkedHashMap<>();
        data.put("type", type);
        data.put("id", id);
        data.put("attributes", attributes);
        return Map.of("data", data);
    }

    public static Map<String,Object> error(int status, String title, String detail){
        return Map.of("errors", List.of(Map.of("status", String.valueOf(status), "title", title, "detail", detail)));
    }
}
