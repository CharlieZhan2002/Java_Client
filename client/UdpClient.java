package client;

import models.Request;
import models.Response;
import java.util.Map;

public class UdpClient {

    private final NetworkManager networkManager;
    private final Serializer serializer;
    private final Deserializer deserializer;

    public UdpClient(String serverAddress, int serverPort) {
        this.networkManager = new NetworkManager(serverAddress, serverPort);
        this.serializer = new Serializer();
        this.deserializer = new Deserializer();
    }

    // 发送请求并返回响应
    public Response sendRequest(Map<String, String> requestPayload) throws Exception {

        // 创建请求对象
        Request request = new Request(requestPayload);

        // 序列化请求
        byte[] requestData = serializer.serialize(request);

        // 添加调试日志，打印序列化后的请求数据
        System.out.println("Serialized Request Data: " + java.util.Arrays.toString(requestData));

        // 发送请求并接收响应
        byte[] responseData = networkManager.sendRequest(requestData);

        // 反序列化响应为 Map<String, Object>
        Map<String, Object> responseMap = deserializer.deserialize(responseData);

        // 将反序列化的 Map 转换为 Response 对象
        Response response = new Response(responseMap);

        return response;
    }
}
