package UDP;

import java.net.*;
import java.io.*;

public class udpobject_dinhdangkhachhang {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress host = InetAddress.getByName("36.50.135.242");

        byte[] send = ";B23DCCN934;6sum4mOU".getBytes();
        socket.send(new DatagramPacket(send, send.length, host, 2209));

        byte[] buf = new byte[4096];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        String requestId = new String(buf, 0, 8);

        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(buf, 8, packet.getLength() - 8)
        );

        Customer c = (Customer) ois.readObject();

        String[] w = c.getName().trim().toLowerCase().split("\\s+");

        StringBuilder name = new StringBuilder();
        name.append(w[w.length - 1].toUpperCase()).append(", ");

        for (int i = 0; i < w.length - 1; i++) {
            name.append(Character.toUpperCase(w[i].charAt(0)))
                    .append(w[i].substring(1));
            if (i < w.length - 2) name.append(" ");
        }

        c.setName(name.toString());

        String[] date = c.getDayOfBirth().split("-");
        c.setDayOfBirth(date[1] + "/" + date[0] + "/" + date[2]);

        StringBuilder username = new StringBuilder();
        for (int i = 0; i < w.length - 1; i++)
            username.append(w[i].charAt(0));

        username.append(w[w.length - 1]);
        c.setUserName(username.toString());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(c);
        oos.flush();

        byte[] obj = bos.toByteArray();
        byte[] result = new byte[8 + obj.length];

        System.arraycopy(requestId.getBytes(), 0, result, 0, 8);
        System.arraycopy(obj, 0, result, 8, obj.length);

        socket.send(new DatagramPacket(result, result.length, host, 2209));
        socket.close();
    }
}