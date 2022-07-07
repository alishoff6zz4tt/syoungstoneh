package com.example.kidstodoapp;

import java.io.Serializable;
import java.util.HashMap;
/*
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
 */


//If you need to change the variable type within Trophy, make change in firebase too
public class Trophy implements Serializable {

    private String name;
    //private String description;
    private long pointValue;
    private boolean redeemed;
    private long imageLocation;
    //private BufferedImage image;

    Trophy(String name, long pointValue, boolean redeemed, long imageLocation) { //, String imageLocation
        this.name = name;
        //this.description = description;
        this.pointValue = pointValue;
        this.redeemed = false;
        this.imageLocation = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPoints() {
        return pointValue;
    }

    public void setPoints(long pointValue) {
        this.pointValue = pointValue;
    }

    public long getImage() {
        return imageLocation;
    }

    public void setImage(int imageLocation) {
        this.imageLocation = imageLocation;
    }

    public boolean isRedeemed() {
        return redeemed;
    }
    public void setRedeemed(boolean redeemed) {
        this.redeemed = redeemed;
    }

    public static Trophy buildTrophy(HashMap<String,Object> map) {
        return new Trophy(
                (String)map.get("name"),
                //(String)map.get("description"),
                (long)map.get("pointValue"),
                (boolean)map.get("redeemed"),
                (long)map.get("imageLocation")
        );
    }
}
