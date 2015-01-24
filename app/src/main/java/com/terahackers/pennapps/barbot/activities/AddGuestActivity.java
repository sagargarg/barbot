package com.terahackers.pennapps.barbot.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.terahackers.pennapps.barbot.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class AddGuestActivity extends ActionBarActivity {

    Button add,done;
    ListView lv;
    String id = "test";
    ArrayList<Guest> info = new ArrayList<Guest>();
    MyAdapter adapter;
    int click;
    String theCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guest);
        click = 0;

        add = (Button)findViewById(R.id.addBtn);
       done = (Button)findViewById(R.id.doneBtn);
        lv = (ListView)findViewById(R.id.myList);
        if(ParseUser.getCurrentUser() == null){
            Log.d("USER STATUS","NULL");
            Intent i = new Intent(AddGuestActivity.this, Login.class);
            startActivity(i);
        }else {
            Log.d("USER STATUS", "LOGGED IN" + ParseUser.getCurrentUser().getUsername());

            if (ParseUser.getCurrentUser().getBoolean("host") == false) {
                Log.d("HOST?","NO");
                Intent i = new Intent(AddGuestActivity.this, MainActivity.class);
                Log.d("STATUS","BEFORE START ACTIVITY");
                startActivity(i);
                Log.d("STATUS","AFTER START ACTIVITY");
            } else {
                Log.d("HOST?","YES");
                Bundle extras = getIntent().getExtras();


                if (extras != null) {
                    id = extras.getString("partyID");
                    theCode = extras.getString("myCode");
                    Log.d("ID", id);
                } else {
                    Log.d("ID", "is null");
                    id = ParseUser.getCurrentUser().getString("currentParty");
                }


                ParseQuery<Guest> initialQuery = ParseQuery.getQuery("Guest");

                initialQuery.whereEqualTo("partyID", id);
                initialQuery.addDescendingOrder("createdAt");
                initialQuery.findInBackground(new FindCallback<Guest>() {
                    @Override
                    public void done(List<Guest> parseObjects, ParseException e) {
                        Log.d("initial query length",parseObjects.size() + "");
                        for (Guest parseObject : parseObjects) {

                            info.add(parseObject);
                        }
                        adapter = new MyAdapter(getApplicationContext(), info);
                        lv.setAdapter(adapter);
                        final Handler handler = new Handler();
                        Timer timer = new Timer();
                        TimerTask reloadIt = new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("STATUS","REFRESHING");
                                        refreshAdapter(false);
                                    }
                                });
                            }
                        };
                        timer.schedule(reloadIt,0,5000);
                    }
                });


                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("ACTION", "ADD CLICKED");
                        addGuest(id);
                    }
                });

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("ACTION", "DONE CLICKED");
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Party");
                        query.getInBackground(id, new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject parseObject, ParseException e) {
                                if(e==null) {
                                    parseObject.deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {

                                                ParseUser.logOut();
                                                Intent i = new Intent(getApplicationContext(), Login.class);
                                                startActivity(i);
                                            } else {
                                                Log.e("ERROR", e.getMessage());
                                            }
                                        }
                                    });
                                }else{
                                    ParseUser.logOut();
                                    Intent i = new Intent(getApplicationContext(), Login.class);
                                    startActivity(i);
                                }
                            }
                        });

                    }
                });




            }
        }





    }


    public void refreshAdapter(final boolean afterAdd){
        ParseQuery<Guest> query = ParseQuery.getQuery("Guest");

        query.whereEqualTo("partyID",id);
        query.addDescendingOrder("createdAt");
        if(afterAdd){
            query.fromLocalDatastore();
        }
        query.findInBackground(new FindCallback<Guest>() {
            @Override
            public void done(List<Guest> parseObjects, ParseException e) {
                if (e == null) {
                    Log.d("STATUS FINDING","SUCCESS " + parseObjects.size());




                        if(afterAdd){
                            Log.d("CLICK",click + "");
                            adapter.insert(parseObjects.get(parseObjects.size()-1),0);


                        }else {
                            adapter.clear();
                            adapter.addAll(parseObjects);
                        }
                        adapter.notifyDataSetChanged();
                        Log.d("LENGTH OF INFO", info.size() + "");



                }
                else{
                    Log.e("STATUS FINDING", "ERROR: " + e.getMessage());
                }
            }
        });
    }


    public void refresh(){
        finish();
        startActivity(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_guest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int idx = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (idx == R.id.action_settings) {
            return true;
        }else if(idx == R.id.action_seecode){
            Toast.makeText(getApplicationContext(),"Your Code Is "+ theCode,Toast.LENGTH_LONG).show();
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void addGuest(final String pid){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddGuestActivity.this);
        builder.setTitle("Enter a Guest Name and Drink Limit");
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText guestName = new EditText(AddGuestActivity.this);
        final EditText drinkLimit = new EditText(AddGuestActivity.this);
        guestName.setInputType(InputType.TYPE_CLASS_TEXT);
        drinkLimit.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(guestName);
        layout.addView(drinkLimit);
        builder.setView(layout);
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String gN = guestName.getText().toString();
                int dL = Integer.parseInt(drinkLimit.getText().toString());

               final Guest guest = new Guest();
                guest.put("name",gN.toLowerCase());
                guest.put("drinkLimit",dL);
                guest.put("partyID",pid);
                guest.put("checkedIn",false);
                guest.pinInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            guest.saveEventually();
                            Toast.makeText(getApplicationContext(), "Guest Added", Toast.LENGTH_SHORT).show();
                            refreshAdapter(true);

                        } else {
                            Log.e("ERROR WITH PINNING GUEST", e.getMessage());
                        }
                    }
                });



            }
        });

        builder.create().show();
    }

    private class MyAdapter extends  ArrayAdapter<Guest>{
        private  final Context context;
         private final ArrayList<Guest> strings;

        public MyAdapter(Context context, ArrayList<Guest> strings){
            super(context,R.layout.activity_add_guest,strings);
            this.context = context;
            this.strings = strings;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.adapter_item, parent, false);
            TextView txtview = (TextView)rowView.findViewById(R.id.text);
            txtview.setTextColor(Color.BLACK);
            Guest guest = strings.get(position);
            if(guest.isCheckedIn()){
                rowView.setBackgroundColor(Color.GREEN);
            }
            if(guest.getDrinksConsumed() > guest.getLimit()){
                rowView.setBackgroundColor(Color.RED);
            }
            String finalString = guest.getName() + ", " + guest.getLimit();
            txtview.setText(finalString);
            txtview.setTextSize(25);

            return  rowView;
        }

    }

}
