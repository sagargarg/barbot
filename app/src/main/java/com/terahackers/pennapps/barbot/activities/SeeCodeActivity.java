package com.terahackers.pennapps.barbot.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.terahackers.pennapps.barbot.R;

public class SeeCodeActivity extends ActionBarActivity {
    Button done;
    TextView code;
    String theCode,pID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_code);
        done = (Button)findViewById(R.id.doneBtn);
        code = (TextView)findViewById(R.id.codeView);
        View view = this.getWindow().getDecorView();
        code.setTextColor(Color.WHITE);
        view.setBackgroundColor(Color.parseColor("#551a8b"));
        Bundle extras = getIntent().getExtras();

        if(extras!=null){
            theCode = extras.getString("myCode");
            pID = extras.getString("partyID");
        }
        else{
            theCode = "code";
            pID = "blah";
        }

        code.setText("The Code For Your Party Is: " + theCode);

        AlertDialog.Builder builder = new AlertDialog.Builder(SeeCodeActivity.this);
        builder.setTitle("Important!");
        builder.setMessage("Listed here is your code that you will show your guests. Guests without the code will not" +
                "be able to get drinks from the BarBot. This code will be accessible through the menu on the next screen through the menu as well");
        builder.setPositiveButton("Got It!",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //nothing
            }
        });

        builder.create().show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddGuestActivity.class);
                i.putExtra("partyID",pID);
                i.putExtra("myCode",theCode);
                startActivity(i);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_see_code, menu);
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
