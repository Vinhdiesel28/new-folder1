package UDP;

// [Mã câu hỏi (qCode): QABGDFxd]. Quản lý sinh viên - xử lý đối tượng Student
// Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2209.
// Đối tượng: UDP.Student (id, code, name, email), serialVersionUID = 20171107
//
// Xử lý:
// - Chuẩn hóa tên: chữ cái đầu tiên in hoa, còn lại in thường
//   Ví dụ: "NGUYEN VAN TUAN NAM" -> "Nguyen van tuan nam"
// - Tạo email từ họ tên: lấy tên (từ cuối) + chữ đầu của họ và tên đệm + @ptit.edu.vn
//   Ví dụ: "nguyen van tuan nam" -> "namnvt@ptit.edu.vn"

import java.io.*;
import java.net.*;

public class udpobject_sinhvien {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        String code = ";B21DCCN441;QABGDFxd";
        InetAddress sA = InetAddress.getByName("203.162.10.109");
        int port = 2209;

        DatagramPacket dp = new DatagramPacket(code.getBytes(), code.length(), sA, port);
        socket.send(dp);

        byte[] bf = new byte[4096];
        DatagramPacket dp1 = new DatagramPacket(bf, bf.length);
        socket.receive(dp1);

        // 8 byte đầu là requestId
        String requestId = new String(dp1.getData(), 0, 8);

        // Các byte còn lại là đối tượng Student
        ByteArrayInputStream bais = new ByteArrayInputStream(dp1.getData(), 8, dp1.getLength() - 8);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Student student = (Student) ois.readObject();
        ois.close();

        System.out.println("Received name: " + student.getName());

        // Lưu tên gốc lowercase để tạo email
        String origName = student.getName().toLowerCase().trim();

        // Chuẩn hóa tên: chữ cái đầu tiên in hoa, còn lại in thường
        student.setName(normalizeName(origName));

        // Tạo email: "nguyen van tuan nam" -> "namnvt@ptit.edu.vn"
        student.setEmail(createEmail(origName));

        System.out.println("name: " + student.getName());
        System.out.println("email: " + student.getEmail());

        // Gửi lại đối tượng Student: 8 byte requestId + object bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(student);
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

    // "NGUYEN VAN TUAN NAM" -> "Nguyen van tuan nam" (chỉ chữ đầu tiên của toàn chuỗi in hoa)
    static String normalizeName(String name) {
        if (name == null || name.isEmpty()) return name;
        String lower = name.toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

    // "nguyen van tuan nam" -> "namnvt@ptit.edu.vn"
    // tên (từ cuối) + chữ đầu mỗi từ còn lại (họ và tên đệm) + @ptit.edu.vn
    static String createEmail(String name) {
        String[] words = name.trim().toLowerCase().split("\\s+");
        String ten = words[words.length - 1]; // tên (từ cuối)
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < words.length - 1; i++) {
            prefix.append(words[i].charAt(0)); // chữ đầu họ và tên đệm
        }
        return ten + prefix.toString() + "@ptit.edu.vn";
    }
}


