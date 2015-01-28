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

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.terahackers.pennapps.barbot.R;


public class Login extends ActionBarActivity {
    Button hB,gB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        hB = (Button)findViewById(R.id.hostBtn);
        gB = (Button)findViewById(R.id.guestBtn);
        View view = this.getWindow().getDecorView();

         view.setBackgroundColor(Color.parseColor("#551a8b"));




        if(ParseUser.getCurrentUser() != null){
            ParseUser.logOut();
        }

        hB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseAnonymousUtils.logIn(new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(e!=null){
                            Log.e("ANONYMOUS LOGIN ERROR", e.getMessage());
                        }else{
                            final Intent intent = new Intent(getApplicationContext(),HostActivity.class);
                            parseUser.put("host",true);
                            parseUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    startActivity(intent);
                                }
                            });

                        }
                    }
                });

            }
        });

        gB.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ParseAnonymousUtils.logIn(new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(e!=null){
                            Log.e("ANONYMOUS LOGIN ERROR",e.getMessage());
                        }else{
                            //go to next page
                            final Intent intent = new Intent(getApplicationContext(),ChooseActivity.class);
                            parseUser.put("host",false);
                            parseUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
