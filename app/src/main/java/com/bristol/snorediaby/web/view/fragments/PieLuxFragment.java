package com.bristol.snorediaby.web.view.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bristol.snorediaby.R;
import com.bristol.snorediaby.common.constants.SdConstants;
import com.bristol.snorediaby.common.exceptions.SnoreException;
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

public class PieLuxFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private View view;

    private Float lux_percent;
    private final int variable = 0xFFF57C00;
    private final int regular = 0xFFFF69B4;
    private final int[] MY_COLORS = { variable, regular };
    private Context context;

    public PieLuxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "Start PieLuxFragment onCreateView");
        try {
            view = inflater.inflate(R.layout.fragment_pie_lux, container, false);
            context = getActivity();

            Bundle bundle = this.getArguments();
            lux_percent = bundle.getFloat("lux_percent");
            Log.e(TAG, "lux_percent: " + lux_percent);

            drawPie();
        } catch (Exception e) {
            SnoreException.getErrorException(TAG, e);
        } finally {
            Log.i(TAG, "End PieLuxFragment onCreateView");
        }
        return view;
    }

    private void drawPie() {
        PieChart pieChart = view.findViewById(R.id.luxpie);

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
        float non_snore = 100 - lux_percent;
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                yValues.add(new PieEntry(lux_percent, context.getString(R.string.light)));
            } else {
                yValues.add(new PieEntry(non_snore, context.getString(R.string.normal)));
            }
        }

        //set database
        PieDataSet dataSet = new PieDataSet(yValues, "");

        //區塊間隔
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueFormatter(new MyValueFormatter());

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : MY_COLORS) colors.add(c);
        dataSet.setColors(colors);

        //dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
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
