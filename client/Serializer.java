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

        Map<String, String> payload = request.getPayload();

        // 序列化 request_id
        serializeString(payload.get("request_id"), buffer);

        // 序列化 action (确保 action 的类型标识符和内容被正确序列化)
        serializeInt32(Integer.parseInt(payload.get("action")), buffer);

        // 序列化其他参数
        for (Map.Entry<String, String> entry : payload.entrySet()) {
            if (!entry.getKey().equals("request_id") && !entry.getKey().equals("action")) {
                // 序列化每个键和值
                serializeString(entry.getKey(), buffer); // 序列化键
                serializeString(entry.getValue(), buffer); // 序列化值
            }
        }

        buffer.flip();
        byte[] serializedData = new byte[buffer.limit()];
        buffer.get(serializedData);

        return serializedData;
    }

    // 序列化 Int32，并添加类型标识符
private void serializeInt32(int value, ByteBuffer buffer) {
    System.out.println("Serializing Int32: " + value);
    buffer.put((byte) 1); // 类型标识：1表示 Int32
    buffer.putInt(value);
}

// 序列化 String，并添加类型标识符
private void serializeString(String value, ByteBuffer buffer) {
    System.out.println("Serializing String: " + value);
    buffer.put((byte) 3); // 类型标识：3表示 String
    byte[] stringBytes = value.getBytes();
    buffer.putInt(stringBytes.length); // 序列化字符串长度
    buffer.put(stringBytes); // 序列化字符串内容
}

}
