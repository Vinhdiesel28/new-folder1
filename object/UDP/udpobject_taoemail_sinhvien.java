package UDP;

import java.net.*;
import java.io.*;
import java.util.*;

public class udpobject_taoemail_sinhvien {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress host = InetAddress.getByName("36.50.135.242");

        byte[] send = ";B23DCCN934;OPJ1DOEt".getBytes();
        socket.send(new DatagramPacket(send, send.length, host, 2209));

        byte[] buf = new byte[4096];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        String requestId = new String(buf, 0, 8);

        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(buf, 8, packet.getLength() - 8)
        );

        Student st = (Student) ois.readObject();

        String[] w = st.getName().trim().toLowerCase().split("\\s+");
        StringBuilder name = new StringBuilder();

        for (String x : w) {
            if (name.length() > 0) name.append(" ");
            name.append(Character.toUpperCase(x.charAt(0)))
                    .append(x.substring(1));
        }

        st.setName(name.toString());

        StringBuilder email = new StringBuilder(w[w.length - 1]);
        for (int i = 0; i < w.length - 1; i++)
            email.append(w[i].charAt(0));

        st.setEmail(email + "@ptit.edu.vn");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(st);
        oos.flush();

        byte[] obj = bos.toByteArray();
        byte[] result = new byte[8 + obj.length];

        System.arraycopy(requestId.getBytes(), 0, result, 0, 8);
        System.arraycopy(obj, 0, result, 8, obj.length);

        socket.send(new DatagramPacket(result, result.length, host, 2209));
        socket.close();
    }
}