package com.data.datasource.core;

import java.util.HashMap;
import java.util.Map;

public class RoutingContext {
    private static final ThreadLocal<Map<String,RoutingContext>>routingContextHolder=new ThreadLocal<Map<String, RoutingContext>>(){
        @Override
        protected Map<String, RoutingContext> initialValue() {
            return new HashMap<String, RoutingContext>();
        }
    };
}
