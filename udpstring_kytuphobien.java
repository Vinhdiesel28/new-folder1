

import java.io.*;
import java.net.*;
import java.util.*;

public class udpstring_kytuphobien {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        String code = ";B23DCCN934;mWHxrLIg";
        InetAddress sA = InetAddress.getByName("36.50.135.242");
        int port = 2208;

        DatagramPacket dp = new DatagramPacket(code.getBytes(), code.length(), sA, port);
        socket.send(dp);

        byte[] bf = new byte[1024];
        DatagramPacket dp1 = new DatagramPacket(bf, bf.length);
        socket.receive(dp1);
        String s = new String(dp1.getData(), 0, dp1.getLength()).trim();
        System.out.println(s);

        String[] parts = s.split(";");
        String requestId = parts[0];
        String data = parts[1].trim();

        // Đếm số lần xuất hiện từng ký tự (theo thứ tự xuất hiện lần đầu)
        Map<Character, Integer> countMap = new LinkedHashMap<>();
        for (char c : data.toCharArray()) {
            countMap.put(c, countMap.getOrDefault(c, 0) + 1);
        }

        // Tìm ký tự xuất hiện nhiều nhất (ưu tiên ký tự xuất hiện đầu tiên nếu bằng nhau)
        char maxChar = data.charAt(0);
        int maxCount = 0;
        for (Map.Entry<Character, Integer> e : countMap.entrySet()) {
            if (e.getValue() > maxCount) {
                maxCount = e.getValue();
                maxChar = e.getKey();
            }
        }

        // Tìm các vị trí xuất hiện của ký tự đó (1-indexed)
        StringBuilder positions = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == maxChar) {
                if (positions.length() > 0) positions.append(",");
                positions.append(i + 1);
            }
        }

        String result = requestId + ";" + maxChar + ":" + positions.toString();
        System.out.println(result+",");
        DatagramPacket dp2 = new DatagramPacket(result.getBytes(), result.length(), sA, port);
        socket.send(dp2);
        socket.close();
    }
}
