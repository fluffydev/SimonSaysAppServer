package edu.uprb.simonsaysapp;

/*

 * File: ConnectionActivity.java

 * Author: Víctor M. Martínez 845-09-4440

 * Course: SICI 4037-LJ1, Dr. Juan M. Solá

 * Date: December 7, 2018

 * This class creates the main UI Activity for the Server Application.

 */

import android.os.Bundle;
import android.app.Activity;

import android.widget.TextView;


import uprb.edu.simonsaysappserver.R;
import edu.uprb.sockets.Server;

/*
 * This is the main UI Activity of the application.
 */
public class ConnectionActivity extends Activity {

    Server server;
    public TextView infoip, msg;

    @Override
    /*
     * Creates the activity and initializes all of its visual components.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        infoip = findViewById(R.id.infoip);
        msg = findViewById(R.id.msg);
        server = new Server(this);
        infoip.setText(server.getIpAddress()+":"+server.getPort());

    }


    /*

     * Destroys both the activity and the server socket thread upon closing the

     * application.

     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.onDestroy();
    }

}
