package com.example.bryanhutto.macdpro;

/**
 * Created by bryanhutto on 4/26/15.
 */
public class DataHolder {
    float[] data = new float[50];
    String name = "";
     public DataHolder(float[] f, String s){
         data=f;
         name=s;
     }
    public float[] getData(){
        return data;
    }
    public String getName(){
        return name;
    }
}
