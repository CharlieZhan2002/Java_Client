package client;

import models.Response;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

public class Deserializer {

    // 反序列化字节数组为 Response 对象
    public Response deserialize(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        Map<String, String> map = new HashMap<>();

        // 反序列化 request_id
        String requestId = deserializeString(buffer);
        map.put("request_id", requestId);

        // 反序列化 status
        int statusCode = deserializeInt32(buffer);
        map.put("status", String.valueOf(statusCode));

        // 反序列化其他参数
        while (buffer.remaining() > 0) {
            String key = deserializeString(buffer);
            String value = deserializeString(buffer);
            map.put(key, value);
        }

        return new Response(map);
    }

    // 反序列化 Int32
    private int deserializeInt32(ByteBuffer buffer) {
        return buffer.getInt();
    }

    // 反序列化 String
    private String deserializeString(ByteBuffer buffer) {
        int length = buffer.getInt();
        byte[] stringBytes = new byte[length];
        buffer.get(stringBytes);
        return new String(stringBytes);
    }
}
