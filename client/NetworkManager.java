package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class NetworkManager {
    // ANSI 颜色代码
    public static final String RESET = "\033[0m"; // 重置颜色
    public static final String RED = "\033[0;31m"; // 红色文本
    public static final String GREEN = "\033[0;32m"; // 绿色文本
    public static final String YELLOW = "\033[0;33m"; // 黄色文本
    public static final String BLUE = "\033[0;34m"; // 蓝色文本
    public static final String PURPLE = "\033[0;35m"; // 紫色文本
    public static final String CYAN = "\033[0;36m"; // 青色文本
    public static final String WHITE = "\033[0;37m"; // 白色文本

    private final String serverAddress;
    private final int serverPort;
    private static final int TIMEOUT = 10000; // 超时时间10秒

    public NetworkManager(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    // 发送请求数据并接收响应
    public byte[] sendRequest(byte[] requestData) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName(serverAddress);

        // 设置超时时间
        socket.setSoTimeout(TIMEOUT);

        // 创建并发送请求数据包
        DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, address, serverPort);
        socket.send(requestPacket);
        // System.out.println("Request sent to server. Data length: " +
        // requestData.length);

        // 接收响应数据包
        byte[] responseData = new byte[4096]; // 定义缓冲区大小
        DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);

        try {
            socket.receive(responsePacket); // 等待响应
            // System.out.println("Response received from server. Data length: " +
            // responsePacket.getLength());

            // 只返回接收到的实际数据长度
            byte[] actualResponseData = new byte[responsePacket.getLength()];
            System.arraycopy(responsePacket.getData(), 0, actualResponseData, 0, responsePacket.getLength());
            return actualResponseData;

        } catch (SocketTimeoutException e) {
            // 超时未接收到响应，抛出异常
            System.err.println(YELLOW + "Timeout waiting for response from server." + RESET);
            throw e;
        } finally {
            socket.close(); // 关闭套接字
        }
    }
}
