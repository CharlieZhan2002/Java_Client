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

        // 反序列化 Map
        Map<String, String> map = deserializeMap(buffer);

        return new Response(map);
    }

    // 反序列化 Int32
    private int deserializeInt32(ByteBuffer buffer) {
        return buffer.getInt();
    }

    // 反序列化 Bool
    private boolean deserializeBool(ByteBuffer buffer) {
        return buffer.get() == 1;
    }

    // 反序列化 String
    private String deserializeString(ByteBuffer buffer) {
        int length = buffer.getInt();
        byte[] stringBytes = new byte[length];
        buffer.get(stringBytes);
        return new String(stringBytes);
    }

    // 反序列化 Map
    private Map<String, String> deserializeMap(ByteBuffer buffer) {
        Map<String, String> map = new HashMap<>();
        int mapSize = buffer.getInt();

        for (int i = 0; i < mapSize; i++) {
            String key = deserializeString(buffer);
            String value = deserializeString(buffer); // 假设值类型为 String，可扩展支持其他类型
            map.put(key, value);
        }

        return map;
    }
}
