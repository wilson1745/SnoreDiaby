package com.bristol.snorediaby.repo.domain.beans;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import com.bristol.snorediaby.R;
import com.bristol.snorediaby.repo.domain.LuxStorage;
import com.bristol.snorediaby.logic.modules.MyMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.util.ArrayList;

public class LuxLineChart {

    private final ArrayList<LuxStorage> luxList;
    private final Context context;
    private final View view;

    public LuxLineChart(ArrayList<LuxStorage> list, Context context, View view) {
        this.luxList = list;
        this.context = context;
        this.view = view;
    }

    public void drawLuxChart() {
        LineChart lineChart = view.findViewById(R.id.linechart);
        //lineChart.setOnChartGestureListener(this);
        //lineChart.setOnChartValueSelectedListener(this);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.animateY(2000);
        lineChart.getLegend().setTextColor(Color.WHITE);
        lineChart.getAxisRight().setEnabled(false);

        MyMarkerView mv = new MyMarkerView(context, R.layout.custom_marker_view);
        mv.setChartView(lineChart); // For bounds control
        lineChart.setMarker(mv); // Set the marker to the chart

        //limit line
        LimitLine lower_line = new LimitLine(5f, context.getString(R.string.limit_lux));
        lower_line.setLineWidth(1f);
        lower_line.enableDashedLine(10f, 10, 0f);
        lower_line.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        lower_line.setLineColor(Color.CYAN);
        lower_line.setTextColor(Color.CYAN);
        lower_line.setTextSize(10f);

        //上面左圖具有描述設置，默認有描述
        Description description = lineChart.getDescription();
        description.setEnabled(false);

        //取得Y軸最大值
        int maxNum = (int) luxList.get(0).getLux();
        for (int i = 1; i < luxList.size(); i++) {
            if (maxNum < luxList.get(i).getLux()) {
                maxNum = (int) luxList.get(i).getLux();
            }
        }
        do {
            maxNum++;
        } while ((maxNum % 10) != 0);

        //設置Y軸
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(lower_line);
        //leftAxis.setAxisMaximum(Float.MAX_VALUE);
        leftAxis.setAxisMaximum(maxNum);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0);
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setTextColor(Color.WHITE);

        //set Y data
        ArrayList<Entry> yValues = new ArrayList<>();
        for (int i = 0; i < luxList.size(); i++) {
            yValues.add(new Entry(i, luxList.get(i).getLux()));
        }

        //put into line data database
        LineDataSet set1 = new LineDataSet(yValues, context.getString(R.string.lux));
        set1.setFillAlpha(110);

        //set text, colors
        set1.setColor(context.getResources().getColor(R.color.HotPink));
        set1.setLineWidth(1f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.BLUE);
        set1.setDrawValues(false);
        set1.setDrawCircles(false);

        ArrayList<ILineDataSet> datasets = new ArrayList<>();
        datasets.add(set1);

        LineData data = new LineData(datasets);
        lineChart.setData(data);

        //set X data
        String[] values = new String[luxList.size()];
        for (int i = 0; i < luxList.size(); i++) {
            values[i] = luxList.get(i).getDate();
            values[i] = values[i].substring(11, 16);
            //Log.e(TAG, "Time:" + values[i]);
        }

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new LuxLineChart.MyXAxisValueFormatter(values));
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGridColor(Color.WHITE); //設置該軸的格線顏色
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
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