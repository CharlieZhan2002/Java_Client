package client;

import models.Response;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUI {
    // ANSI 颜色代码
    public static final String CLEAR_SCREEN = "\033[H\033[2J";
    public static final String RESET = "\033[0m"; // 重置颜色
    public static final String RED = "\033[0;31m"; // 红色文本
    public static final String GREEN = "\033[0;32m"; // 绿色文本
    public static final String YELLOW = "\033[0;33m"; // 黄色文本
    public static final String BLUE = "\033[0;34m"; // 蓝色文本
    public static final String PURPLE = "\033[0;35m"; // 紫色文本
    public static final String CYAN = "\033[0;36m"; // 青色文本
    public static final String WHITE = "\033[0;37m"; // 白色文本

    public static void main(String[] args) {

        // 配置服务器信息
        String serverAddress = "18.141.209.212"; // 替换为实际服务器地址
        int serverPort = 8888; // 替换为实际服务器端口

        // 创建 UdpClient 实例
        UdpClient udpClient = new UdpClient(serverAddress, serverPort);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            clearConsole();
            displayMenu(); // 固定菜单栏
            // 展示菜单
            // System.out.println("\nSelect an action:");
            // System.out.println("1. Query Flight IDs");
            // System.out.println("2. Query Flight Details");
            // System.out.println("3. Reserve Seats");
            // System.out.println("4. Monitor Flight");
            // System.out.println("5. Exit");

            int choice = -1; // 初始化choice为无效值

            // 捕获用户输入的异常
            while (choice == -1) {
                System.out.print("Your choice: ");
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // 消耗换行符
                } catch (InputMismatchException e) {
                    System.out.println(YELLOW + "Invalid input, please enter a valid number." + RESET);
                    scanner.next(); // 消耗错误的输入，继续循环等待用户输入
                }
            }

            Map<String, String> requestPayload = new HashMap<>();
            String requestId = String.valueOf(System.currentTimeMillis()); // 使用当前时间戳生成 request_id
            requestPayload.put("request_id", requestId);

            String actionType = ""; // 根据用户的选择设置 actionType

            switch (choice) {
                case 1: // 查询航班 ID
                    System.out.print("Enter source: ");
                    String source = scanner.nextLine();
                    System.out.print("Enter destination: ");
                    String destination = scanner.nextLine();

                    requestPayload.put("source", source);
                    requestPayload.put("destination", destination);
                    requestPayload.put("action", "1");
                    actionType = "QueryFlightIds";
                    break;

                case 2: // 查询航班详情
                    System.out.print("Enter flight ID: ");
                    String flightId = scanner.nextLine();
                    requestPayload.put("flight_id", flightId);
                    requestPayload.put("action", "2");
                    actionType = "QueryFlightDetails";
                    break;

                case 3: // 预订座位
                    System.out.print("Enter flight ID: ");
                    flightId = scanner.nextLine();
                    System.out.print("Enter number of seats to reserve: ");
                    String seats = scanner.nextLine();

                    requestPayload.put("flight_id", flightId);
                    requestPayload.put("seats", seats);
                    requestPayload.put("action", "3");
                    actionType = "ReserveSeats";
                    break;

                case 4: // 监控航班
                    System.out.print("Enter flight ID: ");
                    flightId = scanner.nextLine();
                    System.out.print("Enter monitor interval (seconds): ");
                    String monitorInterval = scanner.nextLine();

                    requestPayload.put("flight_id", flightId);
                    requestPayload.put("monitor_interval", monitorInterval);
                    requestPayload.put("action", "4");
                    actionType = "MonitorFlight";
                    break;
                case 5: // 预订价格最便宜航班的座位
                    System.out.print("Enter source: ");
                    source = scanner.nextLine();
                    System.out.print("Enter destination: ");
                    destination = scanner.nextLine();

                    requestPayload.put("source", source);
                    requestPayload.put("destination", destination);
                    requestPayload.put("action", "6");
                    actionType = "ReserveSeatsCheapestPrice";
                    break;

                case 6: // 预订某个价格以下的所有座位
                    System.out.print("Enter source: ");
                    source = scanner.nextLine();
                    System.out.print("Enter destination: ");
                    destination = scanner.nextLine();
                    System.out.print("Enter maximum price: ");
                    String maxPrice = scanner.nextLine();

                    requestPayload.put("source", source);
                    requestPayload.put("destination", destination);
                    requestPayload.put("max_price", maxPrice);
                    requestPayload.put("action", "7");
                    actionType = "ReserveSeatsBelowPrice";
                    break;

                case 7: // 重置所有航班座位数据
                    requestPayload.put("action", "8");
                    actionType = "ResetSeatsData";
                    break;

                case 8: // 退出
                    running = false;
                    System.out.println("Exiting...");
                    continue;

                default:
                    System.out.println(YELLOW + "Invalid choice, please try again." + RESET);
                    choice = -1; // 重置choice，使用户重新选择
                    continue;
            }

            // 发送请求并获取响应
            try {
                Response response = udpClient.sendRequest(requestPayload, actionType);
                // 打印 Response 对象
                // System.out.println("Response received: " + response + "\n");

                if (response.message != null) {
                    System.out.println("\n" + "message: " + response.message + "\n");
                }

                if (choice == 1) {
                    if (((Map<String, Object>) response.additionalData).get("flight_ids") != null) {
                        System.out.println("\nFlight ids: "
                                + ((Map<String, Object>) response.additionalData).get("flight_ids") + "\n");
                    }
                } else if (choice == 2) {
                    if (((Map<String, Object>) response.additionalData).get("departure_time") != null) {
                        System.out.println("\n" + "Departure time: "
                                + ((Map<String, Object>) response.additionalData).get("departure_time"));
                        System.out.println(
                                "Airfare: " + ((Map<String, Object>) response.additionalData).get("airfare"));
                        System.out.println("Seats available: "
                                + ((Map<String, Object>) response.additionalData).get("seats_available")
                                + "\n");
                    }
                } else if (choice == 3) {
                    if (!"500".equals(response.status)) {
                        System.out.println("\n" + "Seats successfully reserved" + "\n");
                    }
                } else if (choice == 4) {
                    if (!"500".equals(response.status)) {
                        System.out.println("\n" + "Monitor successful implementation" + "\n");
                    }
                } else if (choice == 5) {
                    if (!"500".equals(response.status)) {
                        System.out.println("\n" + "Booking successful" + "\n");
                    }
                } else if (choice == 6) {
                    if (!"500".equals(response.status)) {
                        System.out.println("\n" + "Booking successful" + "\n");
                    }
                } else if (choice == 7) {
                    if (!"500".equals(response.status)) {
                        System.out.println("\n" + "Reset successful" + "\n");
                    }
                }
                // System.out.println(resultMap.get("message"));

            } catch (Exception e) {
                System.err.println(YELLOW + "An error occurred: " + e.getMessage() + RESET);
                e.printStackTrace();
            }

            // 捕获用户输入的异常
            System.out.print(YELLOW + "Press any key to continue..." + RESET);
            scanner.nextLine();
        }

        scanner.close();

    }

    // 清理控制台屏幕
    public static void clearConsole() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }

    // 显示固定的菜单栏
    public static void displayMenu() {
        System.out
                .println(
                        "\n******************************** Select an action:            ********************************");
        System.out.println(
                "******************************** 1. Query Flight IDs          ********************************");
        System.out.println(
                "******************************** 2. Query Flight Details      ********************************");
        System.out.println(
                "******************************** 3. Reserve Seats             ********************************");
        System.out.println(
                "******************************** 4. Monitor Flight            ********************************");
        System.out.println(
                "******************************** 5. Reserve Cheapest Flight   ********************************");
        System.out.println(
                "******************************** 6. Reserve Seats Below Price ********************************");
        System.out
                .println(
                        "******************************** 7. Reset All Flight Data     ********************************");
        System.out.println(
                "******************************** 8. Exit                      ********************************\n");
    }
}
