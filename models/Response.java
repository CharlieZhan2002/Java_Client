package models;

import java.util.Map;

public class Response {

    private String requestId;
    private String status;
    private String message;
    private Map<String, Object> additionalData;

    // 从 Map<String, Object> 构造 Response 对象
    public Response(Map<String, Object> responseMap) {
        this.requestId = (String) responseMap.get("request_id");
        this.status = (String) responseMap.get("status");
        this.message = (String) responseMap.get("message");
        this.additionalData = responseMap;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    @Override
    public String toString() {
        return "Response{" +
                "requestId='" + requestId + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", additionalData=" + additionalData +
                '}';
    }
}
