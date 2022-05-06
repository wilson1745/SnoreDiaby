package com.bristol.snorediaby.repo.domain.beans;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import com.bristol.snorediaby.R;
import com.bristol.snorediaby.repo.domain.SnoreStorage;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import java.util.ArrayList;

public class SnoreBarChart {

    private final ArrayList<SnoreStorage> snoreList;
    private final Context context;
    private final View view;

    public SnoreBarChart(ArrayList<SnoreStorage> snoreList, Context context, View view) {
        this.snoreList = snoreList;
        this.context = context;
        this.view = view;
    }

    public void drawSnoreChart() {
        BarChart barChart = view.findViewById(R.id.barchart);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false); //網格
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.animateY(3000, Easing.EasingOption.Linear);
        barChart.animateX(3000, Easing.EasingOption.Linear);
        barChart.setDrawBorders(false); //顯示邊界
        barChart.getLegend().setTextColor(Color.WHITE);

        //取得Y軸最大值
        int maxNum = snoreList.get(0).getSnore();
        for (int i = 1; i < snoreList.size(); i++) {
            if (maxNum < snoreList.get(i).getSnore()) {
                maxNum = snoreList.get(i).getSnore();
            }
        }
        do {
            maxNum++;
        } while ((maxNum % 10) != 0);

        //上面左圖具有描述設置，默認有描述
        Description description = barChart.getDescription();
        description.setEnabled(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum(maxNum);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0);
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setTextColor(Color.WHITE);

        barChart.getAxisRight().setEnabled(false);

        //set data
        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 0; i < snoreList.size(); i++) {
            yValues.add(new BarEntry(i, snoreList.get(i).getSnore()));
        }

        BarDataSet set1 = new BarDataSet(yValues, context.getString(R.string.bar_thr));
        set1.setColor(context.getResources().getColor(R.color.HotPink));

        //'com.github.PhilJay:MPAndroidChart:v3.0.1' 新版本會掛掉0.0
        BarData data = new BarData(set1);

        //space between two bars
        //      float groupSpace = 0.25f;
        //      float barSpace = 0.02f;
        float barWidth = 0.3f;

        barChart.setData(data);
        data.setBarWidth(0.9f);
        data.setBarWidth(barWidth);
        data.setValueTextColor(Color.WHITE);

        XAxis xAxis = barChart.getXAxis();
        String[] values = new String[snoreList.size()];
        for (int i = 0; i < snoreList.size(); i++) {
            values[i] = snoreList.get(i).getDate();
            values[i] = values[i].substring(11, 16);
        }

        xAxis.setValueFormatter(new SnoreBarChart.MyXAxisValueFormatter(values));
        xAxis.setLabelCount(values.length, false);

        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGridColor(Color.WHITE); //設置該軸的格線顏色
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setTextColor(Color.WHITE);
        //xAxis.setCenterAxisLabels(true);
        //xAxis.setAxisMinimum(1);
    }

    private class MyXAxisValueFormatter implements IAxisValueFormatter {
        private final String[] values;

        private MyXAxisValueFormatter(String[] values) {
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return values[(int) value];
        }
    }

}