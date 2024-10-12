package models;

import java.util.Map;

public class Response {

    private String requestId;
    private String status;
    private String message;
    private String action;  // 新增action字段，用于客户端操作类型
    private Map<String, Object> additionalData;

    // 构造函数添加 action 参数，并打印服务器返回的数据
    public Response(Map<String, Object> responseMap, String action) {
        // 打印服务器返回的所有数据
        System.out.println("Debug: Full Response Map from Server: " + responseMap);

        // 从 responseMap 中提取值
        this.requestId = (String) responseMap.get("request_id");
        this.status = (String) responseMap.get("status");
        this.message = (String) responseMap.get("message");
        this.action = action;  // action 从客户端传入
        this.additionalData = responseMap;
       // 打印 responseMap 的类型和详细信息
       System.out.println("Map Class: " + responseMap.getClass().getName());

       // 遍历 responseMap 并打印每个键及其对应的值类型
       for (Map.Entry<String, Object> entry : responseMap.entrySet()) {
           System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue() + ", Type: " + entry.getValue().getClass().getName());
       }
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
        StringBuilder responseString = new StringBuilder();
        responseString.append("Request ID: ").append(requestId).append("\n");
        responseString.append("Status: ").append(status).append("\n");

        // 当状态为200时，显示所有的键和值
        if ("200".equals(status)) {
            for (Map.Entry<String, Object> entry : additionalData.entrySet()) {
                responseString.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        } else {
            // 如果不是200状态，显示message信息
            responseString.append("Message: ").append(message != null ? message : "No message provided.");
        }

        return responseString.toString();
    }
}
