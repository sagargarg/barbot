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
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.terahackers.pennapps.barbot.R;


public class Validation extends ActionBarActivity {

    EditText enterPID,enterFullName;
    Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        verify = (Button)findViewById(R.id.rdyToParty);
        enterPID = (EditText)findViewById(R.id.enterPartyID);
        enterFullName = (EditText)findViewById(R.id.enterName);
        View view = this.getWindow().getDecorView();

        view.setBackgroundColor(Color.parseColor("#551a8b"));
        enterPID.setHintTextColor(Color.WHITE);
        enterFullName.setHintTextColor(Color.WHITE);
        enterPID.setTextColor(Color.WHITE);
        enterFullName.setTextColor(Color.WHITE);

        if(ParseUser.getCurrentUser().getString("currentParty")!=null){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }else {

            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Party");
                    Log.d("Trying... ", enterPID.getText().toString());
                    query.whereEqualTo("code", enterPID.getText().toString());
                    try {
                        ParseObject first = query.getFirst();
                        if (first != null) {
                            ParseQuery<ParseObject> newQuery = ParseQuery.getQuery("Guest");
                            newQuery.whereEqualTo("partyID",first.getObjectId());
                            newQuery.whereEqualTo("checkedIn",false);
                            newQuery.whereEqualTo("limitReached",false);
                            newQuery.whereEqualTo("name",enterFullName.getText().toString().toLowerCase());
                            try{
                               final ParseObject trueGuest = newQuery.getFirst();
                                if(trueGuest!=null){
                                    trueGuest.put("checkedIn", true);
                                    trueGuest.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            ParseUser.getCurrentUser().put("guestID",trueGuest.getObjectId());
                                            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(i);
                                                    } else {
                                                        Log.e("ERROR", e.getMessage());


                                                    }
                                                }
                                            });
                                        }
                                    });

                                }
                                else{
                                    Log.d("ENTERED NAME -TrueGuest null",enterFullName.getText().toString().toLowerCase());
                                    Toast.makeText(getApplicationContext(),"Oops, you're not on the guest list!",Toast.LENGTH_SHORT).show();
                                }
                            }catch(ParseException e){
                                if (e.getMessage().equalsIgnoreCase("no results found for query")){
                                    Log.d("ENTERED NAME -TRY/CATCH",enterFullName.getText().toString().toLowerCase());
                                    Toast.makeText(getApplicationContext(),"Oops, you're not on the guest list!",Toast.LENGTH_SHORT).show();
                                }
                            }



                        } else {
                            Toast.makeText(getApplicationContext(), "Oops, that code was not correct, try again!", Toast.LENGTH_LONG).show();
                        }
                    } catch (ParseException e) {
                        Log.e("ERROR", e.getMessage());
                        if (e.getMessage().equalsIgnoreCase("no results found for query")) {
                            Toast.makeText(getApplicationContext(), "Oops, that code was not correct, try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_validation, menu);
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
