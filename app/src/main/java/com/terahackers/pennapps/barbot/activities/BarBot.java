package com.terahackers.pennapps.barbot.activities;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by shreyashirday on 1/17/15.
 */
public class BarBot extends Application {

    public void onCreate(){
        super.onCreate();

        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(Guest.class);
        Parse.initialize(this, "2sW7VgNBTiXB0TTz30SG7xLEGnyqkKy3CY0aWqI7", "gsJoRyKKMaLthmefYWxoA2bQRXz7oQ9G89zgEvu7");


    }

}
