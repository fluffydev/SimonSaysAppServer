package edu.uprb.simonsaysapp;

/*

 * File: SimonSaysFragment.java

 * Author: Víctor M. Martínez 845-09-4440

 * Course: SICI 4997-KJ1, Prof. Antonio F. Huertas

 * Date: Febreuary 17, 2018

 * This application simulates a simon says game between two players

 * using a single android device.

 */

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import uprb.edu.simonsaysappserver.R;


public class SimonSaysFragment extends Fragment {

    /* Fragment Fields. */
    private TextView clicksEditText, playerLabel;
    private Button redButton, blueButton, yellowButton, cyanButton,
            greenButton, magentaButton;
    private TextView resultTextView, pOnePattern, pTwoPattern;
    private int clicks, playerOneCtr, playerTwoCtr;
    private int[] playerOne, playerTwo;

    /* Determines if the match has ended. */
    private boolean determineEnd = false;


    /* To save values for app calculation. */
    private SharedPreferences preferences;

    /*
     * Called when the activity is starting, Initializes
     * the activity.
     * @Param: savedInstanceState The last known state of this application
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Set the default value for the preferences.
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);

        /* Creates a shared preferences object. */
        // Links shared preferences logical name with the physical name of the file.
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Turns on the options menu:
        setHasOptionsMenu(true);
    }

    /* Initializes layout assets that belong to the given fragment.
     * @param: inflater Inflates the assets
     * @param: container Stores the assets
     * @param: savedInstanceState The app's last recorded state
     * @return: The displayed view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_simonsays, container,
                false);

        /* Creating the reference to each widget. */
        clicksEditText = (TextView) view.findViewById(R.id.clicksEditText);
        playerLabel = (TextView) view.findViewById(R.id.playerLabel);
        redButton = (Button) view.findViewById(R.id.redButton);
        blueButton = (Button) view.findViewById(R.id.blueButton);
        yellowButton = (Button) view.findViewById(R.id.yellowButton);
        cyanButton = (Button) view.findViewById(R.id.cyanButton);
        greenButton = (Button) view.findViewById(R.id.greenButton);
        magentaButton = (Button) view.findViewById(R.id.magentaButton);

        resultTextView = (TextView) view.findViewById(R.id.resultTextView);
        pOnePattern = (TextView) view.findViewById(R.id.pOnePattern);
        pTwoPattern = (TextView) view.findViewById(R.id.pTwoPattern);


        /* Setting the appropriate listeners. */
        redButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                resetColors();
                redButton.setBackgroundColor(Color.WHITE);
                calculateArray(Colors.RED);
            }
        });

        blueButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                resetColors();
                blueButton.setBackgroundColor(Color.WHITE);
                calculateArray(Colors.BLUE);
            }
        });

        yellowButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                resetColors();
                yellowButton.setBackgroundColor(Color.WHITE);
                calculateArray(Colors.YELLOW);
            }
        });

        cyanButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                resetColors();
                cyanButton.setBackgroundColor(Color.WHITE);
                calculateArray(Colors.CYAN);
            }
        });

        greenButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                resetColors();
                greenButton.setBackgroundColor(Color.WHITE);
                calculateArray(Colors.GREEN);
            }
        });

        magentaButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){
                resetColors();
                magentaButton.setBackgroundColor(Color.WHITE);
                calculateArray(Colors.MAGENTA);
            }
        });
        return view;
    }

    /*
    * Handles the event for menu item selection. Returns
    * true if one of the options is selected. If no option is selected,
    * superclass handles event.
    * @param item The clicked item on the menu
    * @return True after processing given selection
    */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         // Attempt to get an instance of the SettingsFragment Object
        SettingsFragment settingsFragment = (SettingsFragment) getFragmentManager()
                .findFragmentById(R.id.settings_fragment);

        // If the fragment is null, displays the appropriate menu
        if(settingsFragment == null)
            inflater.inflate(R.menu.fragment_simonsays, menu);
        else
            ;
    }

    /*
    * Handles the event for menu item selection. Returns
    * true if one of the options is selected. If no option is selected,
    * superclass handles event.
    * @param item The clicked item on the menu
    * @return True after processing given selection
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reset:
                resetValues();
                resetColors();
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(getActivity(),
                        SettingsActivity.class));
                return true;
            case R.id.menu_about:
                startActivity(new Intent(getActivity(),
                        AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * Creates the array given by the player.
     * @param clr The color of the pressed button
     */
    public void calculateArray(Colors clr) {
        if(playerOneCtr < playerOne.length) {
            playerOne[playerOneCtr] = clr.toInt();
            playerOneCtr++;

            if (playerOneCtr == playerOne.length) {
                playerLabel.setText(R.string.player2_label);
            }
        }

        else if (playerTwoCtr < playerTwo.length && playerOneCtr == clicks){
            playerTwo[playerTwoCtr] = clr.toInt();
            playerTwoCtr++;
        }

        if(playerTwoCtr == playerTwo.length)
            calculateResult();
            determineEnd = true;
    }

    /*
     * Calculates the result of the game.
     */
    public void calculateResult(){
        String str1 = "";
        String str2 = "";
        for(int idx = 0; idx < playerOne.length; idx++){
            str1 += Colors.toText(playerOne[idx]);
            str2 += Colors.toText(playerTwo[idx]);

            // Second Player loss condition.
            if(playerOne[idx] != playerTwo[idx]) {
                resultTextView.setText(R.string.fail_label);
                break;
            }

            if(idx != playerOne.length - 1 ) {
                str1 += ", ";
                str2 += ", ";
            }

            if(idx == playerOne.length - 1)
                resultTextView.setText(R.string.win_label);
        }
        pOnePattern.setText(str1);
        pTwoPattern.setText(str2);
    }


    /*
    * Saves the current values being processed by the application
    * when it is paused.
    */
    @Override
    public void onPause(){
        Editor editor = preferences.edit();
        editor.putBoolean("determineEnd", determineEnd);

        editor.putInt("p1_counter", playerOneCtr);
        editor.putInt("p2_counter", playerTwoCtr);

        for(int idx = 0; idx < clicks; idx++){
            editor.putString("p1_array_" + idx, playerOne[idx] + "");
            editor.putString("p2_array_" + idx, playerTwo[idx] + "");
        }

        editor.commit();
        super.onPause();
    }

    /*
     * Sets each field in the activity to the values that had been
     * saved before a pause.
     */
    @Override
    public void onResume() {
        super.onResume();

        /* Getting preferences. */
        clicks = Integer.parseInt(preferences.getString("pref_clicks", "5"));
        clicksEditText.setText(preferences.getString("pref_clicks", "5"));

         /* Each player's options. */
        playerOne = new int[clicks];
        playerTwo = new int[clicks];

        playerOneCtr = preferences.getInt("p1_counter", 0);
        playerTwoCtr = preferences.getInt("p2_counter", 0);

        /* Acquiring game state, */
        determineEnd = preferences.getBoolean("determineEnd", false);

        for (int idx = 0; idx < clicks; idx++){
                playerOne[idx] = Integer.parseInt(preferences.getString(
                        "p1_array_" + idx, "0"));
        }
        for (int idx = 0; idx < clicks; idx++){
            playerTwo[idx] = Integer.parseInt(preferences.getString(
                    "p2_array_" + idx, "0"));
        }

        if(playerTwoCtr == clicks && determineEnd == true) {
            calculateResult();
        }

    }

    /*
     * Returns each Button to their default color.
     */
    public void resetColors(){
        redButton.setBackgroundColor(Color.RED);
        blueButton.setBackgroundColor(Color.BLUE);
        yellowButton.setBackgroundColor(Color.YELLOW);
        cyanButton.setBackgroundColor(Color.CYAN);
        greenButton.setBackgroundColor(Color.GREEN);
        magentaButton.setBackgroundColor(Color.MAGENTA);
    }

    /*
     * This method both initializes the values for the variables in this application
     * and resets them when necessary.
     */
    public void resetValues (){
        /* Initializing the counters to keep track of each array. */
        playerOneCtr = playerTwoCtr = 0;

        determineEnd = false;

        /* Each player's options. */
        playerOne = new int[clicks];
        playerTwo = new int[clicks];

        playerLabel.setText(R.string.player1_label);

        resultTextView.setText(" ");

        pOnePattern.setText(" ");
        pTwoPattern.setText(" ");
    }
}



