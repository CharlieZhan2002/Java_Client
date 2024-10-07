package client;

import models.Request;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

public class Serializer {

    // 将请求序列化为字节数组
    public byte[] serialize(Request request) {
        ByteBuffer buffer = ByteBuffer.allocate(4096); // 使用大缓冲区
        buffer.order(ByteOrder.LITTLE_ENDIAN); // 小端序
        
        // 序列化 Map 类型
        serializeMap(request.getPayload(), buffer);

        buffer.flip();
        byte[] serializedData = new byte[buffer.limit()];
        buffer.get(serializedData);

        return serializedData;
    }

    // 序列化 Int32
    private void serializeInt32(int value, ByteBuffer buffer) {
        buffer.put((byte) 1); // 类型标识
        buffer.putInt(value);
    }

    // 序列化 Bool
    private void serializeBool(boolean value, ByteBuffer buffer) {
        buffer.put((byte) 2); // 类型标识
        buffer.put((byte) (value ? 1 : 0));
    }

    // 序列化 String
    private void serializeString(String value, ByteBuffer buffer) {
        buffer.put((byte) 3); // 类型标识
        byte[] stringBytes = value.getBytes();
        buffer.putInt(stringBytes.length); // 字符串长度
        buffer.put(stringBytes); // 字符串内容
    }

    // 序列化 Float
    private void serializeFloat(float value, ByteBuffer buffer) {
        buffer.put((byte) 4); // 类型标识
        buffer.putFloat(value);
    }

    // 序列化 Array（假设数组类型统一）
    private void serializeArray(Object[] array, ByteBuffer buffer) {
        buffer.put((byte) 5); // 类型标识
        buffer.putInt(array.length); // 数组长度

        for (Object element : array) {
            if (element instanceof Integer) {
                serializeInt32((int) element, buffer);
            } else if (element instanceof String) {
                serializeString((String) element, buffer);
            } // 添加更多类型支持
        }
    }

    // 序列化 Map
    private void serializeMap(Map<String, String> map, ByteBuffer buffer) {
        buffer.put((byte) 6); // 类型标识
        buffer.putInt(map.size()); // Map 长度

        for (Map.Entry<String, String> entry : map.entrySet()) {
            serializeString(entry.getKey(), buffer); // 序列化键
            serializeString(entry.getValue(), buffer); // 序列化值（这里假设值为 String，可以扩展其他类型）
        }
    }
}
