package com.example.bryanhutto.macdpro;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;


public class MainActivity extends ActionBarActivity implements AsyncResponse{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void handler(View view){
        EditText t = (EditText) findViewById(R.id.ticker);
        String ticker = t.getText().toString();
        String[] tokens = ticker.split(" ");

        StringBuilder result = new StringBuilder();
        for(String token : tokens) {
            if(!token.isEmpty()) {
                result.append(token);
            }
        }

        DataGetter asyncTask =new DataGetter();
        asyncTask.delegate = this;
        asyncTask.execute(result.toString().toUpperCase());

        t.requestFocus();
    }


    public void processFinish(DataHolder dataHolder){
        float[] data = dataHolder.getData();
        String name = dataHolder.getName();

        int c=0;
        for(int l=0;l<data.length;l++){if(data[l]!=0)c++; }
        if(c>3) {
            double[] shortValues;
            double[] longValues;

            MyEma shortEma = new MyEma(12, data);
            shortValues = shortEma.calculateEma();

            MyEma longEma = new MyEma(26, data);
            longValues = longEma.calculateEma();

            double[] mACD = new double[50];
            for (int j = 0; j < mACD.length; j++) {
                mACD[j] = shortValues[j] - longValues[j];
            }

            float[] tempMACD = new float[50];
            for (int i = 0; i < mACD.length; i++) {
                tempMACD[i] = (float) mACD[i];
            }
            //^make this step unnecessary by changing DataGetter to handle double
            MyEma signalEma = new MyEma(9, tempMACD);
            double[] signalValues = signalEma.calculateEma();

            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            Graph graph = new Graph();
            graph.graphMACD(this, mACD, signalValues, layout);

            double diff = signalValues[49] - mACD[49];
            DecimalFormat df = new DecimalFormat("#.####");

            TextView diffV = (TextView) findViewById(R.id.diffView);
            diffV.setText(df.format(diff));
            if (diff >= 0) {
                diffV.setTextColor(Color.BLUE);
            } else {
                diffV.setTextColor(Color.RED);
            }
            TextView stockName = (TextView) findViewById(R.id.stockName);
            stockName.setText(name);
        }
        else{
            TextView stockName = (TextView) findViewById(R.id.stockName);
            stockName.setText("data currently unavailable for this stock");
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            layout.removeAllViews();
            TextView diffV = (TextView) findViewById(R.id.diffView);
            diffV.setText("n/a");
            diffV.setTextColor(Color.BLACK);
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
