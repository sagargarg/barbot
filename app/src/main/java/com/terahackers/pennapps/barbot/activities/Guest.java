package com.terahackers.pennapps.barbot.activities;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by shreyashirday on 1/17/15.
 */
@ParseClassName("Guest")
public class Guest extends ParseObject {


    public Guest(){

    }

    public String getName(){
        return  getString("name");
    }

    public int getLimit(){
        return getInt("drinkLimit");
    }

    public String getPartyID(){
        return  getString("partyID");
    }

    public boolean isCheckedIn(){
        return  getBoolean("checkedIn");
    }

    public int getDrinksConsumed(){
        return  getInt("drinksConsumed");
    }



}
