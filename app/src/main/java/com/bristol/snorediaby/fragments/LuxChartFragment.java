package com.bristol.snorediaby.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bristol.snorediaby.R;
import com.bristol.snorediaby.domain.LuxStorage;
import com.bristol.snorediaby.domain.beans.LuxLineChart;
import java.util.ArrayList;

public class LuxChartFragment extends Fragment {

    private static final String TAG = "LuxChartFragment";

    private View view;

    private ArrayList<LuxStorage> luxList;

    public LuxChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            Log.i(TAG, "Start LuxChartFragment onCreateView");
            view = inflater.inflate(R.layout.fragment_lux_chart, container, false);
            Context context = getActivity();

            readLuxList();
            LuxLineChart luxLineChart = new LuxLineChart(luxList, context, view);
            luxLineChart.drawLuxChart();
        } catch (Exception e) {
            Log.e(TAG, "LuxChartFragment Exception: " + e, e);
            e.printStackTrace();
        } finally {
            Log.i(TAG, "End LuxChartFragment onCreateView");
        }
        return view;
    }

    private void readLuxList() {
        luxList = new ArrayList<>();
        luxList = (ArrayList<LuxStorage>) getArguments().getSerializable("luxList");
    }

}
