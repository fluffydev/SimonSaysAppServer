package edu.uprb.sockets;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Fluffy on 12/6/2018.
 */

public class Client extends AsyncTask<Void, Void, String> {

    private String destAddress;
    private int destPort;
    String response = "";
    private TextView textResponse;

    public Client(String address, int port, TextView response){
        destAddress = address;
        destPort = port;
        textResponse = response;
    }

    @Override
    protected String doInBackground(Void... arg0){

        Socket socket = null;

        try{

            socket = new Socket(destAddress, destPort);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);

            byte[] buffer = new byte[1024];

            int bytesRead;
            InputStream inputStream = socket.getInputStream();

            while((bytesRead = inputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                response += byteArrayOutputStream.toString("UTF-8");
            }
        } catch (UnknownHostException e) {

            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {

            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return response;

    }
    @Override
    protected void onPostExecute(String result) {
        textResponse.setText(response);
        super.onPostExecute(result);
    }
}
