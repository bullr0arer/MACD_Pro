package com.example.bryanhutto.macdpro;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/**
 * Created by bryanhutto on 4/19/15.
 */
public class Graph {
    public void graphMACD(Context context, double[] mACD, double[] signal,LinearLayout layout){
        double x[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50};

        TimeSeries mACDTS = new TimeSeries("MACD");
        for(int i=0;i<50;i++){
            mACDTS.add(x[50-i-1],mACD[i]);
        }
        TimeSeries signalTS = new TimeSeries("Signal");
        for(int i=0;i<50;i++){
            signalTS.add(x[50-i-1],signal[i]);
        }

        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
        dataSet.addSeries(mACDTS);
        dataSet.addSeries(signalTS);

        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        XYSeriesRenderer renderMACD = new XYSeriesRenderer();
        XYSeriesRenderer renderSignal = new XYSeriesRenderer();
        renderSignal.setColor(Color.MAGENTA);

        renderer.addSeriesRenderer(renderMACD);
        renderer.addSeriesRenderer(renderSignal);
        renderer.setShowAxes(true);


        GraphicalView mChartView = ChartFactory.getLineChartView(context, dataSet, renderer);
        layout.removeAllViews();
        layout.addView(mChartView);

    }
}
