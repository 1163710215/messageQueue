package MQMainLogic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class NetConnection implements Runnable {
    static ArrayList<MessageQueue> messageQueues = new ArrayList<>();
    static ArrayList<Observer> observers = new ArrayList<>();
    NetConnection netConnection;
    ServerSocket serverSocket;
    int port = 6666;

    private NetConnection() throws IOException {

    }

    private NetConnection(int i) throws IOException {
        port = i;
    }

    public static void main(String[] args) {
        try {
            new NetConnection();
            Runnable myRunnable1 = new NetConnection(6666);
            Runnable myRunnable2 = new NetConnection(6667);
            Runnable myRunnable3 = new NetConnection(6668);
            Runnable myRunnable4 = new NetConnection(6669);
            Runnable myRunnable5 = new NetConnection(6670);
            Thread thread1 = new Thread(myRunnable1);
            Thread thread2 = new Thread(myRunnable2);
            Thread thread3 = new Thread(myRunnable3);
            Thread thread4 = new Thread(myRunnable4);
            Thread thread5 = new Thread(myRunnable5);
            thread1.start();
            thread2.start();
            thread3.start();
            thread4.start();
            thread5.start();
        } catch (IOException e) {
            System.out.println("ON USE");
            e.printStackTrace();
        }
    }

    private void startSocket() throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                SocketAddress remoteAddress = socket.getRemoteSocketAddress();

                Observer observer = null;
                for (Observer o : observers) {
                    if (o.getObserverName().equals(remoteAddress)) {
                        observer = o;
                        break;
                    }
                }
                if (observer == null) {
                    observer = new ObserverEntity(remoteAddress);
                    observers.add(observer);
                }
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                String message = null;
                do {
                    message = inputStream.readUTF();
                    if (message.equals("send")) {
                        String queueName = inputStream.readUTF();
                        MessageQueueEntity mqe = null;
                        for (MessageQueue mq : messageQueues) {
                            if (mq.name.equals(queueName)) {
                                mqe = (MessageQueueEntity) mq;
                                break;
                            }
                        }
                        if (mqe == null) {
                            mqe = new MessageQueueEntity(queueName);
                            messageQueues.add(mqe);
                        }
                        String toSend = inputStream.readUTF();
                        mqe.setSupperContent(toSend);
                        System.out.println("receive " + toSend + " from " + observer.getObserverName());

                    } else if (message.equals("subscribe")) {
                        String queueName = inputStream.readUTF();
                        MessageQueueEntity mqe = null;
                        for (MessageQueue mq : messageQueues) {
                            if (mq.name.equals(queueName)) {
                                mqe = (MessageQueueEntity) mq;
                                break;
                            }
                        }
                        if (mqe == null) {
                            mqe = new MessageQueueEntity(queueName);
                            messageQueues.add(mqe);
                        }
                        mqe.attach(observer);
                        System.out.println(observer.getObserverName() + " attach " + queueName);
                    } else if (message.equals("unsubscribe")) {
                        String queueName = inputStream.readUTF();
                        MessageQueueEntity mqe = null;
                        for (MessageQueue mq : messageQueues) {
                            if (mq.name.equals(queueName)) {
                                mqe = (MessageQueueEntity) mq;
                                break;
                            }
                        }
                        if (mqe == null) {
                            continue;
                        }
                        mqe.detach(observer);
                        System.out.println(observer.getObserverName() + " cancel attach " + queueName);


                    } else if (message.equals("getMessages")) {
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        for (String s : ((ObserverEntity) observer).getMessages()) {
                            out.writeUTF(s + '\n');
                            System.out.println(observer.getObserverName() + " fetch message: " + s);

                        }
                        System.out.println(observer.getObserverName() + " fetch messages over");


                    } else {
                        System.out.println("order Error");
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        out.writeUTF("order Error\n");
                    }
                } while (!message.equals("-1"));

            } catch (SocketTimeoutException s) {
                System.out.println("wait too long ;break");
                continue;
            } catch (IOException e) {
                System.out.println("over");
                continue;
            }

        }
    }

    @Override
    public void run() {
        try {

            startSocket();
        } catch (IOException e) {
            System.out.println("port on use");
        }

    }
}
