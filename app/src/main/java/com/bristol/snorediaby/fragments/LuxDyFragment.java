package com.bristol.snorediaby.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bristol.snorediaby.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.util.Objects;

public class LuxDyFragment extends Fragment implements OnChartValueSelectedListener {

    private static final String TAG = "LuxDyFragment";

    private View view;

    private LineChart mChart;
    private Context context;

    public LuxDyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            Log.i(TAG, "Start LuxDyFragment onCreateView");
            view = inflater.inflate(R.layout.fragment_lux_dy, container, false);
            context = getActivity();

            mChart = view.findViewById(R.id.lux_instant);
            mChart.setOnChartValueSelectedListener(this);
            mChart.setDrawGridBackground(false);
            mChart.getDescription().setEnabled(false);

            // add an empty data object
            mChart.setData(new LineData());
            //mChart.getXAxis().setDrawLabels(false);
            //mChart.getXAxis().setDrawGridLines(false);

            mChart.invalidate();
            mChart.setTouchEnabled(false); // 设置是否可以触摸
            mChart.getAxisRight().setEnabled(false);
            //mChart.setNoDataText("");
            mChart.getLegend().setTextColor(Color.WHITE);

            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.setAxisMaximum(500f);
            leftAxis.setAxisMinimum(0f);
            leftAxis.setDrawGridLines(true);
            leftAxis.setAxisLineColor(Color.WHITE);
            leftAxis.setTextColor(Color.WHITE);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setEnabled(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGridColor(Color.WHITE); //設置該軸的格線顏色
            xAxis.setAxisLineColor(Color.WHITE);
            xAxis.setDrawGridLines(false);
            xAxis.setTextColor(Color.WHITE);
        } catch (Exception e) {
            Log.e(TAG, "LuxDyFragment Exception: " + e, e);
            e.printStackTrace();
        } finally {
            Log.i(TAG, "End LuxDyFragment onCreateView");
        }
        return view;
    }

    public void addEntry(float yValue) {
        LineData data = mChart.getData();
        ILineDataSet set = data.getDataSetByIndex(0);

        if (Objects.equals(null, set)) {
            set = createSet();
            data.addDataSet(set);
        }

        // choose a random dataSet
        int randomDataSetIndex = (int) (Math.random() * data.getDataSetCount());
        //float yValue = (float) (Math.random() * 50);

        data.addEntry(new Entry(data.getDataSetByIndex(randomDataSetIndex).getEntryCount(), yValue), randomDataSetIndex);
        data.setDrawValues(true);
        data.setValueTextColor(Color.WHITE);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        mChart.notifyDataSetChanged();
        mChart.setVisibleXRangeMaximum(10);
        //mChart.setVisibleYRangeMaximum(100, AxisDependency.LEFT);
        // this automatically refreshes the chart (calls invalidate())
        mChart.moveViewTo(data.getEntryCount() - 7, 50f, YAxis.AxisDependency.LEFT);
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, context.getString(R.string.lux));
        set.setLineWidth(1f);
        set.setCircleRadius(4.5f);
        set.setColor(context.getResources().getColor(R.color.HotPink));
        //set.setCircleColor(Color.rgb(240, 99, 99));
        //set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(10f);
        set.setDrawCircles(false);
        set.setValueTextColor(Color.BLUE);

        return set;
    }

    public void removeDataSet() {
        LineData data = mChart.getData();
        if (!Objects.equals(null, data)) {
            data.removeDataSet(data.getDataSetByIndex(data.getDataSetCount() - 1));
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
        mChart.clear();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

}
