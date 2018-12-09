package edu.uprb.sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;

import edu.uprb.simonsaysapp.ConnectionActivity;

/**
 * Created by Fluffy on 12/6/2018.
 */

public class Server {
    ConnectionActivity activity;
    ServerSocket serverSocket;
    String message = "";
    static final int socketServerPORT = 6000;

    public Server(ConnectionActivity activity) {
        this.activity = activity;
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public int getPort() {
        return socketServerPORT;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class SocketServerThread extends Thread {

        int count = 0;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(socketServerPORT);

                while (true) {
                    Socket socket = serverSocket.accept();
                    DataInputStream dis = new DataInputStream(socket.getInputStream());

                    int playerCtr = dis.readInt();

                    count++;

                    message += "#" + count + " from "
                            + socket.getInetAddress() + ":"
                            + socket.getPort() + "\n" + "Repetitions: " + playerCtr + "\n";

                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            activity.msg.setText(message);
                        }
                    });

                    SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(
                            socket, count, playerCtr);
                    socketServerReplyThread.run();

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        private int cnt;
        private int playerCtr;

        SocketServerReplyThread(Socket socket, int c, int ctr) {
            hostThreadSocket = socket;
            cnt = c;
            playerCtr = ctr;
        }

        //int[] combination = SimonSaysUtil.generateCombination(playerCtr);

        @Override
        public void run() {

            //ByteArrayOutputStream out = new ByteArrayOutputStream();

            //OutputStream outputStream;

            String msgReply = "Hello from Server, Combination " + cnt + " is ready! Press \"Play Game\" to Play";

            try {
                //dataOut = new DataOutputStream(hostThreadSocket.getOutputStream());

                DataOutputStream dataOut = new DataOutputStream((hostThreadSocket.getOutputStream()));

                dataOut.writeInt(playerCtr);

                Random random = new Random();
                for (int i = 0; i < playerCtr; i++) { dataOut.writeInt(random.nextInt(playerCtr + 1)); }

//                outputStream = hostThreadSocket.getOutputStream();
                PrintStream printStream = new PrintStream(dataOut);
                printStream.print(msgReply);
                printStream.close();

                message += "replayed: " + msgReply + "\n";

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {activity.msg.setText(message);}
                });

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                message += "Something wrong! " + e.toString() + "\n";
            }

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    activity.msg.setText(message);
                }
            });
        }
    }

    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "\nServer running at : "
                                + inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }
}