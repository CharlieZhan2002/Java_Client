package client;

import models.Request;
import models.Response;
import java.util.Map;
import java.net.SocketTimeoutException;

public class UdpClient {
    // ANSI 颜色代码
    public static final String RESET = "\033[0m"; // 重置颜色
    public static final String RED = "\033[0;31m"; // 红色文本
    public static final String GREEN = "\033[0;32m"; // 绿色文本
    public static final String YELLOW = "\033[0;33m"; // 黄色文本
    public static final String BLUE = "\033[0;34m"; // 蓝色文本
    public static final String PURPLE = "\033[0;35m"; // 紫色文本
    public static final String CYAN = "\033[0;36m"; // 青色文本
    public static final String WHITE = "\033[0;37m"; // 白色文本

    private final NetworkManager networkManager;
    private final Serializer serializer;
    private final Deserializer deserializer;
    private static final int MAX_RETRIES = 3; // 最大重试次数

    public UdpClient(String serverAddress, int serverPort) {
        this.networkManager = new NetworkManager(serverAddress, serverPort);
        this.serializer = new Serializer();
        this.deserializer = new Deserializer();
    }

    // 发送请求并返回响应，带有重试机制
    public Response sendRequest(Map<String, String> requestPayload, String actionType) throws Exception {

        // 动态设置 invocation_semantic 值
        String invocationSemantic = determineInvocationSemantic(actionType);
        requestPayload.put("invocation_semantic", invocationSemantic);

        // 创建请求对象
        Request request = new Request(requestPayload);

        // 序列化请求
        byte[] requestData = serializer.serialize(request);

        // 添加调试日志，打印序列化后的请求数据
        // System.out.println("Serialized Request Data: " +
        // java.util.Arrays.toString(requestData));

        int attempts = 0;
        boolean receivedResponse = false;
        byte[] responseData = null;

        // 重试逻辑，最多重试 MAX_RETRIES 次
        while (attempts < MAX_RETRIES && !receivedResponse) {
            try {
                if (attempts == 0) {
                    System.out.println("\n" + YELLOW + "Attempt " + (attempts + 1) + " to send request..." + RESET);
                } else {
                    System.out.println(YELLOW + "Attempt " + (attempts + 1) + " to send request..." + RESET);
                }
                // 发送请求并接收响应
                responseData = networkManager.sendRequest(requestData);
                receivedResponse = true; // 如果成功收到响应，退出循环

            } catch (SocketTimeoutException e) {
                // 超时未接收到响应，尝试重发请求
                attempts++;
                if (attempts >= MAX_RETRIES) {
                    throw new Exception("Failed to receive response after " + MAX_RETRIES + " attempts.");
                }
            }
        }

        // 反序列化响应为 Map<String, Object>
        Map<String, Object> responseMap = deserializer.deserialize(responseData);

        // 将反序列化的 Map 转换为 Response 对象
        Response response = new Response(responseMap, actionType);

        return response;
    }

    // 根据 actionType 动态设置 invocation_semantic
    private String determineInvocationSemantic(String actionType) {
        switch (actionType) {
            case "QueryFlightIds":
            case "QueryFlightDetails":
            case "MonitorFlight":
            case "ReserveSeatsCheapestPrice":
            case "ReserveSeatsBelowPrice":
            case "ResetSeatsData":
                return "at-most-once";

            case "ReserveSeats":
                return "at-least-once";

            default:
                throw new IllegalArgumentException("Unknown action type: " + actionType);
        }
    }
}
