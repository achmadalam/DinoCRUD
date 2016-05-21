package com.aldoapps.firebasedemoone;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aldokelvianto on 21/05/16.
 */

@IgnoreExtraProperties
public class Dinosaur {
    String name;
    double height;
    double length;
    long weight;
    int leg;

    public Dinosaur(){}

    public Dinosaur(String name, double height, double length, long weight, int leg) {
        this.name = name;
        this.height = height;
        this.length = length;
        this.weight = weight;
        this.leg = leg;
    }

    // without exclude this will be saved on Firebase database
    @Exclude
    public Map<String, Object> getMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("height", height);
        result.put("length", length);
        result.put("weight", weight);
        result.put("leg", leg);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public int getLeg() {
        return leg;
    }

    public void setLeg(int leg) {
        this.leg = leg;
    }
}
