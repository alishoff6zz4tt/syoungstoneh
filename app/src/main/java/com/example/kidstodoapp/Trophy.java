package com.example.kidstodoapp;

import java.io.Serializable;
import java.util.HashMap;

//If you need to change the variable type within Trophy, make change in firebase too
public class Trophy implements Serializable {

  private String name;
  private long pointValue;
  private long imageLocation;

  Trophy(String name, long pointValue, long imageLocation) {
    this.name = name;
    this.pointValue = pointValue;
    this.imageLocation = imageLocation;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getPointValue() {
    return pointValue;
  }

  public long getImageLocation() {
    return imageLocation;
  }

  public static Trophy buildTrophy(HashMap<String, Object> map) {
    return new Trophy(
        (String) map.get("name"),
        (long) map.get("pointValue"),
        (long) map.get("imageLocation")
    );
  }
}
