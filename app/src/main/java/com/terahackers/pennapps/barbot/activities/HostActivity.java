package com.terahackers.pennapps.barbot.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.terahackers.pennapps.barbot.R;

import java.math.BigInteger;
import java.security.SecureRandom;


public class HostActivity extends ActionBarActivity {
    Button next;
    EditText partyName;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        View view = this.getWindow().getDecorView();

        view.setBackgroundColor(Color.parseColor("#551a8b"));
        next = (Button)findViewById(R.id.nextBtn);
        partyName = (EditText)findViewById(R.id.partyName);
        partyName.setHintTextColor(Color.WHITE);
        partyName.setTextColor(Color.WHITE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ParseObject party = new ParseObject("Party");
                party.put("name",partyName.getText().toString());
                code = createCode();
                party.put("code",code);
                party.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            final Intent i = new Intent(getApplicationContext(),SeeCodeActivity.class);
                            i.putExtra("partyID",party.getObjectId());
                            i.putExtra("myCode",code);
                            ParseUser.getCurrentUser().put("currentParty", party.getObjectId());
                            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    startActivity(i);
                                }
                            });

                        }else{
                                Log.e("ERROR SAVING OBJECT", e.getMessage());
                        }
                    }
                });

            }
        });
    }

    public  String createCode(){

        SecureRandom random = new SecureRandom();

        String longCode =  new BigInteger(130,random).toString(32);
        String shortCode = longCode.substring(0,7);

        return  shortCode;


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_host, menu);
        return true;
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
