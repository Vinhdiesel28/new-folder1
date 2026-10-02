package UDP;

// [Mã câu hỏi (qCode): 0Iend7Pp]. Tổng các chữ số
// Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2207.
// a. Gửi thông điệp ";studentCode;qCode"
// b. Nhận thông điệp "requestId;num" (num là một số nguyên lớn)
// c. Tính tổng các chữ số trong num -> gửi "requestId;sumDigits"
// d. Đóng socket và kết thúc chương trình.

import java.io.*;
import java.net.*;

public class udpdatatype_tongchuuso {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        String code = ";B21DCCN441;0Iend7Pp";
        InetAddress sA = InetAddress.getByName("203.162.10.109");
        int port = 2207;

        DatagramPacket dp = new DatagramPacket(code.getBytes(), code.length(), sA, port);
        socket.send(dp);

        byte[] bf = new byte[1024];
        DatagramPacket dp1 = new DatagramPacket(bf, bf.length);
        socket.receive(dp1);
        String s = new String(dp1.getData(), 0, dp1.getLength()).trim();
        System.out.println(s);

        String[] parts = s.split(";");
        String requestId = parts[0];
        String num = parts[1].trim();

        // Tính tổng các chữ số (xử lý được số nguyên lớn vì dùng String)
        int sum = 0;
        for (char c : num.toCharArray()) {
            if (Character.isDigit(c)) {
                sum += (c - '0');
            }
        }

        String result = requestId + ";" + sum;
        System.out.println(result);
        DatagramPacket dp2 = new DatagramPacket(result.getBytes(), result.length(), sA, port);
        socket.send(dp2);
        socket.close();
    }
}
