package com.movesense.showcaseapp.testfolder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.movesense.showcaseapp.R;

import java.util.ArrayList;

public class MPchart extends AppCompatActivity {

        private LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpchart);

        lineChart = (LineChart) findViewById(R.id.linechart);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<>();
        yValues.add(new Entry(0, 50f));
        yValues.add(new Entry(1, 30f));
        yValues.add(new Entry(2, 60f));
        yValues.add(new Entry(3, 40f));

        LineDataSet set1 = new LineDataSet(yValues, "Käppyrä 1");
        set1.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        lineChart.setData(data);

    }
}