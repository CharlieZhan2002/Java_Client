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

        // 发送请求并接收响应
        byte[] responseData = networkManager.sendRequest(requestData);

        // 反序列化响应
        return deserializer.deserialize(responseData);
    }
}
