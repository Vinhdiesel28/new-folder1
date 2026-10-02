package UDP;

// [Mã câu hỏi (qCode): GfeNSBMT]. [Loại bỏ ký tự đặc biệt, số, trùng và giữ nguyên thứ tự xuất hiện]
// Một chương trình server cho phép kết nối qua giao thức UDP tại cổng 2208.
// a. Gửi thông điệp ";studentCode;qCode"
// b. Nhận thông điệp "requestId;strInput"
// c. Loại bỏ ký tự đặc biệt, số, trùng; giữ nguyên thứ tự xuất hiện -> gửi "requestId;strOutput"
// d. Đóng socket và kết thúc chương trình.

import java.io.*;
import java.net.*;
import java.util.*;

public class udpstring_lockytu {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        String code = ";B21DCCN441;GfeNSBMT";
        InetAddress sA = InetAddress.getByName("203.162.10.109");
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
        String strInput = parts[1];

        // Loại bỏ ký tự đặc biệt, số, trùng; giữ nguyên thứ tự xuất hiện
        StringBuilder ans = new StringBuilder();
        Set<Character> seen = new LinkedHashSet<>();
        for (char c : strInput.toCharArray()) {
            if (Character.isLetter(c) && !seen.contains(c)) {
                seen.add(c);
                ans.append(c);
            }
        }

        String result = requestId + ";" + ans.toString();
        System.out.println(result);
        DatagramPacket dp2 = new DatagramPacket(result.getBytes(), result.length(), sA, port);
        socket.send(dp2);
        socket.close();
    }
}
