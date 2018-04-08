package com.example.bryanhutto.macdpro;

/**
 * Created by bryanhutto on 4/16/15.
 */
public class MyEma {
    double periods;
    float[] data = new float[50];

    public MyEma(double periods, float[] data){

        this.data= data;
        this.periods= periods;
    }

    public double[] calculateEma(){
        double total=0;

        for(int n=0;n<data.length;n++){
            total = total+data[n];
        }

        double m = 2 / ( periods + 1);
        double[] emaOut=new double[data.length];
        double ema=total/data.length;

            for(int j=0;j<data.length;j++){

                ema=m*(data[data.length-j-1])+((1-m)*ema);
                emaOut[data.length-j-1]=ema;}

        return emaOut;
    }

    }


