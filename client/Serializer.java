package client;

import models.Request;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

public class Serializer {

    // 将请求序列化为字节数组
    public byte[] serialize(Request request) {
        ByteBuffer buffer = ByteBuffer.allocate(4096); // 使用大缓冲区
        buffer.order(ByteOrder.LITTLE_ENDIAN); // 使用小端序

        Map<String, String> payload = request.getPayload();

        // 序列化 Map 类型标识符和长度
        buffer.put((byte) 6); // 类型标识符：6表示 Map
        serializeInt32(payload.size(), buffer); // 序列化 Map 的长度

        // 序列化每个键值对
        for (Map.Entry<String, String> entry : payload.entrySet()) {
            serializeString(entry.getKey(), buffer); // 序列化键
            serializeString(entry.getValue(), buffer); // 序列化值
        }

        buffer.flip();
        byte[] serializedData = new byte[buffer.limit()];
        buffer.get(serializedData);

        return serializedData;
    }

    // 序列化 Int32，并添加类型标识符
    private void serializeInt32(int value, ByteBuffer buffer) {
        buffer.put((byte) 1); // 类型标识符：1表示 Int32
        buffer.putInt(value); // 小端序
    }

    // 序列化布尔值，并添加类型标识符
    private void serializeBool(boolean value, ByteBuffer buffer) {
        buffer.put((byte) 2); // 类型标识符：2表示 Bool
        buffer.put((byte) (value ? 1 : 0)); // 1 表示 true, 0 表示 false
    }

    // 序列化字符串，并添加类型标识符
    private void serializeString(String value, ByteBuffer buffer) {
        buffer.put((byte) 3); // 类型标识符：3表示 String
        byte[] stringBytes = value.getBytes();
        serializeInt32(stringBytes.length, buffer); // 序列化字符串长度（小端序）
        buffer.put(stringBytes); // 序列化字符串内容
    }

    // 序列化 Float，并添加类型标识符
    private void serializeFloat(float value, ByteBuffer buffer) {
        buffer.put((byte) 4); // 类型标识符：4表示 Float
        buffer.putFloat(value); // 使用小端序存储浮点数
    }

    // 序列化数组，并添加类型标识符
    private <T> void serializeArray(T[] array, ByteBuffer buffer) throws Exception {
        buffer.put((byte) 5); // 类型标识符：5表示 Array
        serializeInt32(array.length, buffer); // 数组长度
        for (T item : array) {
            if (item instanceof String) {
                serializeString((String) item, buffer);
            } else if (item instanceof Integer) {
                serializeInt32((Integer) item, buffer);
            } else if (item instanceof Boolean) {
                serializeBool((Boolean) item, buffer);
            } else if (item instanceof Float) {
                serializeFloat((Float) item, buffer);
            } else {
                throw new Exception("Unsupported array item type");
            }
        }
    }

    // 序列化 Map 并添加类型标识符，Map 键为字符串
    public void serializeMap(Map<String, Object> map, ByteBuffer buffer) throws Exception {
        buffer.put((byte) 6); // 类型标识符：6表示 Map
        serializeInt32(map.size(), buffer); // 序列化 Map 的长度
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            serializeString(entry.getKey(), buffer); // 序列化键
            Object value = entry.getValue();
            if (value instanceof String) {
                serializeString((String) value, buffer);
            } else if (value instanceof Integer) {
                serializeInt32((Integer) value, buffer);
            } else if (value instanceof Boolean) {
                serializeBool((Boolean) value, buffer);
            } else if (value instanceof Float) {
                serializeFloat((Float) value, buffer);
            } else if (value instanceof Object[]) {
                serializeArray((Object[]) value, buffer);
            } else {
                throw new Exception("Unsupported Map value type");
            }
        }
    }
}
