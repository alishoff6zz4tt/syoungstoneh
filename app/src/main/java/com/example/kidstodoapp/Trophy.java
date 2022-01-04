package com.example.kidstodoapp;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.lang.Object;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Trophy implements Serializable {

    private String name;
    private String description;
    private int pointValue;
    private boolean redeemed;
    //BufferedImage image;

    Trophy(String name, String description, int pointValue) {
        this.name = name;
        this.description = description;
        this.pointValue = pointValue;
        //this.image = ImageIO.read(new File(name + ".png"));
    /*    try {
            img = ImageIO.read(new File("logo.png"));
        } catch (IOException e) {
        }
        redeemed = false;*/
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return pointValue;
    }

    public void setPoints(int pointValue) {
        this.pointValue = pointValue;
    }

    public boolean isCompleted() {
        return redeemed;
    }

    public void setCompleted(boolean redeemed) {
        this.redeemed = redeemed;
    }


}
