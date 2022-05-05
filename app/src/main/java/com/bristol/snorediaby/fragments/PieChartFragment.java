package com.bristol.snorediaby.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bristol.snorediaby.R;
import com.bristol.snorediaby.common.constants.SdConstants;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class PieChartFragment extends Fragment {

    private static final String TAG = "PieChartFragment";

    private View view;

    private Float snore_percent;
    private final int variable = 0xFF00BFFF;
    private final int regular = 0xFFFF69B4;
    private final int[] MY_COLORS = { variable, regular };

    public PieChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            Log.i(TAG, "Start PieChartFragment onCreateView");
            view = inflater.inflate(R.layout.fragment_pie_chart, container, false);
            // Context context = getActivity();

            Bundle bundle = this.getArguments();
            snore_percent = bundle.getFloat("snore_percent");
            Log.e(TAG, "snore_percent: " + snore_percent);

            drawPie();
        } catch (Exception e) {
            Log.e(TAG, "PieChartFragment Exception: " + e, e);
            e.printStackTrace();
        } finally {
            Log.i(TAG, "End PieChartFragment onCreateView");
        }
        return view;
    }

    private void drawPie() {
        PieChart pieChart = view.findViewById(R.id.snorepie);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setRotationEnabled(false); //手動旋轉
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setTextColor(Color.WHITE);

        pieChart.setDragDecelerationFrictionCoef(0.99f);

        //圖片中間中空
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        //中空延伸區域
        //pieChart.setTransparentCircleRadius(61f);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleRadius(30f);

        //開場動畫
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCirc);

        setData(pieChart);
    }

    private void setData(PieChart pie) {
        //儲存資料
        ArrayList<PieEntry> yValues = new ArrayList<>();
        float non_snore = 100 - snore_percent;
        for (int i = 0; i < 2; i++) {
            if (Objects.equals(0, i)) {
                yValues.add(new PieEntry(snore_percent, "Snore"));
            } else {
                yValues.add(new PieEntry(non_snore, "Regular"));
            }
        }

        //set database
        PieDataSet dataSet = new PieDataSet(yValues, "");

        //區塊間隔
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueFormatter(new PieChartFragment.MyValueFormatter());

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : MY_COLORS) colors.add(c);
        dataSet.setColors(colors);

        //set display
        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);

        pie.setData(data);
    }

    class MyValueFormatter implements IValueFormatter {

        private final DecimalFormat mFormat;

        private MyValueFormatter() {
            mFormat = new DecimalFormat(SdConstants.M_FORMAT); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + " %"; // e.g. append a dollar-sign
        }
    }

}
