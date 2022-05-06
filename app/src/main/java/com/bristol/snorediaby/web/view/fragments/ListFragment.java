package com.bristol.snorediaby.web.view.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.bristol.snorediaby.R;
import com.bristol.snorediaby.web.view.activities.data.DataActivity;
import com.bristol.snorediaby.common.exceptions.SnoreException;
import com.bristol.snorediaby.repo.sqllite.CustomSqlLite;

public class ListFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private View view;

    private Context context;

    private Cursor cursor;

    private SQLiteDatabase db;

    private ListView sleep_list;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "Start ListFragment onCreateView");
        try {
            view = inflater.inflate(R.layout.fragment_list, container, false);
            context = getActivity();

            sleep_list = view.findViewById(R.id.sleep_list);
            CustomSqlLite helper = new CustomSqlLite(context, "DB.db", null, 1);
            db = helper.getReadableDatabase();
            //cursor = helper.getReadableDatabase().query("records", null, null, null, null, null, "_id");
            cursor = db.rawQuery("SELECT * FROM records ORDER BY _id", null);
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                context,
                R.layout.list_row,
                cursor,
                new String[] { "start_time", "snore_per", "lux_per" },
                new int[] { R.id.date_row, R.id.snore_row, R.id.lux_row },
                1
            );

            sleep_list.setOnItemLongClickListener((parent, view, position, id) -> {
                new AlertDialog.Builder(context)
                    .setTitle(R.string.warning)
                    .setMessage(R.string.delete_individual)
                    .setPositiveButton(R.string.yes_, (dialog, which) -> {
                        deleteData(id);
                        cursor = db.rawQuery("SELECT * FROM records ORDER BY _id", null);
                        SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(
                            context,
                            R.layout.list_row,
                            cursor,
                            new String[] { "start_time", "snore_per", "lux_per" },
                            new int[] { R.id.date_row, R.id.snore_row, R.id.lux_row },
                            1
                        );

                        sleep_list.setAdapter(adapter1);
                    })
                    .setNegativeButton(R.string.no_, (dialog, which) -> {
                        //Do nothing!!!
                    }).show();

                return true;
            });

            sleep_list.setOnItemClickListener((parent, view, position, id) -> {
                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(id));
                Intent intent = new Intent(context, DataActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            });

            sleep_list.setAdapter(adapter);
        } catch (Exception e) {
            SnoreException.getErrorException(TAG, e);
        } finally {
            Log.i(TAG, "End ListFragment onCreateView");
        }
        return view;
    }

    // 刪除資料，刪除id為id的資料
    private void deleteData(long id) {
        CustomSqlLite dbHelp = new CustomSqlLite(getActivity());
        SQLiteDatabase db2 = dbHelp.getWritableDatabase();
        db2.delete("records", "_id" + " = " + id, null);
        db2.delete("lux", "_id" + " = " + id, null);
        db2.delete("snoreblock", "_id" + " = " + id, null);
    }

}
