package UDP;

// [Mã câu hỏi (qCode): 78CCQ6xD]. Liệt kê số nguyên tố
// Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2207.
// a. Gửi thông điệp ";studentCode;qCode"
// b. Nhận thông điệp "requestId;n" (n là số nguyên ngẫu nhiên <= 100)
// c. Tìm n số nguyên tố đầu tiên -> gửi "requestId;p1,p2,...,pn"
// d. Đóng socket và kết thúc chương trình.

import java.io.*;
import java.net.*;
import java.util.*;

public class udpdatatype_lietkesonguyen {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        String code = ";B21DCCN441;78CCQ6xD";
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
        int n = Integer.parseInt(parts[1].trim());

        // Tìm n số nguyên tố đầu tiên
        List<Integer> primes = new ArrayList<>();
        int candidate = 2;
        while (primes.size() < n) {
            if (isPrime(candidate)) {
                primes.add(candidate);
            }
            candidate++;
        }

        // Tạo chuỗi kết quả "p1,p2,...,pn"
        StringBuilder sb = new StringBuilder(requestId + ";");
        for (int i = 0; i < primes.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(primes.get(i));
        }

        String result = sb.toString();
        System.out.println(result);
        DatagramPacket dp2 = new DatagramPacket(result.getBytes(), result.length(), sA, port);
        socket.send(dp2);
        socket.close();
    }

    static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
