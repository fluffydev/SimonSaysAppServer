package edu.uprb.sockets;

/*

 * File: Server.java

 * Author: Víctor M. Martínez 845-09-4440

 * Course: SICI 4037-LJ1, Dr. Juan M. Solá

 * Date: December 7, 2018

 * This class handles server side operations for the Simon Says app

 * It handles operations through an instance of the Server Socket object

 * of the Java API.

 */

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
 * Created by Víctor M. Martínez on 12/6/2018.
 */

public class Server {
    ConnectionActivity activity;
    ServerSocket serverSocket;
    String message = "";
    static final int socketServerPORT = 6000;

    /*

     * Initializes the socked server thread that handles incoming communication from the

     * client application.

     */
    public Server(ConnectionActivity activity) {
        this.activity = activity;
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    /*
     * Returns the port of the server socket.
     */
    public int getPort() {
        return socketServerPORT;
    }

    /*
     * Destroys the server sockets on closing application.
     */
    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    /*

     * Receives an interaction request from the client application and displays

     * a message to signal successful connection.

     */
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

                e.printStackTrace();
            }
        }
    }

    /*

     * Creates the socket server reply thread in order to generate a pattern to return

     * to the client app.

     */
    private class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        private int cnt;
        private int playerCtr;

        SocketServerReplyThread(Socket socket, int c, int ctr) {
            hostThreadSocket = socket;
            cnt = c;
            playerCtr = ctr;
        }

        /*

         * Runs the reply thread and generates a random integer array to server

         * as input to the client app. This array generates the random pattern the user

         * should match.

         */
        @Override
        public void run() {

            String msgReply = "Hello from Server, Combination " + cnt + " is ready! Press \"Play Game\" to Play";

            try {

                DataOutputStream dataOut = new DataOutputStream((hostThreadSocket.getOutputStream()));

                dataOut.writeInt(playerCtr);

                Random random = new Random();
                for (int i = 0; i < playerCtr; i++) { dataOut.writeInt(random.nextInt(playerCtr + 1)); }

                PrintStream printStream = new PrintStream(dataOut);
                printStream.print(msgReply);
                printStream.close();

                message += "replied: " + msgReply + "\n";

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {activity.msg.setText(message);}
                });

            } catch (IOException e) {
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

    /*
     * Gets the IP Address of the device running the server application.
     */
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
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }
}