package UDP;

// [Mã câu hỏi (qCode): 4MpxJ947]. Khách hàng - xử lý đối tượng Customer
// Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2209.
// Đối tượng: UDP.Customer (id, code, name, dayOfBirth, userName), serialVersionUID = 20151107
//
// Xử lý:
// a. Tên: "nguyen van hai duong" -> "DUONG, Nguyen Van Hai" (tên cuối UPPER, còn lại Title Case)
// b. Ngày sinh: mm-dd-yyyy -> dd/mm/yyyy
// c. userName: "nguyen van hai duong" -> "nvhduong" (chữ đầu mỗi từ đệm/họ + tên cuối đầy đủ)

import java.io.*;
import java.net.*;

public class udpobject_khachhang {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        String code = ";B23DCCN934;mWHxrLIg";
        InetAddress sA = InetAddress.getByName("36.50.135.242");
        int port = 2209;

        DatagramPacket dp = new DatagramPacket(code.getBytes(), code.length(), sA, port);
        socket.send(dp);

        byte[] bf = new byte[4096];
        DatagramPacket dp1 = new DatagramPacket(bf, bf.length);
        socket.receive(dp1);

        // 8 byte đầu là requestId
        String requestId = new String(dp1.getData(), 0, 8);

        // Các byte còn lại là đối tượng Customer
        ByteArrayInputStream bais = new ByteArrayInputStream(dp1.getData(), 8, dp1.getLength() - 8);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Customer customer = (Customer) ois.readObject();
        ois.close();

        // Lưu tên gốc (lowercase) để dùng cho userName
        String origName = customer.getName().toLowerCase().trim();

        // a. Chuẩn hóa tên: "nguyen van hai duong" -> "DUONG, Nguyen Van Hai"
        customer.setName(formatName(origName));

        // b. Định dạng ngày sinh: mm-dd-yyyy -> dd/mm/yyyy
        customer.setDayOfBirth(formatDate(customer.getDayOfBirth()));

        // c. Tạo userName: "nguyen van hai duong" -> "nvhduong"
        customer.setUserName(createUserName(origName));

        System.out.println("name: " + customer.getName());
        System.out.println("dob: " + customer.getDayOfBirth());
        System.out.println("userName: " + customer.getUserName());

        // Gửi lại đối tượng Customer: 8 byte requestId + object bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(customer);
        oos.close();

        byte[] objBytes = baos.toByteArray();
        byte[] reqBytes = requestId.getBytes();
        byte[] result = new byte[8 + objBytes.length];
        System.arraycopy(reqBytes, 0, result, 0, 8);
        System.arraycopy(objBytes, 0, result, 8, objBytes.length);

        DatagramPacket dp2 = new DatagramPacket(result, result.length, sA, port);
        socket.send(dp2);
        socket.close();
    }

    // "nguyen van hai duong" -> "DUONG, Nguyen Van Hai"
    static String formatName(String name) {
        String[] words = name.trim().split("\\s+");
        String lastName = words[words.length - 1].toUpperCase();
        StringBuilder rest = new StringBuilder();
        for (int i = 0; i < words.length - 1; i++) {
            if (i > 0) rest.append(" ");
            String w = words[i];
            rest.append(Character.toUpperCase(w.charAt(0)));
            if (w.length() > 1) rest.append(w.substring(1));
        }
        return lastName + ", " + rest.toString();
    }

    // "10-11-2012" (mm-dd-yyyy) -> "11/10/2012" (dd/mm/yyyy)
    static String formatDate(String date) {
        String[] parts = date.split("-");
        // parts[0]=mm, parts[1]=dd, parts[2]=yyyy
        return parts[1] + "/" + parts[0] + "/" + parts[2];
    }

    // "nguyen van hai duong" -> "nvhduong" (chữ đầu mỗi từ trừ từ cuối + từ cuối đầy đủ)
    static String createUserName(String name) {
        String[] words = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length - 1; i++) {
            sb.append(words[i].charAt(0));
        }
        sb.append(words[words.length - 1]);
        return sb.toString();
    }
}


