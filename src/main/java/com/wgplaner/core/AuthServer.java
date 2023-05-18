package com.wgplaner.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum AuthServer {
    HOME_BREW("https://wg-planer-auth-server.onrender.com");
    private final String url;
    static final Map<String, AuthServer> ENUM_MAP;

    AuthServer(String url) {
        this.url = url;
    }

    static {
        Map<String, AuthServer> map = new ConcurrentHashMap<>();
        Arrays.stream(values()).forEach(instance -> map.put(instance.url.toLowerCase(), instance));
        ENUM_MAP = Collections.unmodifiableMap(map);
    }
    public static AuthServer get(String url) {
        return ENUM_MAP.get(url.toLowerCase());
    }
}
