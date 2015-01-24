package com.terahackers.pennapps.barbot.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.terahackers.pennapps.barbot.R;

public class HealthActivity extends ActionBarActivity {

    EditText enterHealthLimit;
    Button nxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        enterHealthLimit = (EditText)findViewById(R.id.healthLimit);
        enterHealthLimit.setHintTextColor(Color.WHITE);
        enterHealthLimit.setTextColor(Color.WHITE);
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.parseColor("#551a8b"));
        nxt = (Button)findViewById(R.id.nBtn);
        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.getCurrentUser().put("healthLimit",Integer.parseInt(enterHealthLimit.getText().toString()));
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        i.putExtra("from","here");
                        startActivity(i);
                    }
                });
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_health, menu);
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
