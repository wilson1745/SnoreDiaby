package com.bristol.snorediaby.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bristol.snorediaby.R;
import com.bristol.snorediaby.domain.SnoreStorage;
import com.bristol.snorediaby.domain.beans.SnoreBarChart;
import java.util.ArrayList;

public class SnoreChartFragment extends Fragment {

    private static final String TAG = "SnoreChartFragment";

    private View view;

    private ArrayList<SnoreStorage> snoreList;

    public SnoreChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            Log.i(TAG, "Start SnoreChartFragment onCreateView");
            view = inflater.inflate(R.layout.fragment_snore_chart, container, false);
            Context context = getActivity();

            readSnoreList();
            SnoreBarChart snoreBarChart = new SnoreBarChart(snoreList, context, view);
            snoreBarChart.drawSnoreChart();
        } catch (Exception e) {
            Log.e(TAG, "SnoreChartFragment Exception: " + e, e);
            e.printStackTrace();
        } finally {
            Log.i(TAG, "End SnoreChartFragment onCreateView");
        }
        return view;
    }

    private void readSnoreList() {
        snoreList = new ArrayList<>();
        snoreList = (ArrayList<SnoreStorage>) getArguments().getSerializable("snoreList");
    }

}
