package com.movesense.showcaseapp.testfolder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.movesense.showcaseapp.R;
import com.movesense.showcaseapp.google_drive.SendLogsToGoogleDriveActivity;
import com.movesense.showcaseapp.section_00_mainView.MainViewActivity;
import com.movesense.showcaseapp.section_08_info.InfoActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MPchart extends AppCompatActivity {

        private LineChart angularChartX, angularChartY, angularChartZ;
        private HorizontalBarChart horizontalBarChart;
        ArrayList angularArrayList1, angularArrayList2, angularArrayList3;
        public TextView textCounter, textAHI, textTime, textResult, textNotMedical;
        public ImageView backButton;
    int count = 0;
    float lastMilSecValue = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpchart);

        Intent receiverIntent = getIntent();
        File csvFile = (File)receiverIntent.getSerializableExtra("KEY_FILE");

        Log.d("Tuleva tiedosto","" + csvFile);

        try {
            readLinearData(csvFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        backButton = (ImageView) findViewById(R.id.results_back_button);
        backButton.setOnClickListener(this::onClick);

        textCounter = (TextView) findViewById(R.id.textViewCounter);
        textAHI = (TextView) findViewById(R.id.textViewAHI);
        textTime = (TextView) findViewById(R.id.textViewTime);
        textResult = (TextView) findViewById(R.id.textViewResult);
        textNotMedical = (TextView) findViewById(R.id.textViewNotMedical);

        angularChartY = (LineChart) findViewById(R.id.angularcharty);
        angularChartY.setDragEnabled(true);
        angularChartY.setScaleEnabled(true);

        ArrayList<ILineDataSet> angularDataSetsY = new ArrayList<>();

        LineDataSet angularset2 = new LineDataSet(angularArrayList2, "Kulmanopeus-anturin Y-akseli");
        angularset2.setColor(Color.BLUE);
        angularset2.setLineWidth(2f);
        angularset2.setDrawCircles(false);

        angularDataSetsY.add(angularset2);

        LineData angulardatay = new LineData(angularDataSetsY);
        angularChartY.getDescription().setText("");
        angularChartY.setData(angulardatay);

        textCounter.setText("Matalia hengitysjaksoja havaittu " + count + " kertaa.");

        //setBarData();
        setResultText(count, lastMilSecValue);
    }

    //read csv file, parse data, count low breathing
    private void readLinearData(File path) throws FileNotFoundException {
        angularArrayList2 = new ArrayList<>();
        ArrayList<String> millisecods =  new ArrayList<>();
        ArrayList<Integer> sleepSections = new ArrayList<>();

        InputStream is = new FileInputStream(path);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line = "";
        int number = 1;
        int currentCount = 0; //current line count
        int lowerCount = 0; //counting lines where values are under border values
        int sectionTotal = 0;
        float xValue, yValue, zValue;
        float measureValue = 0;
        float startingValue = 30000; //taking of 30 sec from beginning
        float timerange = 0;
        float endValue = 0;

        try {
            reader.readLine();
            while (((line = reader.readLine()) != null)) {
                //Log.d("MyActivity", "Rivi:" + line);
                String[] tokens = line.split(",");
                    //not drawing lines above 2 and below -2
                    yValue = Float.parseFloat(tokens[3] + '.' + tokens[4]);
                    if(yValue >= 2){
                        angularArrayList2.add(new BarEntry(number, 2));
                    }if(yValue <= -2){
                        angularArrayList2.add(new BarEntry(number, -2));
                    }if(yValue < 2 && yValue > -2){
                        angularArrayList2.add(new BarEntry(number, Float.parseFloat(tokens[3] + '.' + tokens[4])));
                    }
                    millisecods.add(tokens[0]);
                    number++;
                    currentCount++;
                    //measure values
                    measureValue = Float.parseFloat(tokens[3] + '.' + tokens[4]);
                    if(measureValue >= 0.4 || measureValue <= -0.4) {
                        endValue = Float.parseFloat(tokens[0]);
                        timerange = endValue - startingValue;

                        sectionTotal += currentCount;


                        //count if timerange over 10 sec
                        if(timerange >= 10000){
                            Log.d("Hyväksytty pituus", "erotus: " + timerange);
                            count++;
                            sleepSections.add(sectionTotal-currentCount);
                            sleepSections.add(currentCount);
                            sectionTotal = 0;
                        }
                        currentCount = 0;
                        startingValue = endValue;
                    }

            }
            sleepSections.add(sectionTotal);

            int lastIndex = millisecods.size();
            int last = Integer.parseInt(millisecods.get(millisecods.size()-1));
            lastMilSecValue = Integer.parseInt(millisecods.get(millisecods.size()-1));
            //Log.d("Viimeinen millisekuntti", "index " + lastIndex + " arvo: " + last);
            //Log.d("pätkä yhteensä", "rivisumma " + sectionTotal);
            //Log.d("sleep array", "lukuja " + sleepSections);

        } catch (IOException e) {
            Log.wtf("MyActivity", "Error reading csv-file" + line, e);
            throw new RuntimeException(e);
        }
            //bar chart
            horizontalBarChart = (HorizontalBarChart) findViewById(R.id.barchart);
            horizontalBarChart.setDragEnabled(true);
            horizontalBarChart.setScaleEnabled(true);
            horizontalBarChart.setDragEnabled(true);
            horizontalBarChart.getXAxis().setDrawAxisLine(false);
            horizontalBarChart.getAxisLeft().setDrawAxisLine(false);
            horizontalBarChart.setDrawGridBackground(false);
            horizontalBarChart.getXAxis().setDrawGridLines(false);
            horizontalBarChart.getAxisLeft().setDrawGridLines(false);
            horizontalBarChart.getAxisRight().setDrawGridLines(false);
            horizontalBarChart.getAxisLeft().setDrawLabels(false);
            horizontalBarChart.getAxisRight().setDrawLabels(false);
            horizontalBarChart.getXAxis().setDrawLabels(false);
            horizontalBarChart.getDescription().setText("");

            ArrayList<BarEntry> bValues = new ArrayList<>();

            //draw sections from sleepSections arrayList
            float[] sectionList = new float[sleepSections.size()];
            for(int i = 0; i < sleepSections.size(); i++){
            sectionList[i] = sleepSections.get(i);
            }

            //bValues.add(new BarEntry(0, new float[]{10, 5, 30, 2, 10, 5, 10}));
            bValues.add(new BarEntry(0, sectionList));
            BarDataSet set2 = new BarDataSet(bValues, "");

            //new colors
            int myBlue = Color.rgb(30, 144, 255);
            int myYellow = Color.rgb(240, 230, 100);

            int[] colors = new int[]{myYellow, myBlue};
            if(sleepSections.size() <= 1){
                set2.setLabel("Normaali hengitysliike");
            }
            set2.setStackLabels(new String[]{"Normaali hengitysliike", "Matala hengitysliike"});
            set2.setColors(colors);
            set2.setDrawValues(false);

            BarData bardata = new BarData(set2);

            horizontalBarChart.setData(bardata);
    }

    private void setResultText(int count, float lastMilSecValue) {
        float hours = 0;
        if(lastMilSecValue < 3600000){
            hours = lastMilSecValue;
        }
        if(lastMilSecValue >= 3600000){
            hours = lastMilSecValue/3600000;
        }
        //convert hours and minutes from last time value
        int resultHours = (int) ((lastMilSecValue / (1000*60*60)) % 24);
        int resultMinutes = (int) ((lastMilSecValue / (1000*60)) % 60);
        String resultTime = String.format("%02d.%02d", resultHours, resultMinutes);

        //result/hour
        float resultValue = count/hours;
        DecimalFormat df = new DecimalFormat("0.0");
        String average = df.format(resultValue);
        //Log.d("Keskiarvo", "df: " + average);
        //Log.d("AHI keskiarvo", "Kesto " + hours + " Määrä " + count + " Tulos: " + resultValue);

        textAHI.setText(" AHI keskiarvo: " + average + "/tunnissa.");
        textTime.setText("Mittausjakson kesto " + resultTime + ".");

        if(resultValue <= 4.9){
            textResult.setText("Ei aihetta epäillä sentraalista uniapneaa.");
            textNotMedical.setText("(Tulos on viitteellinen.)");
        }
        if(resultValue >= 5.0 && resultValue <= 14.9){
            textResult.setText("On aihetta epäillä lievää sentraalista uniapneaa.");
            textNotMedical.setText("(Tulos on viitteellinen.)");
        }
        if(resultValue >= 15.0 && resultValue <= 29.9){
            textResult.setText("On aihetta epäillä keskivaikeaa sentraalista uniapneaa.");
            textNotMedical.setText("(Tulos on viitteellinen.)");
        }
        if(resultValue >= 30.0){
            textResult.setText("On aihetta epäillä vaikeaa sentraalista uniapneaa.");
            textNotMedical.setText("(Tulos on viitteellinen.)");
        }

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.results_back_button:
                startActivity(new Intent(MPchart.this, SendLogsToGoogleDriveActivity.class));
                break;
        }
    }
}