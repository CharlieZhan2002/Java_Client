package client;

import java.nio.ByteBuffer;
import java.nio.BufferUnderflowException;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

public class Deserializer {

    // 将字节数组反序列化为 Map<String, Object>
    public Map<String, Object> deserialize(byte[] data) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // 使用小端序

        // 读取类型标识符
        byte dataType = buffer.get();

        // 处理不同的数据类型
        if (dataType == 6) { // 类型标识符 6 表示 Map
            return deserializeMap(buffer);
        } else {
            throw new IllegalArgumentException("Unknown data type: " + dataType);
        }
    }

    // 反序列化 Map
    private Map<String, Object> deserializeMap(ByteBuffer buffer) throws Exception {
        Map<String, Object> map = new HashMap<>();

        // 读取 Map 的大小 (int 类型)
        byte mapSizeType = buffer.get();
        if (mapSizeType != 1) {  // 检查类型标识符是否为 Int32 类型
            throw new IllegalArgumentException("Expected int type for map size, but got: " + mapSizeType);
        }

        int mapSize = buffer.getInt(); // 读取 map 大小

        // 避免无效的 Map 大小
        if (mapSize < 0 || mapSize > buffer.remaining()) {
            throw new IllegalArgumentException("Invalid map size: " + mapSize);
        }

        // 依次读取键值对
        for (int i = 0; i < mapSize; i++) {
            String key = deserializeString(buffer); // 反序列化键（字符串）
            String value = deserializeString(buffer); // 反序列化值（字符串）
            map.put(key, value);
        }

        return map;
    }

    // 反序列化字符串
    private String deserializeString(ByteBuffer buffer) throws Exception {
        if (buffer.remaining() < 1) {
            throw new BufferUnderflowException();
        }

        // 检查类型标识符，确保为字符串类型
        byte stringType = buffer.get();
        if (stringType != 3) {
            throw new IllegalArgumentException("Expected string type, but got: " + stringType);
        }

        // 检查长度标识符，确保为 Int32 长度
        byte lengthType = buffer.get();
        if (lengthType != 1) {
            throw new IllegalArgumentException("Expected int type for string length, but got: " + lengthType);
        }

        // 读取字符串长度
        int length = buffer.getInt();

        // 检查长度是否合理
        if (length < 0 || length > buffer.remaining()) {
            throw new IllegalArgumentException("Invalid string length: " + length);
        }

        // 读取字符串内容
        byte[] stringBytes = new byte[length];
        buffer.get(stringBytes); // 读取字符串内容
        return new String(stringBytes, "UTF-8"); // 转换为 UTF-8 字符串
    }

    // 将字节数组转换为十六进制字符串（用于调试输出）
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x ", b));
        }
        return sb.toString().trim();
    }
}
