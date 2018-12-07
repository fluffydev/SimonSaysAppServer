package edu.uprb.simonsaysapp;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

import uprb.edu.simonsaysappserver.R;
import edu.uprb.sockets.Client;

/**
 * Created by Fluffy on 12/6/2018.
 */

public class ConnectFragment extends Fragment {

    private TextView response;
    private EditText editAddress, editPort;
    private Button btnConnect, btnClear;

    //@Override
    public void OnCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.connect_fragment, container, false);

        editAddress = (EditText) view.findViewById(R.id.editAddress);
        editPort = (EditText) view.findViewById(R.id.editPort);
        btnConnect = (Button) view.findViewById(R.id.btnConnect);
        btnClear = (Button) view.findViewById(R.id.btnClear);
        response = (TextView) view.findViewById(R.id.response);

        btnConnect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Client myClient = new Client(editAddress.getText()
                        .toString(), Integer.parseInt(editPort
                        .getText().toString()), response);
                myClient.execute();
            }
        });

        btnClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                response.setText("");
            }
        });

        return view;

    }

}
