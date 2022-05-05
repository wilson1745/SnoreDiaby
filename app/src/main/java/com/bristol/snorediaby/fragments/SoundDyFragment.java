package com.bristol.snorediaby.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SoundDyFragment extends Fragment implements OnChartValueSelectedListener {

    private final String TAG = this.getClass().getSimpleName();

    private View view;

    private LineChart mChart;

    private Context context;

    public SoundDyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            Log.i(TAG, "Start SoundDyFragment onCreateView");
            view = inflater.inflate(R.layout.fragment_sound_dy, container, false);
            context = getActivity();

            mChart = view.findViewById(R.id.sound_instant);
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
            //mChart.getLegend().setTextSize(1f);

            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.setAxisMaximum(100f);
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
            Log.e(TAG, "SoundDyFragment Exception: " + e, e);
            e.printStackTrace();
        } finally {
            Log.i(TAG, "End SoundDyFragment onCreateView");
        }
        return view;
    }

    public void addEntry(int yValue) {
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
        data.setDrawValues(false);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        mChart.notifyDataSetChanged();
        mChart.setVisibleXRangeMaximum(10);
        //mChart.setVisibleYRangeMaximum(100, AxisDependency.LEFT);
        // this automatically refreshes the chart (calls invalidate())
        mChart.moveViewTo(data.getEntryCount() - 7, 50f, YAxis.AxisDependency.LEFT);
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, context.getString(R.string.db));
        set.setLineWidth(1f);
        set.setCircleRadius(4.5f);
        set.setColor(Color.CYAN);
        //set.setCircleColor(Color.rgb(240, 99, 99));
        //set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(10f);
        set.setDrawCircles(false);
        set.setValueTextColor(Color.BLUE);

        set.setDrawFilled(true);
        set.setFormLineWidth(1f);
        set.setFormSize(15.f);
        //设置曲线展示为圆滑曲线（如果不设置则默认折线）
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        return set;
    }

    //   public void removeDataSet() {
    //      LineData data = mChart.getData();
    //      if (data != null) {
    //         data.removeDataSet(data.getDataSetByIndex(data.getDataSetCount() - 1));
    //         mChart.notifyDataSetChanged();
    //         mChart.invalidate();
    //      }
    //      mChart.clear();
    //   }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

}
