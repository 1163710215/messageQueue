package Customer;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class c3 {
    public static void main(String[] args) {
        String serverName = "localhost";
        int port = 6667;

        System.out.println("连接到主机：" + serverName + " ，端口号：" + port);
        try {
            Scanner scanner = new Scanner(System.in);
            Socket client = new Socket(serverName, port);
            System.out.println("远程主机地址：" + client.getRemoteSocketAddress());

            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            while (true) {
                try {
                    String message = scanner.nextLine();
                    if (message.equals("getMessages")) {
                        out.writeUTF(message);
                        String receive = null;
                        do {
                            receive = in.readUTF();
                            System.out.println(receive);
                        } while (receive != null);
                    } else if (message.equals("send")) {

                        String queue = scanner.nextLine();
                        String s = scanner.nextLine();
                        out.writeUTF(message);
                        out.writeUTF(queue);
                        out.writeUTF(s);
                    } else if (message.equals("subscribe")) {
                        String queue = scanner.nextLine();
                        out.writeUTF(message);
                        out.writeUTF(queue);

                    } else if (message.equals("unsubscribe")) {
                        String queue = scanner.nextLine();
                        out.writeUTF(message);
                        out.writeUTF(queue);
                    } else if (message.equals("-1")) {
                        break;
                    } else {
                        out.writeUTF(message);
                        System.out.println(in.readUTF());

                    }


                } catch (SocketTimeoutException e) {
                    System.out.println("time out ,restart");
                }
            }


            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
