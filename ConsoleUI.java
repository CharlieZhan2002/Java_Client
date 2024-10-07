package client;

import models.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUI {

    public static void main(String[] args) {
        // 配置服务器信息
        String serverAddress = "localhost"; // 替换为实际服务器地址
        int serverPort = 12345; // 替换为实际服务器端口

        // 创建 UdpClient 实例
        UdpClient udpClient = new UdpClient(serverAddress, serverPort);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            // 展示菜单
            System.out.println("\nSelect an action:");
            System.out.println("1. Query Flight IDs");
            System.out.println("2. Query Flight Details");
            System.out.println("3. Reserve Seats");
            System.out.println("4. Monitor Flight");
            System.out.println("5. Exit");

            System.out.print("Your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            Map<String, String> requestPayload = new HashMap<>();
            String requestId = "1234"; // 这里可以使用随机生成的 ID

            switch (choice) {
                case 1: // 查询航班 ID
                    System.out.print("Enter source: ");
                    String source = scanner.nextLine();

                    System.out.print("Enter destination: ");
                    String destination = scanner.nextLine();

                    requestPayload.put("action", "1");
                    requestPayload.put("source", source);
                    requestPayload.put("destination", destination);
                    requestPayload.put("request_id", requestId);

                    break;

                case 2: // 查询航班详情
                    System.out.print("Enter flight ID: ");
                    String flightId = scanner.nextLine();

                    requestPayload.put("action", "2");
                    requestPayload.put("flight_id", flightId);
                    requestPayload.put("request_id", requestId);

                    break;

                case 3: // 预订座位
                    System.out.print("Enter flight ID: ");
                    flightId = scanner.nextLine();

                    System.out.print("Enter number of seats to reserve: ");
                    String seats = scanner.nextLine();

                    requestPayload.put("action", "3");
                    requestPayload.put("flight_id", flightId);
                    requestPayload.put("seats", seats);
                    requestPayload.put("request_id", requestId);

                    break;

                case 4: // 监控航班
                    System.out.print("Enter flight ID: ");
                    flightId = scanner.nextLine();

                    System.out.print("Enter monitor interval (seconds): ");
                    String monitorInterval = scanner.nextLine();

                    requestPayload.put("action", "4");
                    requestPayload.put("flight_id", flightId);
                    requestPayload.put("monitor_interval", monitorInterval);
                    requestPayload.put("request_id", requestId);

                    break;

                case 5: // 退出
                    running = false;
                    System.out.println("Exiting...");
                    continue;

                default:
                    System.out.println("Invalid choice, please try again.");
                    continue;
            }

            try {
                // 发送请求并获取响应
                Response response = udpClient.sendRequest(requestPayload);

                // 打印响应
                System.out.println("Response received: " + response.getPayload());

            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }

        scanner.close();
    }
}
