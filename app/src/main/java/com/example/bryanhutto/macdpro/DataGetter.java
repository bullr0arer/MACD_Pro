package com.example.bryanhutto.macdpro;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DataGetter extends AsyncTask<String, Void, DataHolder> {

    public AsyncResponse delegate=null;

     @Override
     protected DataHolder doInBackground(String... args){
         float[] dataArray = new float[50];
         String name = " " ;

         /*final Calendar cal = Calendar.getInstance();
         int dd = cal.get(Calendar.DAY_OF_MONTH);
         int mm = cal.get(Calendar.MONTH);
         int yy = cal.get(Calendar.YEAR);*/


        //String url = "http://ichart.yahoo.com/table.csv?s=BAS.DE&a=0&b=1&c=2000 &d=0&e=31&f=2010&g=w&ignore=.csv";

         //get 50 day history of closing prices

         String url = "https://www.quandl.com/api/v1/datasets/WIKI/"+args[0]+".csv?column=4&rows=50&auth_token=PxLvP7zEWxUSy7svyAKQ";
         String charset = "UTF-8";


        URLConnection connection;
        InputStream response;
        String line;
        try {
            connection = new URL(url).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            response = connection.getInputStream();

            InputStreamReader r = new InputStreamReader(response);
            BufferedReader reader = new BufferedReader(r);

            int c = 0;
            String[] data = new String[50];

            for (int q = 0; q < 50; q++) {
                while ((line = reader.readLine()) != null) {
                    if (c != 0) {
                        String[] dataTemp = line.split(",");
                        data[c - 1] = dataTemp[1];
                        c++;
                    } else {
                        c++;
                    }
                }
            }
            for (int j = 0; j < 50; j++) {
                dataArray[j] = Float.parseFloat(data[j]);
            }

            //get today's current price (if after close, will simply replace dataArray[49] with a float of the same value)

            URLConnection connection2 = new URL("http://download.finance.yahoo.com/d/quotes.csv?s=%40%5EDJI," + args[0] + "&f=nsl1op&e=.csv").openConnection();
            connection2.setRequestProperty("Accept-Charset", charset);
            response = connection2.getInputStream();

            InputStreamReader iR = new InputStreamReader(response);
            BufferedReader bR = new BufferedReader(iR);
            float data2 = 0;

            c = 0;
            while ((line = bR.readLine()) != null) {
                if (c == 1) {
                    String[] datatemp = line.split(",");
                    name = datatemp[0];
                    try {
                        data2 = Float.parseFloat(datatemp[2]);
                    } catch (NumberFormatException e) {
                        try{
                        data2 = Float.parseFloat(datatemp[3]);}
                        catch(NumberFormatException eN){
                            try{
                            data2=Float.parseFloat(datatemp[4]);}
                            catch(NumberFormatException enN){
                                data2=Float.parseFloat(datatemp[5]);
                            }
                        }
                    }
                } else {
                    c++;
                }
            }
            if (data2 != 0) {
                dataArray[49] = data2;
            }
        }
        catch(FileNotFoundException e){
            for(int y = 0; y<dataArray.length; y++){
            dataArray[y]=0;
            }
        }
        catch (MalformedURLException ex) {
            Log.e("API_TESTING", "issues", ex);
        } catch (IOException ex) {
            Log.e("API_TESTING", "different issues", ex);
        }
        DataHolder dataHolder=new DataHolder(dataArray,name);
         return dataHolder;
    }
    protected void onPostExecute(DataHolder result) {
        delegate.processFinish(result);
    }
}
