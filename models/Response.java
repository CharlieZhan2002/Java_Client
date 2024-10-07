package models;

import java.util.Map;

public class Response {

    private final Map<String, String> payload;

    public Response(Map<String, String> payload) {
        this.payload = payload;
    }

    public Map<String, String> getPayload() {
        return payload;
    }
}
