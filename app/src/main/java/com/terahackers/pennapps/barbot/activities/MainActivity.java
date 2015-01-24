package com.terahackers.pennapps.barbot.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.terahackers.pennapps.barbot.R;

import java.util.ArrayList;

import at.abraxas.amarino.Amarino;


public class MainActivity extends ActionBarActivity {

    private static final String DEVICE_ADDRESS = "00:13:12:12:68:90";
    private static final String TAG = "MAIN ACTIVITY";
    int temptations = 0;

    TextView prompt,header;

    private SpeechRecognizer sr;

    boolean health = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = this.getWindow().getDecorView();
        Amarino.connect(getApplicationContext(), DEVICE_ADDRESS);
        view.setBackgroundColor(Color.parseColor("#551a8b"));
        Bundle extras = getIntent().getExtras();
        prompt = (TextView) findViewById(R.id.promptTxt);
        header = (TextView) findViewById(R.id.headerView);
        prompt.setTextColor(Color.WHITE);
        header.setTextColor(Color.WHITE);
        if(extras!=null){
            String secret = extras.getString("from");
            if(secret == "here"){
                health = true;
            }
        }

else {

            Log.d("STATUS", "WELCOME TO MAIN ACTIVITY");


            if (!health) {
                String guestID = ParseUser.getCurrentUser().getString("guestID");
                ParseQuery<ParseObject> gQ = ParseQuery.getQuery("Guest");
                gQ.whereEqualTo("objectId", guestID);
                try {
                    ParseObject aGuest = gQ.getFirst();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Party");
                    query.whereEqualTo("objectId", aGuest.getString("partyID"));
                    try {
                        ParseObject object = query.getFirst();
                        header.setText(object.getString("name"));
                    } catch (ParseException e) {
                        Log.e("TRY/CATCH ERROR PARTY QUERY", e.getMessage());
                        ParseUser.logOut();
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        startActivity(i);
                    }
                } catch (ParseException e) {
                    Log.e("ERROR w TRY/CATCH GUEST QUERY", e.getMessage());
                    ParseUser.logOut();
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                }

            } else {
                if (ParseUser.getCurrentUser().getInt("badChoices") > ParseUser.getCurrentUser().getInt("healthLimit")) {
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                }
            }
        }
        sr = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
        sr.setRecognitionListener(new listener());
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "voice.recognition.test");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        sr.startListening(intent);






        Amarino.connect(getApplicationContext(),DEVICE_ADDRESS);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    class listener implements RecognitionListener
    {

        public void onReadyForSpeech(Bundle params)
        {
            Log.d(TAG, "onReadyForSpeech");
            if(!health) {
                prompt.setText("Say \"I want a drink\"");
            }else{
                prompt.setText("Say, \"I want [whatever you want]\"");
            }
        }


        public void onBeginningOfSpeech()
        {
            Log.d(TAG, "onBeginningOfSpeech");
            prompt.setText("Listening...");

        }



        public void onRmsChanged(float rmsdB)
        {
            Log.d(TAG, "onRmsChanged");
        }


        public void onBufferReceived(byte[] buffer)
        {
            Log.d(TAG, "onBufferReceived");
        }


        public void onEndOfSpeech()
        {

            Log.d(TAG, "onEndofSpeech");
            /*
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something");
                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                    intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
                    sr.startListening(intent);
                    //closeCage();
                    //start();
                }
            },5000);
            */

        }


        public void onError(int error)
        {

            Log.d(TAG,  "error " +  error);
            if(error == 6 || error == 7){
                if(!health) {
                    prompt.setText("Say \"I want a drink\"");
                }else{
                    prompt.setText("Say, \"I want [whatever you want]\"");
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something");
                        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
                        sr.startListening(intent);
                    }
                },2000);
            }else if(error == 8){
                sr.cancel();
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something");
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
                sr.startListening(intent);

            }
                else
            {
                prompt.setText("Oops, something went wrong...");


            }



        }

        public void stop(){
            Amarino.sendDataToArduino(getApplicationContext(),DEVICE_ADDRESS,'x',0);
        }

        public void start(){
            Amarino.sendDataToArduino(getApplicationContext(),DEVICE_ADDRESS,'z',255);
        }

        public void openCage(){
            Amarino.sendDataToArduino(getApplicationContext(),DEVICE_ADDRESS,'w',100);

        }

        public void closeCage(){
            Amarino.sendDataToArduino(getApplicationContext(),DEVICE_ADDRESS,'x',180);
        }





        public void onResults(Bundle results)
        {
            String str = new String();
            Log.d(TAG, "onResults " + results);
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if(data.size() > 0) {
                Log.d("STATUS", "DATA RECORDED");
                String mostAccurate = data.get(0) + "";
                Log.d("word", mostAccurate);
                if (!health) {
                    boolean match = mostAccurate.matches(".*drink");
                    if (match) {

                        openCage();

                        prompt.setText("Got it! Now get your drink from the robot!");
                        Handler myHandler = new Handler();
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                closeCage();
                            }
                        },7000);
                        ParseQuery<Guest> guestQuery = ParseQuery.getQuery("Guest");
                        guestQuery.whereEqualTo("objectId", ParseUser.getCurrentUser().getString("guestID"));
                        try {
                            Guest myGuest = guestQuery.getFirst();
                            if (myGuest.getDrinksConsumed() == myGuest.getLimit()) {
                                prompt.setText("Oops! No more drinks for you!!! You've had too much");

                                myGuest.put("limitReached", true);
                                myGuest.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        closeCage();
                                        if (e == null) {
                                            Intent i = new Intent(getApplicationContext(), Login.class);
                                            startActivity(i);
                                        } else {
                                            Intent i = new Intent(getApplicationContext(), Login.class);
                                            startActivity(i);
                                        }
                                    }
                                });
                            }
                            myGuest.increment("drinksConsumed");
                            myGuest.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                                                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
                                                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                                                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "voice.recognition.test");
                                                sr.startListening(intent);


                                            }
                                        }, 5000);


                                    } else {
                                        Log.e("ERROR SAVING GUEST", e.getMessage());
                                    }
                                }
                            });
                        } catch (ParseException e) {
                            Log.e("ERROR", e.getMessage());

                        }

                    } else {



                        prompt.setText("Ummm...what did you say?");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
                                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "voice.recognition.test");
                                sr.startListening(intent);
                            }
                        }, 1000);

                    }
                }else {
                    boolean match = mostAccurate.matches(".*want+");

                    if(match) {
                        if (temptations > ParseUser.getCurrentUser().getInt("healthLimit")) {
                            prompt.setText("Oops, you've had too many! Bad!");
                            Intent i = new Intent(getApplicationContext(), Login.class);
                            startActivity(i);

                        } else{
                            openCage();
                            temptations++;
                        ParseUser.getCurrentUser().increment("badChoices");
                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                                        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
                                        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                                        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "voice.recognition.test");
                                        sr.startListening(intent);
                                    }
                                }, 1000);
                                closeCage();
                            }
                        });
                    }

                }
                    else{

                        prompt.setText("Ummm...what did you say?");
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
                                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "voice.recognition.test");
                                sr.startListening(intent);
                            }
                        }, 1000);
                    }

                }
            }
        }


        public void onPartialResults(Bundle partialResults)
        {
            Log.d(TAG, "onPartialResults");
        }


        public void onEvent(int eventType, Bundle params)
        {
            Log.d(TAG, "onEvent " + eventType);
        }
    }


    @Override
    public  void onDestroy(){
        super.onDestroy();
        sr.destroy();
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
