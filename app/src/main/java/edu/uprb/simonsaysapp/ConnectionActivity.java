package edu.uprb.simonsaysapp;

import android.os.Bundle;
import android.app.Activity;

import android.widget.TextView;


import uprb.edu.simonsaysappserver.R;
import edu.uprb.sockets.Server;

public class ConnectionActivity extends Activity {

    Server server;
    public TextView infoip, msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        infoip = (TextView) findViewById(R.id.infoip);
        msg = (TextView) findViewById(R.id.msg);
        server = new Server(this);
        infoip.setText(server.getIpAddress()+":"+server.getPort());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.onDestroy();
    }

}
