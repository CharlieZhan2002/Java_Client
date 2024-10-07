package models;

import java.util.Map;

public class Request {

    private final Map<String, String> payload;

    public Request(Map<String, String> payload) {
        this.payload = payload;
    }

    public Map<String, String> getPayload() {
        return payload;
    }
}
