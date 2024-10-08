package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class NetworkManager {

    private final String serverAddress;
    private final int serverPort;

    public NetworkManager(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    // 发送请求数据并接收响应
    public byte[] sendRequest(byte[] requestData) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName(serverAddress);

        // 创建并发送请求数据包
        DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, address, serverPort);
        socket.send(requestPacket);
        System.out.println("Request sent to server. Data length: " + requestData.length);

        // 接收响应数据包
        byte[] responseData = new byte[4096]; // 定义缓冲区大小
        DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);
        socket.receive(responsePacket);
        System.out.println("Response received from server. Data length: " + responsePacket.getLength());

        socket.close();

        // 只返回接收到的实际数据长度
        byte[] actualResponseData = new byte[responsePacket.getLength()];
        System.arraycopy(responsePacket.getData(), 0, actualResponseData, 0, responsePacket.getLength());

        return actualResponseData;
    }
}
