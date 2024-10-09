package client;

import models.Response;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUI {

    public static void main(String[] args) {
        // 配置服务器信息
        String serverAddress = "18.141.209.212"; // 替换为实际服务器地址
        int serverPort = 8888; // 替换为实际服务器端口

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

            int choice = -1; // 初始化choice为无效值

            // 捕获用户输入的异常
            while (choice == -1) {
                System.out.print("Your choice: ");
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input, please enter a valid number.");
                    scanner.next(); // 消耗错误的输入，继续循环等待用户输入
                }
            }

            Map<String, String> requestPayload = new HashMap<>();
            String requestId = String.valueOf(System.currentTimeMillis()); // 使用当前时间戳生成 request_id

            // 构造请求
            requestPayload.put("request_id", requestId);
            requestPayload.put("action", String.valueOf(choice));

            switch (choice) {
                case 1: // 查询航班 ID
                    System.out.print("Enter source: ");
                    String source = scanner.nextLine();
                    System.out.print("Enter destination: ");
                    String destination = scanner.nextLine();

                    requestPayload.put("source", source);
                    requestPayload.put("destination", destination);
                    break;

                case 2: // 查询航班详情
                    System.out.print("Enter flight ID: ");
                    String flightId = scanner.nextLine();
                    requestPayload.put("flight_id", flightId);
                    break;

                case 3: // 预订座位
                    System.out.print("Enter flight ID: ");
                    flightId = scanner.nextLine();
                    System.out.print("Enter number of seats to reserve: ");
                    String seats = scanner.nextLine();

                    requestPayload.put("flight_id", flightId);
                    requestPayload.put("seats", seats);
                    break;

                case 4: // 监控航班
                    System.out.print("Enter flight ID: ");
                    flightId = scanner.nextLine();
                    System.out.print("Enter monitor interval (seconds): ");
                    String monitorInterval = scanner.nextLine();

                    requestPayload.put("flight_id", flightId);
                    requestPayload.put("monitor_interval", monitorInterval);
                    break;

                case 5: // 退出
                    running = false;
                    System.out.println("Exiting...");
                    continue;

                default:
                    System.out.println("Invalid choice, please try again.");
                    choice = -1; // 重置choice，使用户重新选择
                    continue;
            }

            // 发送请求并获取响应
            try {
                Response response = udpClient.sendRequest(requestPayload);
                // 打印 Response 对象
                System.out.println("Response received: " + response.toString());

            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }

        scanner.close();
    }
}
